/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

//import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.junit.jupiter.api.Test;
import org.odpi.egeria.connectors.hms.helpers.SupportedTypes;
import org.odpi.openmetadata.adapters.repositoryservices.caching.repositoryconnector.CachingOMRSRepositoryProxyConnector;
import org.odpi.openmetadata.adapters.repositoryservices.caching.repositoryconnector.CachingOMRSRepositoryProxyConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class OMRSDatabasePollingRepositoryEventMapperTest {
    public static final String DB_NAME_VALUE = "default";
    public static final String CATALOG_NAME_VALUE = "spark";
    public static final String QUALIFIED_NAME_PREFIX = "data-engine::";

    public static final String ENDPOINT_NETWORK_ADDRESS = "jdbc://testing.com";
    public static final String DATABASE_QUALIFIED_NAME = QUALIFIED_NAME_PREFIX + CATALOG_NAME_VALUE + "." + DB_NAME_VALUE;

    public static final String TEST_USER = "testUser";
    public static final String TEST_PW = "testPW";
    public static final String SPARK_2_FORMAT_TABLE_1 = "spark2formatTable1";
    public static final String SPARK_3_FORMAT_TABLE_1 = "spark3formatTable1";

    public static final String VIEW_1 = "view1";

    final List<MockColumn> TWO_COLUMNS = Arrays.asList(
            new MockColumn("col1", "array<string>"),
            new MockColumn("col2", "string")
    );

    final Set<String> TWO_COLUMNS_NAMES = TWO_COLUMNS.stream().map(x -> x.getName()).collect(Collectors.toSet());

    final List<MockColumn> THREE_STRING_COLUMNS = Arrays.asList(
            new MockColumn("f1", "string"),
            new MockColumn("f2", "string"),
            new MockColumn("f3", "string")
    );
    final Set<String> THREE_STRING_COLUMNS_NAMES = THREE_STRING_COLUMNS.stream().map(x -> x.getName()).collect(Collectors.toSet());


    @Test
    protected void mapperTestNoHMSContent() throws ConnectionCheckedException, IllegalAccessException, ConnectorCheckedException, NoSuchFieldException {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.DATABASE_NAME, "default");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CATALOG_NAME, "spark");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.QUALIFIED_NAME_PREFIX, "data-engine::");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.USE_SSL, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CACHE_INTO_CACHING_REPOSITORY, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.SEND_POLL_EVENTS, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_USER, "testUser");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_PASSWORD, "testPW");
        Map<String, String> connectionSecuredPropertiesMap = new HashMap<>();
        connectionSecuredPropertiesMap.put("aaa", "aaa-value");
        connectionSecuredPropertiesMap.put("bbb", "bbb-value");
        connectionSecuredPropertiesMap.put("ccc", "ccc-value");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CONNECTION_SECURED_PROPERTIES, connectionSecuredPropertiesMap);

        OMRSDatabasePollingRepositoryEventMapper omrsDatabasePollingRepositoryEventMapper = getOmrsDatabasePollingRepositoryEventMapper(configProperties);

        omrsDatabasePollingRepositoryEventMapper.setTesting();
        omrsDatabasePollingRepositoryEventMapper.setClient(new MockMetaStoreClient());

        OMRSRepositoryHelper repositoryHelper = new MockRepositoryHelper();
        OMRSMetadataCollection collection = new MockMetadataCollection(null, "test-repoName", repositoryHelper, null, "test-md-collection-id");
        List<InstanceGraph> graphs = getInstanceGraphs(omrsDatabasePollingRepositoryEventMapper, repositoryHelper, collection);

        assertNotNull(graphs);
        assert (graphs.size() == 1);
        assert (graphs.get(0).getEntities().size() == 5);
        assert (graphs.get(0).getRelationships().size() == 4);

        checkQualifiedNamesAreUnique(graphs);

    }

    @Test
    protected void mapperTestHMSContentWithoutDeployedDBSchema() throws InvalidParameterException, ConnectionCheckedException, RepositoryErrorException, UserNotAuthorizedException, EntityProxyOnlyException, IOException, EntityNotKnownException, ConnectorCheckedException, NoSuchFieldException, IllegalAccessException {
        mapperTestHMSContent(false);
    }

    @Test
    protected void mapperTestHMSContentWithDeployedDBSchema() throws InvalidParameterException, ConnectionCheckedException, RepositoryErrorException, UserNotAuthorizedException, EntityProxyOnlyException, IOException, EntityNotKnownException, ConnectorCheckedException, NoSuchFieldException, IllegalAccessException {
        mapperTestHMSContent(true);
    }

    protected void mapperTestHMSContent(boolean deployedDBSchema) throws ConnectionCheckedException, IllegalAccessException, ConnectorCheckedException, NoSuchFieldException, IOException, InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException, EntityProxyOnlyException, EntityNotKnownException {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.DATABASE_NAME, DB_NAME_VALUE);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CATALOG_NAME, CATALOG_NAME_VALUE);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.QUALIFIED_NAME_PREFIX, QUALIFIED_NAME_PREFIX);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.USE_SSL, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CACHE_INTO_CACHING_REPOSITORY, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.SEND_POLL_EVENTS, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_USER, TEST_USER);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_PASSWORD, TEST_PW);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.ENDPOINT_ADDRESS, ENDPOINT_NETWORK_ADDRESS);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.INCLUDE_DEPLOYED_SCHEMA, deployedDBSchema);
        Map<String, String> connectionSecuredPropertiesMap = new HashMap<>();
        connectionSecuredPropertiesMap.put("aaa", "aaa-value");
        connectionSecuredPropertiesMap.put("bbb", "bbb-value");
        connectionSecuredPropertiesMap.put("ccc", "ccc-value");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CONNECTION_SECURED_PROPERTIES, connectionSecuredPropertiesMap);


        OMRSDatabasePollingRepositoryEventMapper omrsDatabasePollingRepositoryEventMapper = getOmrsDatabasePollingRepositoryEventMapper(configProperties);
        MockMetaStoreClient mockMetaStoreClient = new MockMetaStoreClient();
        omrsDatabasePollingRepositoryEventMapper.setTesting();

        // test view 2 columns
        Table t = createTable(VIEW_1);
        StorageDescriptor sd = createStorageDescriptorWithColumns(TWO_COLUMNS);
        t.setSd(sd);
        t.setTableType("VIRTUAL_VIEW");
        mockMetaStoreClient.addTable(t);
        // test spark v3 3 columns
        Table t2 = createTable(SPARK_3_FORMAT_TABLE_1);
        SparkSchemaBean sparkSchemaBean2 = createSparkSchema(THREE_STRING_COLUMNS);
        ObjectMapper om = new ObjectMapper();
        String sparkSchemaBean2Str = om.writeValueAsString(sparkSchemaBean2);
        Map<String, String> parametersMap2 = new HashMap<>();
        parametersMap2.put(HMSOMRSEventProducer.SPARK_SQL_SOURCES_SCHEMA, sparkSchemaBean2Str);
        t2.setParameters(parametersMap2);
        t2.setTableType("EXTERNAL_TABLE");
        mockMetaStoreClient.addTable(t2);

        // test spark v2 3 columns
        Map<String, String> parametersMap3 = new HashMap<>();
        Table t3 = createTable(SPARK_2_FORMAT_TABLE_1);
        SparkSchemaBean sparkSchemaBean3 = createSparkSchema(THREE_STRING_COLUMNS);
        String sparkSchemaBeanStr3 = om.writeValueAsString(sparkSchemaBean3);
        int count = 0;
        int endIndex = 3;
        String checker = "";
        while (sparkSchemaBeanStr3.length() > 0) {
            // split up every 3 characters
            if (sparkSchemaBeanStr3.length() < 3) {
                endIndex = sparkSchemaBeanStr3.length();
            }

            String part = sparkSchemaBeanStr3.substring(0, endIndex);
            parametersMap3.put(HMSOMRSEventProducer.SPARK_SQL_SOURCES_SCHEMA_PART + count, part);

            sparkSchemaBeanStr3 = sparkSchemaBeanStr3.substring(endIndex);

            checker = checker + part;
            count++;
        }
        parametersMap3.put(HMSOMRSEventProducer.SPARK_SQL_SOURCES_SCHEMA_NUM_PARTS, String.valueOf(count));

        // check all the parts knit back to the original string
        assertEquals(checker, om.writeValueAsString(sparkSchemaBean3));

        t3.setParameters(parametersMap3);

        t3.setTableType("EXTERNAL_TABLE");
        mockMetaStoreClient.addTable(t3);

        omrsDatabasePollingRepositoryEventMapper.setClient(mockMetaStoreClient);
        OMRSRepositoryHelper repositoryHelper = new MockRepositoryHelper();
        OMRSMetadataCollection collection = new MockMetadataCollection(null, "test-repoName", repositoryHelper, null, "test-md-collection-id");
        List<InstanceGraph> graphs = getInstanceGraphs(omrsDatabasePollingRepositoryEventMapper, repositoryHelper, collection);
        assertNotNull(graphs);


        assert (graphs.size() == 4);
        // above the tables entities
        List<EntityDetail> entities0 = graphs.get(0).getEntities();
        if (deployedDBSchema) {
            assert (entities0.size() == 6);
        } else {
            assert (entities0.size() == 5);
        }
        String relationalDBTypeQualifiedName = DATABASE_QUALIFIED_NAME +".";
        if (deployedDBSchema) {
            relationalDBTypeQualifiedName = relationalDBTypeQualifiedName + SupportedTypes.DEFAULT_DEPLOYED_SCHEMA_TOKEN_NAME + ".";
        }
        relationalDBTypeQualifiedName = relationalDBTypeQualifiedName + SupportedTypes.DEFAULT_RELATIONAL_DB_SCHEMA_TYPE;
        // check that the entities are as expected
        int foundExpected = 0;
        for (EntityDetail entity : entities0) {
            String typeName = entity.getType().getTypeDefName();

            int propertyCount = entity.getProperties().getPropertyCount();
            if (typeName.equals(SupportedTypes.DATABASE)) {
                assertTrue(propertyCount == 2);
                checkNamesProperties(entity, DB_NAME_VALUE, DATABASE_QUALIFIED_NAME);
                assertNull(entity.getClassifications());
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.CONNECTION)) {
                assertTrue(propertyCount == 2);
                checkNamesProperties(entity, SupportedTypes.CONNECTION_VALUE, DATABASE_QUALIFIED_NAME + "." + SupportedTypes.CONNECTION_VALUE);
                assertNull(entity.getClassifications());
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.CONNECTOR_TYPE)) {
                assertTrue(propertyCount == 2);
                checkNamesProperties(entity, SupportedTypes.CONNECTOR_TYPE_VALUE, DATABASE_QUALIFIED_NAME + "." + SupportedTypes.CONNECTOR_TYPE_VALUE);
                assertNull(entity.getClassifications());
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE)) {
                assertTrue(propertyCount == 2);
                checkNamesProperties(entity, SupportedTypes.DEFAULT_RELATIONAL_DB_SCHEMA_TYPE, relationalDBTypeQualifiedName );
                assertNull(entity.getClassifications());
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.ENDPOINT)) {
                assertTrue(propertyCount == 3);
                checkNamesProperties(entity, SupportedTypes.ENDPOINT_VALUE, DATABASE_QUALIFIED_NAME + "." + SupportedTypes.ENDPOINT_VALUE);
                assertNull(entity.getClassifications());
                foundExpected++;
            } else if (deployedDBSchema && (typeName.equals(SupportedTypes.DEPLOYED_DATABASE_SCHEMA))) {
                // TODO
                foundExpected++;
            }
        }
        if (deployedDBSchema) {
            assert (foundExpected == 6);
        } else {
            assert (foundExpected == 5);
        }

        foundExpected = 0;
        List<Relationship> relationships0 = graphs.get(0).getRelationships();

        if (deployedDBSchema) {
            assert (relationships0.size() == 5);
        } else {
            assert (relationships0.size() == 4);
        }

        for (Relationship relationship : relationships0) {
            String typeName = relationship.getType().getTypeDefName();
            String ep1_guid = relationship.getEntityOneProxy().getGUID();
            String ep1_typeName = relationship.getEntityOneProxy().getType().getTypeDefName();
            String ep2_guid = relationship.getEntityTwoProxy().getGUID();
            String ep2_typeName = relationship.getEntityTwoProxy().getType().getTypeDefName();
            assertNull(relationship.getProperties());
            if (typeName.equals(SupportedTypes.CONNECTION_TO_ASSET)) {
                assertEquals(SupportedTypes.CONNECTION, ep1_typeName);
                assertEquals(SupportedTypes.DATABASE, ep2_typeName);
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.CONNECTION_CONNECTOR_TYPE)) {
                assertEquals(SupportedTypes.CONNECTION, ep1_typeName);
                assertEquals(SupportedTypes.CONNECTOR_TYPE, ep2_typeName);
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.CONNECTION_ENDPOINT)) {
                assertEquals(SupportedTypes.CONNECTION, ep1_typeName);
                assertEquals(SupportedTypes.ENDPOINT, ep2_typeName);
                foundExpected++;
            } else if (deployedDBSchema) {
                if (typeName.equals(SupportedTypes.DATA_CONTENT_FOR_DATASET)) {
                    assertEquals(SupportedTypes.DATABASE, ep1_typeName);
                    assertEquals(SupportedTypes.DEPLOYED_DATABASE_SCHEMA, ep2_typeName);
                    foundExpected++;
                } else if (typeName.equals(SupportedTypes.ASSET_SCHEMA_TYPE)) {
                    assertEquals(SupportedTypes.DEPLOYED_DATABASE_SCHEMA, ep1_typeName);
                    assertEquals(SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE, ep2_typeName);
                    foundExpected++;
                }
            }
            else if (typeName.equals(SupportedTypes.ASSET_SCHEMA_TYPE)) {
                    assertEquals(SupportedTypes.DATABASE, ep1_typeName);
                    assertEquals(SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE, ep2_typeName);
                    foundExpected++;

            }
            assertEquals(collection.getEntityDetail("user", ep1_guid).getType().getTypeDefName(), ep1_typeName);
            assertEquals(collection.getEntityDetail("user", ep2_guid).getType().getTypeDefName(), ep2_typeName);
        }
        if (deployedDBSchema) {
            assert (foundExpected == 5);
        } else {
            assert (foundExpected == 4);
        }
        foundExpected = 0;
        InstanceGraph graph1 = graphs.get(1);
        List<EntityDetail> entities1 = graph1.getEntities();
        assert (entities1.size() == 4);
        String spark2FormatTable1QualifiedName =  relationalDBTypeQualifiedName + "." + SPARK_2_FORMAT_TABLE_1;
        String spark3FormatTable1QualifiedNameAT_TABLE_1_QUALIFIED_NAME =  relationalDBTypeQualifiedName + "." + SPARK_3_FORMAT_TABLE_1;
        String view1QualifiedName = relationalDBTypeQualifiedName + "." + VIEW_1;
        for (EntityDetail entity : entities1) {
            String typeName = entity.getType().getTypeDefName();

            int propertyCount = entity.getProperties().getPropertyCount();
            if (typeName.equals(SupportedTypes.TABLE)) {
                assertTrue(propertyCount == 2);
                checkNamesProperties(entity, SPARK_2_FORMAT_TABLE_1, spark2FormatTable1QualifiedName);
                List<Classification> classifications = entity.getClassifications();
                assertEquals(classifications.size(), 1);
                checkEmbeddedDBType(classifications.get(0), SupportedTypes.RELATIONAL_TABLE_TYPE);
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.COLUMN)) {
                assertTrue(propertyCount == 2);
                checkColumnsProperties(entity, THREE_STRING_COLUMNS_NAMES, spark2FormatTable1QualifiedName);
                List<Classification> classifications = entity.getClassifications();
                assertEquals(classifications.size(), 1);
                checkEmbeddedDBType(classifications.get(0), SupportedTypes.RELATIONAL_COLUMN_TYPE, "string");
                foundExpected++;
            }
        }
        assertEquals(foundExpected, 4);
        foundExpected = 0;
        List<Relationship> relationships1 = graph1.getRelationships();

        assertEquals(relationships1.size(), 4);
        for (Relationship relationship : relationships1) {
            String typeName = relationship.getType().getTypeDefName();
            String ep1_guid = relationship.getEntityOneProxy().getGUID();
            String ep1_typeName = relationship.getEntityOneProxy().getType().getTypeDefName();
            String ep2_guid = relationship.getEntityTwoProxy().getGUID();
            String ep2_typeName = relationship.getEntityTwoProxy().getType().getTypeDefName();
            assertNull(relationship.getProperties());
            if (typeName.equals(SupportedTypes.ATTRIBUTE_FOR_SCHEMA)) {
                assertEquals(SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE, ep1_typeName);
                assertEquals(SupportedTypes.TABLE, ep2_typeName);
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.NESTED_SCHEMA_ATTRIBUTE)) {
                assertEquals(SupportedTypes.TABLE, ep1_typeName);
                assertEquals(SupportedTypes.COLUMN, ep2_typeName);
                foundExpected++;
            }
            assertEquals(collection.getEntityDetail("user", ep1_guid).getType().getTypeDefName(), ep1_typeName);
            assertEquals(collection.getEntityDetail("user", ep2_guid).getType().getTypeDefName(), ep2_typeName);
        }
        assertEquals(foundExpected, 4);
        foundExpected = 0;
        InstanceGraph graph2 = graphs.get(2);
        List<EntityDetail> entities2 = graph2.getEntities();

        assertEquals(entities2.size(), 4);
        for (EntityDetail entity : entities2) {
            String typeName = entity.getType().getTypeDefName();

            int propertyCount = entity.getProperties().getPropertyCount();
            if (typeName.equals(SupportedTypes.TABLE)) {
                assertTrue(propertyCount == 2);
                checkNamesProperties(entity, SPARK_3_FORMAT_TABLE_1, spark3FormatTable1QualifiedNameAT_TABLE_1_QUALIFIED_NAME);
                List<Classification> classifications = entity.getClassifications();
                assertEquals(classifications.size(), 1);
                checkEmbeddedDBType(classifications.get(0), SupportedTypes.RELATIONAL_TABLE_TYPE);
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.COLUMN)) {
                assertTrue(propertyCount == 2);
                checkColumnsProperties(entity, THREE_STRING_COLUMNS_NAMES, spark3FormatTable1QualifiedNameAT_TABLE_1_QUALIFIED_NAME);
                List<Classification> classifications = entity.getClassifications();
                assertEquals(classifications.size(), 1);
                checkEmbeddedDBType(classifications.get(0), SupportedTypes.RELATIONAL_COLUMN_TYPE, "string");
                foundExpected++;
            }
        }
        assertEquals(foundExpected, 4);
        foundExpected = 0;

        List<Relationship> relationships2 = graph2.getRelationships();
        assert (relationships2.size() == 4);
        for (Relationship relationship : relationships2) {
            String typeName = relationship.getType().getTypeDefName();
            String ep1_guid = relationship.getEntityOneProxy().getGUID();
            String ep1_typeName = relationship.getEntityOneProxy().getType().getTypeDefName();
            String ep2_guid = relationship.getEntityTwoProxy().getGUID();
            String ep2_typeName = relationship.getEntityTwoProxy().getType().getTypeDefName();
            assertNull(relationship.getProperties());
            if (typeName.equals(SupportedTypes.ATTRIBUTE_FOR_SCHEMA)) {
                assertEquals(SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE, ep1_typeName);
                assertEquals(SupportedTypes.TABLE, ep2_typeName);
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.NESTED_SCHEMA_ATTRIBUTE)) {
                assertEquals(SupportedTypes.TABLE, ep1_typeName);
                assertEquals(SupportedTypes.COLUMN, ep2_typeName);
                foundExpected++;
            }
            assertEquals(collection.getEntityDetail("user", ep1_guid).getType().getTypeDefName(), ep1_typeName);
            assertEquals(collection.getEntityDetail("user", ep2_guid).getType().getTypeDefName(), ep2_typeName);
        }
        assertEquals(foundExpected, 4);
        foundExpected = 0;
        InstanceGraph graph3 = graphs.get(3);
        List<EntityDetail> entities3 = graph3.getEntities();
        assert (entities3.size() == 3);
        for (EntityDetail entity : entities3) {
            String typeName = entity.getType().getTypeDefName();

            int propertyCount = entity.getProperties().getPropertyCount();
            if (typeName.equals(SupportedTypes.TABLE)) {
                assertTrue(propertyCount == 2);
                checkNamesProperties(entity, VIEW_1, view1QualifiedName);
                List<Classification> classifications = entity.getClassifications();
                assertEquals(classifications.size(), 2);
                int expectedClassificationCount = 0;
                for (Classification classification : classifications) {
                    String classificationName = classification.getName();
                    InstanceProperties properties = classification.getProperties();
                    if (classificationName.equals(SupportedTypes.TYPE_EMBEDDED_ATTRIBUTE)) {
                        expectedClassificationCount++;
                    } else if (classificationName.equals(SupportedTypes.CALCULATED_VALUE)) {
                        expectedClassificationCount++;
                    } else {
                        assertFalse(true, "Unexpected classification on view");
                    }
                }
                assertEquals(expectedClassificationCount, 2);

                foundExpected++;
            } else if (typeName.equals(SupportedTypes.COLUMN)) {
                assertTrue(propertyCount == 2);
                checkColumnsProperties(entity, TWO_COLUMNS_NAMES, view1QualifiedName);
                List<Classification> classifications = entity.getClassifications();
                assertEquals(classifications.size(), 1);
                String columnType = "string";
                if (getEntityName(entity).equals("col1")) {
                    columnType = "array<string>";
                }
                checkEmbeddedDBType(classifications.get(0), SupportedTypes.RELATIONAL_COLUMN_TYPE, columnType);
                foundExpected++;
            }
        }
        assertEquals(foundExpected, 3);
        foundExpected = 0;

        List<Relationship> relationships3 = graph3.getRelationships();
        assert (relationships3.size() == 3);
        for (Relationship relationship : relationships3) {
            String typeName = relationship.getType().getTypeDefName();
            String ep1_guid = relationship.getEntityOneProxy().getGUID();
            String ep1_typeName = relationship.getEntityOneProxy().getType().getTypeDefName();
            String ep2_guid = relationship.getEntityTwoProxy().getGUID();
            String ep2_typeName = relationship.getEntityTwoProxy().getType().getTypeDefName();
            assertNull(relationship.getProperties());
            if (typeName.equals(SupportedTypes.ATTRIBUTE_FOR_SCHEMA)) {
                assertEquals(SupportedTypes.RELATIONAL_DB_SCHEMA_TYPE, ep1_typeName);
                assertEquals(SupportedTypes.TABLE, ep2_typeName);
                foundExpected++;
            } else if (typeName.equals(SupportedTypes.NESTED_SCHEMA_ATTRIBUTE)) {
                assertEquals(SupportedTypes.TABLE, ep1_typeName);
                assertEquals(SupportedTypes.COLUMN, ep2_typeName);
                foundExpected++;
            }
            assertEquals(collection.getEntityDetail("user", ep1_guid).getType().getTypeDefName(), ep1_typeName);
            assertEquals(collection.getEntityDetail("user", ep2_guid).getType().getTypeDefName(), ep2_typeName);
        }
        assertEquals(foundExpected, 3);
        checkQualifiedNamesAreUnique(graphs);

    }

    private void checkEmbeddedDBType(Classification classification, String entityType) {
        checkEmbeddedDBType(classification, entityType, null);
    }

    private void checkEmbeddedDBType(Classification classification, String entityType, String dataType) {
        assertEquals(classification.getName(), SupportedTypes.TYPE_EMBEDDED_ATTRIBUTE);
        InstanceProperties properties = classification.getProperties();
        PrimitivePropertyValue value = (PrimitivePropertyValue) properties.getPropertyValue(SupportedTypes.SCHEMA_TYPE_NAME);
        assertEquals(value.getPrimitiveValue(), entityType);

        if (dataType != null) {
            assertEquals(properties.getPropertyCount(), 2);
            value = (PrimitivePropertyValue) properties.getPropertyValue(SupportedTypes.DATA_TYPE);
            assertEquals(value.getPrimitiveValue(), dataType);
        }
    }

    private static String getEntityName(EntityDetail entity) throws UnsupportedEncodingException {
        String name = null;
        InstanceProperties properties = entity.getProperties();

        Iterator<String> iter = properties.getPropertyNames();
        while (iter.hasNext()) {
            String propertyName = iter.next();
            String propertyValue = properties.getPropertyValue(propertyName).valueAsString();
            if (propertyName.equals("name")) {
                name = propertyValue;
            }
        }
        return name;
    }

    private static void checkNamesProperties(EntityDetail entity, String expectedName, String expectedQualifiedName) throws UnsupportedEncodingException {
        InstanceProperties properties = entity.getProperties();
        ;
        boolean isNameValid = false;
        boolean isQualifiedNameValid = false;
        String expectedGUID = Base64.getUrlEncoder().encodeToString(expectedQualifiedName.getBytes("UTF-8"));

        Iterator<String> iter = properties.getPropertyNames();
        while (iter.hasNext()) {
            String propertyName = iter.next();
            String propertyValue = properties.getPropertyValue(propertyName).valueAsString();
            if (propertyName.equals("name") && propertyValue.equals(expectedName)) {
                isNameValid = true;
            }
            if (propertyName.equals("qualifiedName") && propertyValue.equals(expectedQualifiedName)) {
                isQualifiedNameValid = true;
            }
        }
        assertTrue(isNameValid && isQualifiedNameValid);
        assertTrue(entity.getGUID().equals(expectedGUID));

    }

    private static void checkColumnsProperties(EntityDetail entity, Set<String> expectedNames, String expectedQualifiedNameRoot) throws UnsupportedEncodingException {
        InstanceProperties properties = entity.getProperties();
        Iterator<String> iter = properties.getPropertyNames();
        String nameToTest = null;
        String qualifiedNameToTest = null;
        while (iter.hasNext()) {
            String propertyName = iter.next();
            String propertyValue = properties.getPropertyValue(propertyName).valueAsString();
            if (propertyName.equals("name")) {
                assertTrue(expectedNames.contains(propertyValue));
                nameToTest = propertyValue;
            }
            if (propertyName.equals("qualifiedName")) {
                qualifiedNameToTest = propertyValue;
            }
        }
        String expectedQualifiedName = expectedQualifiedNameRoot + "." + nameToTest;
        assertEquals(expectedQualifiedName, qualifiedNameToTest);
        String expectedGUID = Base64.getUrlEncoder().encodeToString(expectedQualifiedName.getBytes("UTF-8"));
        assertTrue(entity.getGUID().equals(expectedGUID));

    }


    private SparkSchemaBean createSparkSchema(List<MockColumn> columns) {
        SparkSchemaBean sparkSchemaBean = new SparkSchemaBean();

        for (MockColumn mockColumn : columns) {
            FieldSchema fs = new FieldSchema();
            fs.setName(mockColumn.getName());
            fs.setType(mockColumn.getType());
            sparkSchemaBean.addField(fs);
        }
        return sparkSchemaBean;
    }

    private Table createTable(String name) {
        Table table = new Table();
        table.setDbName(HMSOMRSRepositoryEventMapperProvider.DATABASE_NAME);
        table.setCatName(HMSOMRSRepositoryEventMapperProvider.CATALOG_NAME);
        table.setTableName(name);
        return table;
    }

    private StorageDescriptor createStorageDescriptorWithColumns(List<MockColumn> columns) {
        StorageDescriptor sd = new StorageDescriptor();
        List<FieldSchema> cols = new ArrayList<>();

        for (MockColumn mockColumn : columns) {
            FieldSchema fs = new FieldSchema();
            fs.setName(mockColumn.getName());
            fs.setType(mockColumn.getType());
            cols.add(fs);
        }
        sd.setCols(cols);
        return sd;
    }

    private void checkQualifiedNamesAreUnique(List<InstanceGraph> graphs) {
        Set<String> qualifiedNames = new HashSet<>();
        for (InstanceGraph graph : graphs) {
            List<EntityDetail> entityDetails = graph.getEntities();
            for (EntityDetail entityDetail : entityDetails) {
                PrimitivePropertyValue value = (PrimitivePropertyValue) entityDetail.getProperties().getInstanceProperties().get("qualifiedName");
                String qualifiedName = (String) value.getPrimitiveValue();
                assertNotNull(qualifiedName);
                assertFalse((qualifiedNames.contains(qualifiedName)));
                qualifiedNames.add(qualifiedName);
            }
        }
    }

    @Test
    protected void mapperTestHMSContentIncludeDeployedDBSchema() throws ConnectionCheckedException, IllegalAccessException, ConnectorCheckedException, NoSuchFieldException, IOException {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.DATABASE_NAME, "default");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CATALOG_NAME, "spark");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.QUALIFIED_NAME_PREFIX, "data-engine::");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.USE_SSL, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CACHE_INTO_CACHING_REPOSITORY, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.SEND_POLL_EVENTS, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_USER, "testUser");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_PASSWORD, "testPW");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.INCLUDE_DEPLOYED_SCHEMA, true);
        Map<String, String> connectionSecuredPropertiesMap = new HashMap<>();
        connectionSecuredPropertiesMap.put("aaa", "aaa-value");
        connectionSecuredPropertiesMap.put("bbb", "bbb-value");
        connectionSecuredPropertiesMap.put("ccc", "ccc-value");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CONNECTION_SECURED_PROPERTIES, connectionSecuredPropertiesMap);


        OMRSDatabasePollingRepositoryEventMapper omrsDatabasePollingRepositoryEventMapper = getOmrsDatabasePollingRepositoryEventMapper(configProperties);
        MockMetaStoreClient mockMetaStoreClient = new MockMetaStoreClient();
        omrsDatabasePollingRepositoryEventMapper.setTesting();

        Table t = new Table();
        t.setCatName("aaa");
        t.setDbName("bbb");
        t.setTableName("ccc");
        StorageDescriptor sd = new StorageDescriptor();
        List<FieldSchema> cols = new ArrayList<>();
        FieldSchema fs1 = new FieldSchema();
        fs1.setName("col1");
        fs1.setType("array<string>");
        cols.add(fs1);
        FieldSchema fs2 = new FieldSchema();
        fs2.setName("col2");
        fs2.setType("string");
        cols.add(fs2);
        sd.setCols(cols);

        t.setSd(sd);

        mockMetaStoreClient.addTable(t);

        omrsDatabasePollingRepositoryEventMapper.setClient(mockMetaStoreClient);

        OMRSRepositoryHelper repositoryHelper = new MockRepositoryHelper();
        OMRSMetadataCollection collection = new MockMetadataCollection(null, "test-repoName", repositoryHelper, null, "test-md-collection-id");
        List<InstanceGraph> graphs = getInstanceGraphs(omrsDatabasePollingRepositoryEventMapper, repositoryHelper, collection);

        assertNotNull(graphs);
        assert (graphs.size() == 2);
        assert (graphs.get(0).getEntities().size() == 6);
        assert (graphs.get(0).getRelationships().size() == 5);
        assert (graphs.get(1).getEntities().size() == 3);
        assert (graphs.get(1).getRelationships().size() == 3);

    }

    private List<InstanceGraph> getInstanceGraphs(OMRSDatabasePollingRepositoryEventMapper omrsDatabasePollingRepositoryEventMapper,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  OMRSMetadataCollection collection) throws IllegalAccessException, ConnectionCheckedException, ConnectorCheckedException, NoSuchFieldException {

        CachingOMRSRepositoryProxyConnector cachingOMRSRepositoryProxyConnector = getCachingOMRSRepositoryProxyConnector(collection);
        cachingOMRSRepositoryProxyConnector.setRepositoryHelper(repositoryHelper);


        //    Set the mock repository connector into the event mapper using reflection as it is a protected field.
        Field repositoryConnector_field = omrsDatabasePollingRepositoryEventMapper.getClass().getSuperclass().getSuperclass().getDeclaredField("repositoryConnector");
        repositoryConnector_field.setAccessible(true);
        repositoryConnector_field.set(omrsDatabasePollingRepositoryEventMapper, cachingOMRSRepositoryProxyConnector);

        Field repositoryEventProcessor_field = omrsDatabasePollingRepositoryEventMapper.getClass().getSuperclass().getSuperclass().getDeclaredField("repositoryEventProcessor");
        repositoryEventProcessor_field.setAccessible(true);
        MockOMRSRepositoryEventProcessor mockOMRSRepositoryEventProcessor = new MockOMRSRepositoryEventProcessor("MockOMRSRepositoryEventProcessor");
        repositoryEventProcessor_field.set(omrsDatabasePollingRepositoryEventMapper, mockOMRSRepositoryEventProcessor);

        // run with the mock
        omrsDatabasePollingRepositoryEventMapper.start();

        Field pollingThread_field = omrsDatabasePollingRepositoryEventMapper.getClass().getDeclaredField("pollingThread");
        pollingThread_field.setAccessible(true);
        OMRSDatabasePollingRepositoryEventMapper.PollingThread thread = (OMRSDatabasePollingRepositoryEventMapper.PollingThread) pollingThread_field.get(omrsDatabasePollingRepositoryEventMapper);
        thread.run();

        List<InstanceGraph> graphs = mockOMRSRepositoryEventProcessor.getInstanceGraphList();
        return graphs;
    }

    private static OMRSDatabasePollingRepositoryEventMapper getOmrsDatabasePollingRepositoryEventMapper(Map<String, Object> configProperties) throws ConnectionCheckedException, ConnectorCheckedException {
        ConnectorBroker cb = new ConnectorBroker();

        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.ConnectorType");
        testConnType.setDisplayName("TestCT");
        testConnType.setConnectorProviderClassName(HMSOMRSRepositoryEventMapperProvider.class.getName());

        Connection testConnection = new Connection();


        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);
        testConnection.setConfigurationProperties(configProperties);

        ConnectionProperties testConnectionProperties = new ConnectionProperties(testConnection);
        //set endpoint
        Endpoint endpoint = new Endpoint();
        endpoint.setAddress("jdbc:test.url");
        testConnection.setEndpoint(endpoint);

        Connector newConnector = cb.getConnector(testConnectionProperties);
        assertNotNull(newConnector);
        OMRSDatabasePollingRepositoryEventMapper omrsDatabasePollingRepositoryEventMapper = (OMRSDatabasePollingRepositoryEventMapper) newConnector;
        return omrsDatabasePollingRepositoryEventMapper;
    }

    private CachingOMRSRepositoryProxyConnector getCachingOMRSRepositoryProxyConnector(OMRSMetadataCollection collection)
            throws IllegalAccessException, ConnectionCheckedException, ConnectorCheckedException, NoSuchFieldException {
        ConnectorBroker cb = new ConnectorBroker();

        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.ConnectorType");
        testConnType.setDisplayName("TestCT");
        testConnType.setConnectorProviderClassName(CachingOMRSRepositoryProxyConnectorProvider.class.getName());
        Connection testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectionProperties testConnectionProperties = new ConnectionProperties(testConnection);

        Connector newConnector = cb.getConnector(testConnectionProperties);
        assertNotNull(newConnector);
        CachingOMRSRepositoryProxyConnector cachingOMRSRepositoryProxyConnector = (CachingOMRSRepositoryProxyConnector) newConnector;

        Field metadataCollection_field = cachingOMRSRepositoryProxyConnector.getClass().getSuperclass().getDeclaredField("metadataCollection");
        metadataCollection_field.setAccessible(true);
        metadataCollection_field.set(cachingOMRSRepositoryProxyConnector, collection);
        // set parent collector in collection
        Field parentConnector_field = collection.getClass().getSuperclass().getSuperclass().getDeclaredField("parentConnector");
        parentConnector_field.setAccessible(true);
        parentConnector_field.set(collection, cachingOMRSRepositoryProxyConnector);


        OMRSRepositoryValidator MockRepositoryContentValidator = new MockRepositoryContentValidator();
        Field repositoryValidator_field = collection.getClass().getSuperclass().getSuperclass().getDeclaredField("repositoryValidator");
        repositoryValidator_field.setAccessible(true);
        repositoryValidator_field.set(collection, MockRepositoryContentValidator);
        Field isActive_field = cachingOMRSRepositoryProxyConnector.getClass().getSuperclass().getSuperclass().getDeclaredField("isActive");
        isActive_field.setAccessible(true);
        isActive_field.set(cachingOMRSRepositoryProxyConnector, true);

        return cachingOMRSRepositoryProxyConnector;

    }

//
//    @Test
//    protected void testAllPrimitiveConfigProperties() throws IOException {
//        HMSOMRSEventProducer hmsomrsEventProducer = new HMSOMRSEventProducer();
//        hmsomrsEventProducer.extractConfigurationProperties(null);
//        String textPath = "src/test/resources/allConfigProperties.json";
//        Path path = Paths.get(textPath);
//        String content = Files.readString(path);
//        ObjectMapper om = new ObjectMapper();
//        MapPropertyValue mapPropertyValue = om.readValue(content, MapPropertyValue.class);
//        Map<String,Object> configMap = new HashMap<>();
//        configMap.put(HMSOMRSRepositoryEventMapperProvider.CONNECTION_SECURED_PROPERTIES, mapPropertyValue);
//
//        hmsomrsEventProducer.extractConfigurationProperties(configMap);
//
//        Map<String, Object> resultantConfigurationMap = hmsomrsEventProducer.getConnectionSecuredProperties();
//
//        PrimitivePropertyValue value  = (PrimitivePropertyValue)resultantConfigurationMap.get("aaa");
//        assertTrue("aaa-value".equals((String)value.getPrimitiveValue()));
//
//        value  = (PrimitivePropertyValue)resultantConfigurationMap.get("bbb");
//        assertTrue("bbb-value".equals((String)value.getPrimitiveValue()));
//
//        value  = (PrimitivePropertyValue)resultantConfigurationMap.get("ccc");
//        assertTrue("ccc-value".equals((String)value.getPrimitiveValue()));
//    }
}
