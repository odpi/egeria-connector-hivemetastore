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
    private IMetaStoreClient client = null;


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

    public IMetaStoreClient getClient() {
        return client;
    }

    public void setClient(IMetaStoreClient client) {
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
                    client = new HiveMetaStoreClient(conf, null, false);
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

    @SuppressWarnings("JavaUtilDate")
    private ConnectorTable getTableFromHMSTable(String qualifiedName, Table hmsTable) throws ConnectorCheckedException {
        var connectorTable = new ConnectorTable();
        String tableName = hmsTable.getTableName();
        String tableType = hmsTable.getTableType();
        String tableCanonicalName = qualifiedName + SupportedTypes.SEPARATOR_CHAR + tableName;
        String typeName = SupportedTypes.TABLE;
        int createTime = hmsTable.getCreateTime();
        //                            String owner = hmsTable.getOwner();
        //                            if (owner != null) {
        //                               TODO Can we store this on the hmsTable ?
        //                            }

        connectorTable.setName(tableName);
        connectorTable.setCreateTime(new Date(createTime));
        connectorTable.setQualifiedName(tableCanonicalName);
        connectorTable.setType(tableType);
        connectorTable.setType(typeName);

        if (tableType != null && tableType.equals("EXTERNAL_TABLE")) {
            Map<String, String> parameters = hmsTable.getParameters();
            String  numberOfSchemaPartsString = parameters.get(SPARK_SQL_SOURCES_SCHEMA_NUM_PARTS);
            if (numberOfSchemaPartsString != null) {
                Integer numberOfSchemaParts = Integer.valueOf(numberOfSchemaPartsString);
                String schemaAsJSON = "";
                //stitch together the parts
                for (int i=0; i< numberOfSchemaParts; i++) {
                    schemaAsJSON = schemaAsJSON + parameters.get(SPARK_SQL_SOURCES_SCHEMA_PART + i);
                }
                ObjectMapper objectMapper = new ObjectMapper();

                try {
//                    objectMapper.readValue(schemaAsJSON,.class);


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
                                        connectorTable.addColumn(column);
                                    }
                                }

                            }
                        }
                    }
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException(e); //TODO handle properly
                }  catch (IOException e) {
                    ExceptionHelper.raiseConnectorCheckedException(this.getClass().getName(), HMSOMRSErrorCode.FAILED_TO_GET_COLUMNS_FOR_EXTERNAL_TABLE, tableName, null);
                }
            }

        } else {
            Iterator<FieldSchema> colsIterator = hmsTable.getSd().getColsIterator();
            while (colsIterator.hasNext()) {
                FieldSchema fieldSchema = colsIterator.next();
                String columnName = fieldSchema.getName();
                String dataType = fieldSchema.getType();

                var column = new ConnectorColumn();
                column.setName(columnName);
                column.setQualifiedName(connectorTable.getQualifiedName() + SupportedTypes.SEPARATOR_CHAR + columnName);
                column.setType(dataType);
                connectorTable.addColumn(column);
            }
        }


        return connectorTable;
    }
}
