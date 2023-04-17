/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;
import org.odpi.egeria.connectors.hms.ConnectorColumn;
import org.odpi.egeria.connectors.hms.ConnectorTable;
import org.odpi.egeria.connectors.hms.auditlog.HMSOMRSAuditCode;
import org.odpi.egeria.connectors.hms.auditlog.HMSOMRSErrorCode;
import org.odpi.egeria.connectors.hms.helpers.ExceptionHelper;
import org.odpi.egeria.connectors.hms.helpers.SupportedTypes;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventProcessor;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.io.IOException;
import java.util.*;


/**
 * HMSOMRSRepositoryEventMapper supports the event mapper function for a Hive metastore used as an open metadata repository.
 *
 * This class is an implementation of an OMRS event mapper, it polls for content in Hive metastore and puts
 * that content into an embedded Egeria repository. It then (if configured to send batch events) extracts the entities and relationships
 * from the embedded repository and sends a batch event for
 * 1) for the asset Entities and relationships
 * 2) for each RelationalTable, it's RelationalColumns and associated relationships
 */

@SuppressWarnings({"Var","Varifier"})
public class HMSOMRSEventProducer extends OMRSEventProducer
{

    public static final String SPARK_SQL_SOURCES_SCHEMA_NUM_PARTS = "spark.sql.sources.schema.numParts";
    public static final String SPARK_SQL_SOURCES_SCHEMA_PART = "spark.sql.sources.schema.part.";
    public static final String SPARK_SQL_SOURCES_SCHEMA = "spark.sql.sources.schema";
    private IMetaStoreClientFacade client = null;


    private final String className = this.getClass().getName();
    /**
     * Default constructor
     */
    public HMSOMRSEventProducer() {
        super();
    }

    public HMSOMRSEventProducer(AuditLog auditLog,
                                OMRSRepositoryHelper repositoryHelper,
                                OMRSRepositoryConnector repositoryConnector,
                                OMRSRepositoryEventProcessor repositoryEventProcessor,
                                Map<String, Object> configurationProperties,
                                EndpointProperties endpoint,
                                String userId) throws ConnectorCheckedException {
        super(auditLog, repositoryHelper, repositoryConnector, repositoryEventProcessor, configurationProperties, endpoint, userId);

    }

    public IMetaStoreClientFacade getClient() {
        return client;
    }

    public void setClient(IMetaStoreClientFacade client) {
        this.client = client;
    }

    /**
     * Connect to Hive Meta Store using the configuration parameters
     *
     * @throws ConnectorCheckedException could not connect to HMS
     * @throws RepositoryErrorException repository error - endpoint not supplied.
     */
    @Override
    protected void connectTo3rdParty() throws RepositoryErrorException, ConnectorCheckedException {
        String methodName = "connectTo3rdParty";
        Boolean configuredUseSSL = (Boolean) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.USE_SSL);
        boolean useSSL = false;
        if (configuredUseSSL != null) {
            useSSL = configuredUseSSL;
        }
        String configuredCatName = (String) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.CATALOG_NAME);
        String catName =null;
        String dbName = null;
        if (configuredCatName != null) {
            catName = configuredCatName;
        }
        String configuredDBName = (String) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.DATABASE_NAME);
        if (configuredDBName != null) {
            dbName = configuredDBName;
        }
        if (useSSL && catName == null && dbName != null) {
            // useSSL = true is only used with IBM Data Engine
            // At this time it is not possible to get databases without a named catalog.
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.NEED_NAMED_CATALOG_IN_CONFIG, methodName, null, "null");
        }

        String metadata_store_userId =null;
        String configuredMetadataStoreUserId = (String) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_USER);
        if (configuredMetadataStoreUserId != null) {
            metadata_store_userId = configuredMetadataStoreUserId;
        }
        String metadata_store_password = null;
        String configuredMetadataStorePassword = (String) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_PASSWORD);
        if (configuredMetadataStorePassword != null) {
            metadata_store_password = configuredMetadataStorePassword;
        }

        if (endpoint == null) {
            ExceptionHelper.raiseRepositoryErrorException(className, HMSOMRSErrorCode.ENDPOINT_NOT_SUPPLIED_IN_CONFIG, methodName, null, "null");
        } else {
            // populate the Hive configuration for the HMS client.
            Configuration conf = new Configuration();
            // we only support one thrift uri at this time
            conf.set("metastore.thrift.uris", endpoint.getAddress());
            if (useSSL) {
                conf.set("metastore.use.SSL", "true");
                conf.set("metastore.truststore.path", "file:///" + System.getProperty("java.home") + "/lib/security/cacerts");
                conf.set("metastore.truststore.password", "changeit");
                conf.set("metastore.client.auth.mode", "PLAIN");
                conf.set("metastore.client.plain.username", metadata_store_userId);
                conf.set("metastore.client.plain.password", metadata_store_password);
            }
            // if this is not specified then client side user and group checking occurs on the file system.
            // As the server is remote and may not be on this machine, we remove this check.
            // If this is set / or left to default to true then we get this error:
            // "java.lang.RuntimeException: java.lang.RuntimeException: java.lang.ClassNotFoundException:
            // Class org.apache.hadoop.security.JniBasedUnixGroupsMappingWithFallback not found"

            // TODO consider allowing the user to provide their own config to allow them configuration flexibility
            conf.set("metastore.execute.setugi", "false");

            try {
                if (client == null) {
                    // we are not testing
                    client = new HMSMetaStoreClientFacade(conf);
                }
            } catch (MetaException e) {
                ExceptionHelper.raiseConnectorCheckedException(this.getClass().getName(), HMSOMRSErrorCode.FAILED_TO_START_CONNECTOR, methodName, null);
            }
            metadataCollection = this.repositoryConnector.getMetadataCollection();
            metadataCollectionId = metadataCollection.getMetadataCollectionId(getUserId());
        }
    }

    @Override
    protected List<String> getTableNamesFrom3rdParty(String catName, String dbName) {
        String methodName = "refreshRepository";
        List<String> tableNames = new ArrayList<>();

        try {
            tableNames = client.getTables(catName, dbName, "*");
        } catch (TException e) {
            auditLog.logMessage(methodName, HMSOMRSAuditCode.HIVE_GETTABLES_FAILED.getMessageDefinition(e.getMessage()));
        }
        return tableNames;
    }
    @Override
    protected List<String> getAllCatalogNamesFrom3rdParty() throws ConnectorCheckedException {
        String methodName = "getAllCatalogNamesFrom3rdParty";
        List<String> catNames = new ArrayList<>();

        try {
            catNames = client.getCatalogs();
        } catch (TException e) {
            auditLog.logMessage(methodName, HMSOMRSAuditCode.HIVE_GETCATALOGS_FAILED.getMessageDefinition(e.getMessage()));
            // stop the connector if we have no catalogs, as there is nothing to sync.
            ExceptionHelper.raiseConnectorCheckedException(this.getClass().getName(), HMSOMRSErrorCode.NO_CATALOGS_EXCEPTION, methodName, e);
        }

        return catNames;
    }
    @Override
    protected List<String> getDBNamesUnderCatalog(String catalogName) {
        String methodName = "getDBNamesUnderCatalog";
        List<String>dbNames = new ArrayList<>();

        try {
            if (catalogName == null) {
                // some HMS implementations do not support passing null as the catalog name parameter
                dbNames = client.getAllDatabases();
            } else {
                dbNames = client.getAllDatabases(catalogName);
            }
        } catch (TException e) {
            if (catalogName == null) {
                catalogName = "default";
            }
            auditLog.logMessage(methodName, HMSOMRSAuditCode.HIVE_GETDATABASES_FAILED.getMessageDefinition(e.getMessage(), catalogName));
        }
        return dbNames;
    }
    protected ConnectorTable getTableFrom3rdParty(String catName, String dbName, String qualifiedName, String tableName) throws ConnectorCheckedException {
        String  methodName = "getTableFrom3rdParty";
        ConnectorTable connectorTable = null;

        Table hmsTable = null;
        try {
            hmsTable = client.getTable(catName, dbName, tableName);
        } catch (TException e) {
            auditLog.logMessage(methodName, HMSOMRSAuditCode.HIVE_GETTABLE_FAILED.getMessageDefinition(tableName, e.getMessage()));
        }
        if (hmsTable != null) {

//            // uncomment the below code to produce files containing Tables from a running system.
//            ObjectMapper om = new ObjectMapper();
//            try {
//                String tableJson = om.writeValueAsString(hmsTable);
//                FileWriter fileWriter = new FileWriter("/Users/xxx/testtable-" + hmsTable.getTableName());
//                PrintWriter printWriter = new PrintWriter(fileWriter);
//                printWriter.print(tableJson);
//
//                printWriter.close();
//            } catch (JsonProcessingException e) {
//
//            } catch (IOException e) {
//
//            }
            connectorTable = getTableFromHMSTable(qualifiedName, hmsTable);
//            Iterator<FieldSchema> colsIterator = hmsTable.getSd().getColsIterator();
//

        }
        return connectorTable;
    }

    /**
     * This method takes in an HMS table and converts it to a ConnectorTable, which is a technology independent version of the table.
     *
     * The column information is embedded in the HMS table. There are 3 places that we look for columns
     * For an external table we look for spark content:
     * - we look for to see if there are schema parts in the table parameters, if there are then we stitch together the schema parts;
     *   and map this json to ConnectorColumns in the ConnectorTable. This is Spark 2 format
     * - if there is a connector schema then map this json to ConnectorColumns in the ConnectorTable. This is a Spark 3 format
     * If we have not found any columns, in all other cases:
     * - look for the columns in the storage descriptor
     *
     * @param qualifiedName qualified name to use to construct ConnectorTable and Connectorcolumns
     * @param hmsTable hmsTable HMS table to extract information from
     * @return Connector table connector table
     * @throws ConnectorCheckedException connector checked exception
     */
    @SuppressWarnings("JavaUtilDate")
    private ConnectorTable getTableFromHMSTable(String qualifiedName, Table hmsTable) throws ConnectorCheckedException {
        var connectorTable = new ConnectorTable();
        String tableName = hmsTable.getTableName();
        String tableType = hmsTable.getTableType();
        String tableCanonicalName = qualifiedName + SupportedTypes.SEPARATOR_CHAR + tableName;
        long createTime = hmsTable.getCreateTime();
        //                            String owner = hmsTable.getOwner();
        //                            if (owner != null) {
        //                               TODO Can we store this on the hmsTable ?
        //                            }
        // the date for HMS is an int, and according to rHive code is the number of seconds since 1970.
        // In Hive it is created using int time = (int) (System.currentMilliSeconds() / 1000)
        // the below code is looking to get back the long- but does to the casting and the division the date is not
        // correct.
        Date createTimeDate = new Date(createTime*1000);
        connectorTable.setName(tableName);
        connectorTable.setCreateTime(createTimeDate);
        connectorTable.setQualifiedName(tableCanonicalName);
        connectorTable.setType(tableType);

        if (tableType != null && tableType.equals("EXTERNAL_TABLE")) {
            Map<String, String> parameters = hmsTable.getParameters();
            String numberOfSchemaPartsString = parameters.get(SPARK_SQL_SOURCES_SCHEMA_NUM_PARTS);
            String schemaAsJSON = null;
            if (numberOfSchemaPartsString == null) {
                schemaAsJSON = parameters.get(SPARK_SQL_SOURCES_SCHEMA);
            } else {
                Integer numberOfSchemaParts = Integer.valueOf(numberOfSchemaPartsString);
                schemaAsJSON= "";
                //stitch together the parts
                for (int i = 0; i < numberOfSchemaParts; i++) {
                    schemaAsJSON = schemaAsJSON + parameters.get(SPARK_SQL_SOURCES_SCHEMA_PART + i);
                }
            }
            if (schemaAsJSON != null) {
                // Note that I attempted to use the SparkSchemaBean in the test folder to deserialise the json, but it errored.
                // So I am walking the json nodes to extract the information
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode topJsonNode = objectMapper.readTree(schemaAsJSON);
                    Iterator<String> iterator = topJsonNode.fieldNames();
                    while (iterator.hasNext()) {
                        String field = iterator.next();
                        if (field.equals("fields")) {
                            JsonNode fieldsJsonNode = topJsonNode.get("fields");
                            if (fieldsJsonNode.isArray()) {
                                ArrayNode fieldsArrayNode = (ArrayNode) fieldsJsonNode;
                                for (int j = 0; j < fieldsArrayNode.size(); j++) {
                                    JsonNode columnJsonNode = fieldsArrayNode.get(j);
                                    Iterator<Map.Entry<String, JsonNode>> columnDetails = columnJsonNode.fields();
                                    String columnName = null;
                                    String dataType = null;
                                    while (columnDetails.hasNext()) {
                                        Map.Entry<String, JsonNode> columnDetail = columnDetails.next();
                                        String columnDetailName = columnDetail.getKey();
                                        String columnDetailValue = columnDetail.getValue().asText();
                                        if (columnDetailName.equals("name")) {
                                            columnName = columnDetailValue;
                                        }
                                        if (columnDetailName.equals("type")) {
                                            dataType = columnDetailValue;
                                        }
                                    }
                                    if (columnName != null && dataType != null) {
                                        var column = new ConnectorColumn();
                                        column.setName(columnName);
                                        column.setQualifiedName(connectorTable.getQualifiedName() + SupportedTypes.SEPARATOR_CHAR + columnName);
                                        column.setType(dataType);
                                        column.setCreateTime(createTimeDate);

                                        connectorTable.addColumn(column);
                                    }
                                }

                            }
                        }
                    }
                } catch (IOException e) {
                    ExceptionHelper.raiseConnectorCheckedException(this.getClass().getName(), HMSOMRSErrorCode.FAILED_TO_GET_COLUMNS_FOR_EXTERNAL_TABLE, tableName, null);
                }
            }
        }
        // if we have not found any Spark columns for external tables - in all other cases we look for columns in the storage descriptor
        if (connectorTable.getColumns() == null) {
            Iterator<FieldSchema> colsIterator = hmsTable.getSd().getColsIterator();
            while (colsIterator.hasNext()) {
                FieldSchema fieldSchema = colsIterator.next();
                String columnName = fieldSchema.getName();
                String dataType = fieldSchema.getType();

                var column = new ConnectorColumn();
                column.setName(columnName);
                column.setQualifiedName(connectorTable.getQualifiedName() + SupportedTypes.SEPARATOR_CHAR + columnName);
                column.setType(dataType);
                column.setCreateTime(createTimeDate);
                connectorTable.addColumn(column);
            }
        }
        return connectorTable;
    }
}
