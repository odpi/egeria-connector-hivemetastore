/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.odpi.egeria.connectors.hms.ConnectorColumn;
import org.odpi.egeria.connectors.hms.ConnectorTable;
import org.odpi.egeria.connectors.hms.auditlog.HMSOMRSAuditCode;
import org.odpi.egeria.connectors.hms.auditlog.HMSOMRSErrorCode;
import org.odpi.egeria.connectors.hms.helpers.ExceptionHelper;
import org.odpi.egeria.connectors.hms.helpers.MapperHelper;
import org.odpi.egeria.connectors.hms.helpers.SupportedTypes;
import org.odpi.openmetadata.adapters.repositoryservices.caching.repository.CachedRepositoryAccessor;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventProcessor;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * OMRSDatabasePollingRepositoryEventMapper supports the event mapper function for a metastore used as an open metadata repository.
 *
 * This class is an implementation of an OMRS event mapper, it polls for content in the 3rd party metastore and puts
 * that content into an embedded Egeria repository. It then (if configured to send batch events) extracts the entities and relationships
 * from the embedded repository and sends a batch event for
 * 1) for the asset Entities and relationships
 * 2) for each RelationalTable, it's RelationalColumns and associated relationships
 */
@SuppressWarnings({"Var","Varifier"})
abstract public class OMRSEventProducer
{


    /**
     * Running field is a thread safe indicator that the thread is running. So stop the thread set the running flag to false.
     */
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * map to help manage type guids, by keeping a map for type name to guid.
     */
    private Map<String, String> typeNameToGuidMap = null;

    /**
     * UserId associated with this connector
     */
    private String userId = null;
    private String qualifiedNamePrefix = "";
    protected String metadataCollectionId = null;
    protected String metadataCollectionName = null;
    protected OMRSMetadataCollection metadataCollection = null;

    private String repositoryName = null;
    private String catName = null;
    private String dbName = null;
    private boolean sendPollEvents = false;

    private boolean includeDeployedSchema = false;
    private boolean cacheIntoCachingRepository = true;
    private String configuredEndpointAddress = null;

    private String databaseGUID;

    Thread worker = null;
    List<Relationship> aboveTableRelationshipList = new ArrayList<>();
    List<EntityDetail> aboveTableEntityList = new ArrayList<>();
    Map<String, List<EntityDetail>> qualifiedTableNameToEntityMap = new HashMap<>();
    Map<String, List<Relationship>> qualifiedTableNameToRelationshipMap = new HashMap<>();

    CachedRepositoryAccessor cachedRepositoryAccessor = null;
//    String baseCanonicalName = null;

    String relationalDBTypeGuid = null;

    MapperHelper mapperHelper = null;
    AuditLog auditLog;
    OMRSRepositoryHelper repositoryHelper;
    OMRSRepositoryConnector repositoryConnector;
    OMRSRepositoryEventProcessor repositoryEventProcessor;
    Map<String, Object> configurationProperties;
    EndpointProperties endpoint;
    Map<String, String> connectionSecuredProperties = null;

    /**
     * Default constructor
     */
    public OMRSEventProducer() {

    }
    public OMRSEventProducer(AuditLog auditLog,
                             OMRSRepositoryHelper repositoryHelper,
                             OMRSRepositoryConnector repositoryConnector,
                             OMRSRepositoryEventProcessor repositoryEventProcessor,
                             Map<String, Object> configurationProperties,
                             EndpointProperties endpoint,
                             String userId) throws ConnectorCheckedException {
        this.auditLog = auditLog;
        this.repositoryHelper = repositoryHelper;
        this.repositoryConnector =  repositoryConnector;
        this.repositoryEventProcessor = repositoryEventProcessor;
        this.configurationProperties =configurationProperties;
        this.endpoint = endpoint;
        this.repositoryName = this.repositoryConnector.getRepositoryName();
        extractConfigurationProperties(configurationProperties);
        this.userId = userId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getCatName() {
        return catName;
    }

    public String getDbName() {
        return dbName;
    }

    public boolean isSendPollEvents() {
        return sendPollEvents;
    }

    public EndpointProperties getEndpoint() {
        return endpoint;
    }

    public Map<String, String> getConnectionSecuredProperties() {
        return connectionSecuredProperties;
    }

    /**
     * Extract Egeria configuration properties into instance variables
     *
     * @param configurationProperties map of Egeria configuration variables.
     * @throws ConnectorCheckedException connector checked Exception
     */
    @SuppressWarnings("unchecked")
    protected void extractConfigurationProperties(Map<String, Object> configurationProperties) throws ConnectorCheckedException {
        String methodName = "extractConfigurationProperties";
        if (configurationProperties == null) {
            return;
        }

        String configuredQualifiedNamePrefix = (String) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.QUALIFIED_NAME_PREFIX);
        if (configuredQualifiedNamePrefix != null) {
            qualifiedNamePrefix = configuredQualifiedNamePrefix;
        }
        String configuredCatName = (String) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.CATALOG_NAME);
        if (configuredCatName != null) {
            catName = configuredCatName;
        }
        String configuredDBName = (String) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.DATABASE_NAME);
        if (configuredDBName != null) {
            dbName = configuredDBName;
        }

        Boolean configuredSendPollEvents = (Boolean) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.SEND_POLL_EVENTS);
        if (configuredSendPollEvents != null) {
            sendPollEvents = configuredSendPollEvents;
        }

        Boolean configuredCache = (Boolean) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.CACHE_INTO_CACHING_REPOSITORY);
        if (configuredCache != null) {
            cacheIntoCachingRepository = configuredCache;
        }

        Boolean configuredIncludeDeployedschema = (Boolean) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.INCLUDE_DEPLOYED_SCHEMA);
        if (configuredIncludeDeployedschema != null) {
            includeDeployedSchema = configuredIncludeDeployedschema;
        }
        configuredEndpointAddress = (String) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.ENDPOINT_ADDRESS);
        Map<String, String> configuredConnectionSecureProperties = null;
        try {
            configuredConnectionSecureProperties = (Map<String, String>) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.CONNECTION_SECURED_PROPERTIES);
        } catch (ClassCastException classCastException) {
            // it might be that the content of securedProperties does not cast to the expected LinkedHashMap<String, String>
            // if this is the case then throw an exception
            ExceptionHelper.raiseConnectorCheckedException(this.getClass().getName(), HMSOMRSErrorCode.CONFIG_ERROR_CONNECTION_SECURED_PROPERTIES, methodName, null);

        }
        if (configuredConnectionSecureProperties != null) {

            Set<Map.Entry<String, String>> entrySet = configuredConnectionSecureProperties.entrySet();
            connectionSecuredProperties = new HashMap<>();
            for (var entry : entrySet) {
                connectionSecuredProperties.put(entry.getKey(), entry.getValue());
            }
        }
    }

    synchronized public String getUserId() {
        return userId;
    }

    /**
     * Connect the 3rd party technology
     * @throws RepositoryErrorException repository exception
     * @throws ConnectorCheckedException connector exception
     */
    protected abstract void connectTo3rdParty() throws RepositoryErrorException, ConnectorCheckedException;

    /**
     * Get the names of the tables from the 3rd party technology
     * @param catName catalog name
     * @param dbName database name
     * @return a list of all the table names
     */
    protected abstract List<String> getTableNamesFrom3rdParty(String catName, String dbName);

    /**
     * Get the list of all catalog names from the 3rd party.
     * @throws ConnectorCheckedException connector checked exception
     * @return a list of catalog names
     */
    abstract protected List<String> getAllCatalogNamesFrom3rdParty() throws ConnectorCheckedException;
//
    /**
     * get database names under the catalog
     * @param catalogName name of the catalog to get the database names from
     * @return List of database names
     */
    protected abstract List<String> getDBNamesUnderCatalog(String catalogName);
    /**
     * Get a ConnectorTable (a technology independant representation of a table) from the 3rd party technology
     * @param catName catalog name
     * @param dbName database name
     * @param qualifiedName  used to construct qualified names
     * @param tableName name of the table to retrieve
     * @return a Connector table
     * @throws ConnectorCheckedException connector exception
     */
    protected abstract ConnectorTable getTableFrom3rdParty(String catName, String dbName, String qualifiedName, String tableName) throws ConnectorCheckedException;

    /**
     * Get the latest table content , construct Egeria events from them and send the events
     * @return true if there were no errors otherwise false;
     */
    public boolean execute() {
        String methodName = "execute";
        boolean status = false;
        mapperHelper = new MapperHelper(repositoryHelper,
                userId,
                metadataCollectionId,
                repositoryName,
                metadataCollectionName, qualifiedNamePrefix);

        try {
            getRequiredTypes();

            // connect to the 3rd party technology
            connectTo3rdParty();
            // reset the variables used to accumulate state


            if (dbName !=null) {
                // catName will either be specified or null.
                processDataBase(methodName, dbName);
            } else {
                // null catName means process all catalogs
                if (catName == null ) {
                    List<String> catNames = getAllCatalogNamesFrom3rdParty();
                    if (catNames != null) {
                        for (String currentCatName: catNames) {
                            processCatalogDatabases(methodName, currentCatName);
                        }
                    }
                } else {
                    processCatalogDatabases(methodName, catName);
                }
            }

            status = true;
        } catch (ConnectorCheckedException e) {
            String msg = "No Exception message";
            if (e.getMessage() != null) {
                msg = e.getMessage();
            }
            Throwable cause = e.getCause();
            if (cause == null) {
                //TODO change auditlog
                if (auditLog != null) {
                    auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_POLL_LOOP_GOT_AN_EXCEPTION.getMessageDefinition(msg));
                }
            } else {
                String causeMsg = "No cause message";
                if (cause.getMessage() != null) {
                    causeMsg = cause.getMessage();
                }
                //TODO change auditlog
                if (auditLog != null) {
                    auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_POLL_LOOP_GOT_AN_EXCEPTION_WITH_CAUSE.getMessageDefinition(msg, causeMsg));
                }
            }
        } catch (TypeErrorException e) {
            // TODO audit log
        } catch  (RepositoryErrorException e) {
            // TODO audit log
        }
        return status;
    }
    private void processCatalogDatabases(String methodName, String catNameToProcess) throws ConnectorCheckedException, TypeErrorException {
        List<String> dbNames = getDBNamesUnderCatalog(catNameToProcess);
        for (String currentDBName : dbNames) {
            processDataBase(methodName, currentDBName);
        }
    }

    private void processDataBase(String methodName, String currentDBName) throws ConnectorCheckedException, TypeErrorException {
        // reset state for database
        cachedRepositoryAccessor = new CachedRepositoryAccessor(userId, repositoryConnector.getServerName(), metadataCollection);
        aboveTableEntityList = new ArrayList<>();
        aboveTableRelationshipList = new ArrayList<>();
        qualifiedTableNameToEntityMap = new HashMap<>();
        qualifiedTableNameToRelationshipMap = new HashMap<>();
        // populate the above lists with the database and schema entities and relationships
        String qualifiedName = null;
        if (catName == null) {
            qualifiedName = currentDBName;
        } else {
            qualifiedName = catName + SupportedTypes.SEPARATOR_CHAR + currentDBName;
        }
        // collect the entities and relationships above the table(s)
        qualifiedName = collectEntitiesAndRelationshipsAboveTable(currentDBName, qualifiedName);

        if (cacheIntoCachingRepository) {
            refreshRepository(aboveTableEntityList, aboveTableRelationshipList );
        }
        // send batch event
        if (sendPollEvents) {
            issueBatchEvent(aboveTableEntityList, aboveTableRelationshipList);
        }

        List<String> tableNames = getTableNamesFrom3rdParty(catName, currentDBName);
        if (tableNames != null && !tableNames.isEmpty()) {
            // create each table and relationship
            for (String tableName : tableNames) {
                ConnectorTable connectorTable = getTableFrom3rdParty(catName, currentDBName, qualifiedName, tableName);
                qualifiedTableNameToEntityMap = new HashMap<>();
                qualifiedTableNameToRelationshipMap = new HashMap<>();
                convertToConnectorTableToEntitiesAndRelationships(methodName, connectorTable);
                String tableQualifiedName = connectorTable.getQualifiedName();
                List<EntityDetail> entityList = qualifiedTableNameToEntityMap.get(tableQualifiedName);
                List<Relationship> relationshipList = qualifiedTableNameToRelationshipMap.get(tableQualifiedName);

                if (cacheIntoCachingRepository) {
                    refreshRepository(entityList, relationshipList);
                }
                if (sendPollEvents) {
                    issueBatchEvent(entityList, relationshipList );
                }
            }
        }
    }

    /**
     * Collect the Entities and relationships above the tables(s)
     * @param currentDBName name of the database currently being processed
     * @param qualifiedName value to use as a basis of entities qualifiedNames
     * @return qualified name prefix
     * @throws ConnectorCheckedException connector exception
     */
    private String collectEntitiesAndRelationshipsAboveTable(String currentDBName, String qualifiedName) throws ConnectorCheckedException {
        String methodName = "collectEntitiesAndRelationshipsAboveTable";

        // Create database

        EntityDetail databaseEntity = mapperHelper.getEntityDetailSkeleton(methodName,
                SupportedTypes.DATABASE,
                currentDBName,
                qualifiedName,
                null,
                false);
        databaseGUID = databaseEntity.getGUID();
        saveEntityReferenceCopy(databaseEntity);


        createConnectionOrientatedEntities(qualifiedName, databaseEntity);
        String  deployedDatabaseSchemaEntityGuid = null;
        if (includeDeployedSchema) {
            qualifiedName = qualifiedName +SupportedTypes.SEPARATOR_CHAR + "defaultDeployedSchema";
            EntityDetail deployedDatabaseSchemaEntity = mapperHelper.getEntityDetailSkeleton(methodName,
                    SupportedTypes.DEPLOYED_DATABASE_SCHEMA,
                    SupportedTypes.DEFAULT_DEPLOYED_SCHEMA_TOKEN_NAME,
                    qualifiedName + SupportedTypes.SEPARATOR_CHAR + SupportedTypes.DEFAULT_DEPLOYED_SCHEMA_TOKEN_NAME,
                    null,
                    false);
            saveEntityReferenceCopy(deployedDatabaseSchemaEntity);

            deployedDatabaseSchemaEntityGuid = deployedDatabaseSchemaEntity.getGUID();
        }
        qualifiedName = qualifiedName + SupportedTypes.SEPARATOR_CHAR + SupportedTypes.DEFAULT_RELATIONAL_DB_SCHEMA_TYPE;
        // create RelationalDBType
        EntityDetail relationalDBTypeEntity = mapperHelper.getEntityDetailSkeleton(methodName,
                SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE,
                SupportedTypes.DEFAULT_RELATIONAL_DB_SCHEMA_TYPE,
                qualifiedName,
                null,
                false);
        saveEntityReferenceCopy(relationalDBTypeEntity);
        relationalDBTypeGuid = relationalDBTypeEntity.getGUID();

        // create Relationships

        if (includeDeployedSchema) {
            // asset => deployed schema
            aboveTableRelationshipList.add(mapperHelper.createReferenceRelationship(SupportedTypes.DATA_CONTENT_FOR_DATASET,
                    databaseGUID,
                    SupportedTypes.DATABASE,
                    deployedDatabaseSchemaEntityGuid,
                    SupportedTypes.DEPLOYED_DATABASE_SCHEMA));
            // deployed schema => relational schema type
            aboveTableRelationshipList.add(mapperHelper.createReferenceRelationship(SupportedTypes.ASSET_SCHEMA_TYPE,
                    deployedDatabaseSchemaEntityGuid,
                    SupportedTypes.DEPLOYED_DATABASE_SCHEMA,
                    relationalDBTypeEntity.getGUID(),
                    SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE));
        } else {
            // asset => relational schema type
            aboveTableRelationshipList.add(mapperHelper.createReferenceRelationship(SupportedTypes.ASSET_SCHEMA_TYPE,
                    databaseGUID,
                    SupportedTypes.DATABASE,
                    relationalDBTypeGuid,
                    SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE));
        }
        return qualifiedName;
    }

    /**
     * refresh the repository with the entities and relationships for a table
     *
     * @throws ConnectorCheckedException connector exception
     */
    private void refreshRepository(List<EntityDetail> entities, List<Relationship> relationships) throws ConnectorCheckedException {

        for (EntityDetail entity : entities) {
            cachedRepositoryAccessor.saveEntityReferenceCopyToStore(entity);
        }
        for (Relationship relationship : relationships) {
            cachedRepositoryAccessor.saveRelationshipReferenceCopyToStore(relationship);
        }
    }


    /**
     * Check that the repository supports the required types. This is done in a loop with small delays as the types are added asynchonously, so we need to loop until we get the
     * required types
     *
     * @throws ConnectorCheckedException connector exception
     */
    private void getRequiredTypes() throws ConnectorCheckedException {
        String methodName = "getRequiredTypes";
        int supportedCount = SupportedTypes.supportedTypeNames.size();

        int typesAvailableCount = 0;
        int retryCount = 0;
        while ((typesAvailableCount != supportedCount)) {
            if (auditLog != null) {
                auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_ACQUIRING_TYPES_LOOP.getMessageDefinition(typesAvailableCount + "", supportedCount + "", retryCount + ""));
            }
            // only come out the while loop when we can get all of the supported types in one iteration.
            typesAvailableCount = 0;
            if (typeNameToGuidMap == null) {
                typeNameToGuidMap = new HashMap<>();
            }
            // populate the type name to guid map
            for (String typeName : SupportedTypes.supportedTypeNames) {

                TypeDef typeDef = repositoryHelper.getTypeDefByName("HMSOMRSRepositoryEventMapper",
                        typeName);
                if (typeDef != null) {
                    if (auditLog != null) {
                        auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_ACQUIRING_TYPES_LOOP_FOUND_TYPE.getMessageDefinition(typeName));
                    }
                    typeNameToGuidMap.put(typeName, typeDef.getGUID());
                    typesAvailableCount++;
                }
            }
            if (typesAvailableCount < supportedCount) {
                //delay for 1 second and then retry

                try {
                    Thread.sleep(1000);  // TODO Should this be in configuration?
                    retryCount++;
                } catch (InterruptedException e) {
                    // should not happen as there is only one thread
                    // if it does happen it would result in a lower duration for the sleep
                    //
                    // Increment the retry count, in case this happens everytime
                    retryCount++;
                    if (auditLog != null) {
                        auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_ACQUIRING_TYPES_LOOP_INTERRUPTED_EXCEPTION.getMessageDefinition());

                    }  }
            } else if (typesAvailableCount == supportedCount) {
                // log to say we have all the types we need
                if (auditLog != null) {
                    auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_ACQUIRED_ALL_TYPES.getMessageDefinition());
                }
            }

            if (retryCount == 20) { // TODO  Should this be in configuration?
                ExceptionHelper.raiseConnectorCheckedException(this.getClass().getName(), HMSOMRSErrorCode.EVENT_MAPPER_CANNOT_GET_TYPES, methodName, null);
            }
        }
    }

//
//
//    /**
//     * convert the connector tables to entities and relationships
//     *
//     * @param connectorTables connector tables
//     * @throws ConnectorCheckedException connector exception
//     * @throws TypeErrorException        type exception
//     */
//    void convertToConnectorTablesToEntitiesAndRelationships(List<ConnectorTable> connectorTables) throws ConnectorCheckedException, TypeErrorException {
//        String methodName = "convertToConnectorTablesToEntitiesAndRelationships";
//
//        for (ConnectorTable connectorTable : connectorTables) {
//            convertToConnectorTableToEntitiesAndRelationships(methodName, connectorTable);
//        }
//    }

    synchronized private void convertToConnectorTableToEntitiesAndRelationships(String methodName, ConnectorTable connectorTable) throws ConnectorCheckedException, TypeErrorException {
        String tableQualifiedName = connectorTable.getQualifiedName();
        EntityDetail tableEntity = mapperHelper.getEntityDetailSkeleton(methodName,
                SupportedTypes.TABLE,
                connectorTable.getName(),
                tableQualifiedName,
                null,
                true);

        tableEntity.setCreateTime(connectorTable.getCreateTime());


        List<Classification> tableClassifications = tableEntity.getClassifications();
        if (tableClassifications == null) {
            tableClassifications = new ArrayList<>();
        }

        Classification classification = mapperHelper.createTypeEmbeddedClassificationForTable(methodName, tableEntity);
        tableClassifications.add(classification);
        String tableType = connectorTable.getType();

        if (tableType != null && tableType.equals("VIRTUAL_VIEW")) {
            //Indicate that this hmsTable is a view using the classification
            tableClassifications.add(mapperHelper.createCalculatedValueClassification("refreshRepository", tableEntity, connectorTable.getHmsViewOriginalText()));
        }
        if (!tableClassifications.isEmpty()) {
            tableEntity.setClassifications(tableClassifications);
        }

        saveEntityReferenceCopyForTable(tableEntity, tableQualifiedName);
        String tableGuid = tableEntity.getGUID();

        // relationship

        Relationship relationship =mapperHelper.createReferenceRelationship(SupportedTypes.ATTRIBUTE_FOR_SCHEMA,
                relationalDBTypeGuid,
                SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE,
                tableGuid,
                SupportedTypes.TABLE);
        saveRelationshipReferenceCopyForTable(relationship,tableQualifiedName);

        Iterator<ConnectorColumn> colsIterator = connectorTable.getColumns().listIterator();

        while (colsIterator.hasNext()) {
            ConnectorColumn connectorColumn = colsIterator.next();
            String columnName = connectorColumn.getName();
            // TODO change the name for a derived column?

            EntityDetail columnEntity = mapperHelper.getEntityDetailSkeleton(methodName,
                    SupportedTypes.COLUMN,
                    columnName,
                    connectorTable.getQualifiedName() + SupportedTypes.SEPARATOR_CHAR + columnName,
                    null,
                    true);
            String dataType = connectorColumn.getType();
            List<Classification> columnClassifications = columnEntity.getClassifications();
            if (columnClassifications == null) {
                columnClassifications = new ArrayList<>();
            }

            columnClassifications.add(mapperHelper.createTypeEmbeddedClassificationForColumn("refreshRepository", columnEntity, dataType));

            columnEntity.setClassifications(columnClassifications);

            saveEntityReferenceCopyForTable(columnEntity, tableQualifiedName);

            // relate the column to the table
            relationship=mapperHelper.createReferenceRelationship(SupportedTypes.NESTED_SCHEMA_ATTRIBUTE,
                    tableGuid,
                    SupportedTypes.TABLE,
                    columnEntity.getGUID(),
                    SupportedTypes.COLUMN);
            saveRelationshipReferenceCopyForTable(relationship,tableQualifiedName);
        }
    }

    /**
     * Create Connection orientated entities. an Asset can be associated with a Connection,
     * which in turn has a ConnectionType and an Endpoint. Entities of these 3 types are created in this method
     * and relationships are created between them.
     * <p>
     * For more information on these entities see https://egeria-project.org/patterns/metadata-manager/overview/?h=asset+connections#asset-connections
     *
     * @param baseCanonicalName a unique name used as a base to create unique names for the entities
     * @param databaseEntity    the Database (which is a type of Asset)
     * @throws ConnectorCheckedException connector exception
     */
    private void createConnectionOrientatedEntities(String baseCanonicalName, EntityDetail databaseEntity) throws ConnectorCheckedException {
        String methodName = "createConnectionOrientatedEntities";
        String canonicalName = baseCanonicalName + SupportedTypes.SEPARATOR_CHAR + SupportedTypes.CONNECTION_VALUE;

        EntityDetail connectionEntity = mapperHelper.getEntityDetailSkeleton(methodName,
                SupportedTypes.CONNECTION,
                SupportedTypes.CONNECTION_VALUE,
                canonicalName,
                null,
                false);
        InstanceProperties resultingProperties = connectionEntity.getProperties();

        if (connectionSecuredProperties != null) {
            InstanceProperties mapInstanceProperties  = repositoryHelper.addStringPropertyMapToInstance("sourceName",
                    null,
                    "securedProperties",
                    connectionSecuredProperties,
                    methodName);
            // TODO correct sourceName
            if (mapInstanceProperties != null)
            {
                MapPropertyValue mapPropertyValue = new MapPropertyValue();
                mapPropertyValue.setMapValues(mapInstanceProperties);
//            mapPropertyValue.setTypeGUID(stringMapTypeGUID);
//            mapPropertyValue.setTypeName(stringMapTypeName);
                resultingProperties.setProperty("securedProperties", mapPropertyValue);

            }
            connectionEntity.setProperties(resultingProperties);
        }
        saveEntityReferenceCopy(connectionEntity);


        canonicalName = baseCanonicalName + SupportedTypes.SEPARATOR_CHAR + SupportedTypes.CONNECTOR_TYPE_VALUE;

        EntityDetail connectionTypeEntity = mapperHelper.getEntityDetailSkeleton(methodName,
                SupportedTypes.CONNECTOR_TYPE,
                SupportedTypes.CONNECTOR_TYPE_VALUE,
                canonicalName,
                null,
                false);
        saveEntityReferenceCopy(connectionTypeEntity);

        canonicalName = baseCanonicalName + SupportedTypes.SEPARATOR_CHAR + SupportedTypes.ENDPOINT_VALUE;

        EntityDetail endpointEntity = mapperHelper.getEntityDetailSkeleton(methodName,
                SupportedTypes.ENDPOINT,
                SupportedTypes.ENDPOINT_VALUE,
                canonicalName,
                null,
                false);
        InstanceProperties epInstanceProperties = endpointEntity.getProperties();
        //TODO does passing a protocol make sense here?
        if (configuredEndpointAddress != null) {
            repositoryHelper.addStringPropertyToInstance(methodName,
                    epInstanceProperties,
                    "networkAddress",
                    configuredEndpointAddress,
                    methodName);
            endpointEntity.setProperties(epInstanceProperties);
        }


        saveEntityReferenceCopy(endpointEntity);
        // create relationships

        // entity guids used to create proxies
        String connectionGuid = connectionEntity.getGUID();
        String databaseGuid = databaseEntity.getGUID();
        String connectionTypeGuid = connectionTypeEntity.getGUID();
        String endPointGuid = endpointEntity.getGUID();

        // create the 3 relationships
        aboveTableRelationshipList.add(mapperHelper.createReferenceRelationship(SupportedTypes.CONNECTION_TO_ASSET,
                connectionGuid,
                SupportedTypes.CONNECTION,
                databaseGuid,
                SupportedTypes.DATABASE));

        aboveTableRelationshipList.add(mapperHelper.createReferenceRelationship(SupportedTypes.CONNECTION_CONNECTOR_TYPE,
                connectionGuid,
                SupportedTypes.CONNECTION,
                connectionTypeGuid,
                SupportedTypes.CONNECTOR_TYPE));

        aboveTableRelationshipList.add(mapperHelper.createReferenceRelationship(SupportedTypes.CONNECTION_ENDPOINT,
                connectionGuid,
                SupportedTypes.CONNECTION,
                endPointGuid,
                SupportedTypes.ENDPOINT
        ));
    }

    private void saveEntityReferenceCopy(EntityDetail entityToAdd) {
        aboveTableEntityList.add(entityToAdd);
    }

    private void saveEntityReferenceCopyForTable(EntityDetail entityToAdd, String qualifiedTableName) {
        List<EntityDetail> entities = qualifiedTableNameToEntityMap.get(qualifiedTableName);
        if (entities == null) {
            entities= new ArrayList<>();
            entities.add(entityToAdd);
            qualifiedTableNameToEntityMap.put(qualifiedTableName,entities);
        } else {
            entities.add(entityToAdd);
        }
    }
    private void saveRelationshipReferenceCopyForTable(Relationship relationshipToAdd, String qualifiedTableName) {
        List<Relationship> relationships = qualifiedTableNameToRelationshipMap.get(qualifiedTableName);
        if (relationships ==null) {
            relationships =new ArrayList<>();
            relationships.add(relationshipToAdd);
            qualifiedTableNameToRelationshipMap.put(qualifiedTableName, relationships);
        } else {
            relationships.add(relationshipToAdd);
        }
    }
    /**
     * Issue the batch event with the list of supplied entities and relationships
     *
     * @param entityList       entities to include in the event
     * @param relationshipList relationships to include in the event
     */
    private void issueBatchEvent(List<EntityDetail> entityList, List<Relationship> relationshipList ) {
        InstanceGraph instances = new InstanceGraph(entityList, relationshipList);

        // send the event
        repositoryEventProcessor.processInstanceBatchEvent("OMRSDatabasePollingRepositoryEventMapper",
                repositoryConnector.getMetadataCollectionId(),
                repositoryConnector.getServerName(),
                repositoryConnector.getServerType(),
                repositoryConnector.getOrganizationName(),
                instances);
    }
}
