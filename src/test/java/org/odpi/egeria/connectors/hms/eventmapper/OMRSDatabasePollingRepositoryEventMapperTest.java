/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

//import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.StorageDescriptor;
import org.apache.hadoop.hive.metastore.api.Table;
import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.adapters.repositoryservices.caching.repositoryconnector.CachingOMRSRepositoryProxyConnector;
import org.odpi.openmetadata.adapters.repositoryservices.caching.repositoryconnector.CachingOMRSRepositoryProxyConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.adapters.repositoryservices.caching.repositoryconnector.CachingOMRSRepositoryProxyConnectorProvider;
import org.odpi.openmetadata.adapters.repositoryservices.caching.repositoryconnector.CachingOMRSRepositoryProxyConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;


public class OMRSDatabasePollingRepositoryEventMapperTest
{
//
//    public static final String TEST_DB = "testDB";
//    public static final String TEST_CAT = "testCat";
//    public static final String TEST_EP = "testEP";
//    public static final String TEST_USER = "testUser";
//    public static final String TEST_PASSWORD = "testPassword";
//    public static final String TEST_QUALIFIEDNAME_PREFIX = "d.d.d";
//    public static final int TEST_CONFIG_REFRESH_INTERVAL = 5;

    @Test
    protected void mapperTestNoHMSContent() throws ConnectionCheckedException, IllegalAccessException, ConnectorCheckedException, NoSuchFieldException {
        Map<String , Object> configProperties = new HashMap<>();
        configProperties.put(  HMSOMRSRepositoryEventMapperProvider.DATABASE_NAME, "default");
        configProperties.put(  HMSOMRSRepositoryEventMapperProvider.CATALOG_NAME, "spark");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.QUALIFIED_NAME_PREFIX, "data-engine::");
        configProperties.put( HMSOMRSRepositoryEventMapperProvider.USE_SSL, true);
        configProperties.put( HMSOMRSRepositoryEventMapperProvider.CACHE_INTO_CACHING_REPOSITORY, true);
        configProperties.put( HMSOMRSRepositoryEventMapperProvider.SEND_POLL_EVENTS, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_USER, "testUser");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_PASSWORD, "testPW");
        Map<String, String> connectionSecuredPropertiesMap = new HashMap<>();
        connectionSecuredPropertiesMap.put("aaa","aaa-value");
        connectionSecuredPropertiesMap.put("bbb","bbb-value");
        connectionSecuredPropertiesMap.put("ccc","ccc-value");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CONNECTION_SECURED_PROPERTIES,connectionSecuredPropertiesMap);

        OMRSDatabasePollingRepositoryEventMapper omrsDatabasePollingRepositoryEventMapper = getOmrsDatabasePollingRepositoryEventMapper(configProperties);

        omrsDatabasePollingRepositoryEventMapper.setTesting();
        omrsDatabasePollingRepositoryEventMapper.setClient(new MockMetaStoreClient());

        List<InstanceGraph> graphs = getInstanceGraphs(omrsDatabasePollingRepositoryEventMapper);
        assertNotNull(graphs);
        assert(graphs.size() == 1);
        assert(graphs.get(0).getEntities().size() == 5);
        assert(graphs.get(0).getRelationships().size() == 4);

        checkQualifiedNamesAreUnique(graphs);

    }
    @Test
    protected void mapperTestHMSContent() throws ConnectionCheckedException, IllegalAccessException, ConnectorCheckedException, NoSuchFieldException, IOException {
        Map<String , Object> configProperties = new HashMap<>();
        configProperties.put(  HMSOMRSRepositoryEventMapperProvider.DATABASE_NAME, "default");
        configProperties.put(  HMSOMRSRepositoryEventMapperProvider.CATALOG_NAME, "spark");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.QUALIFIED_NAME_PREFIX, "data-engine::");
        configProperties.put( HMSOMRSRepositoryEventMapperProvider.USE_SSL, true);
        configProperties.put( HMSOMRSRepositoryEventMapperProvider.CACHE_INTO_CACHING_REPOSITORY, true);
        configProperties.put( HMSOMRSRepositoryEventMapperProvider.SEND_POLL_EVENTS, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_USER, "testUser");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_PASSWORD, "testPW");
        Map<String, String> connectionSecuredPropertiesMap = new HashMap<>();
        connectionSecuredPropertiesMap.put("aaa","aaa-value");
        connectionSecuredPropertiesMap.put("bbb","bbb-value");
        connectionSecuredPropertiesMap.put("ccc","ccc-value");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CONNECTION_SECURED_PROPERTIES,connectionSecuredPropertiesMap);


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
        sd.setCols(cols);
        FieldSchema fs2 = new FieldSchema();
        fs2.setName("col2");
        fs2.setType("string");
        cols.add(fs2);
        sd.setCols(cols);

        t.setSd(sd);

        mockMetaStoreClient.addTable(t);

        omrsDatabasePollingRepositoryEventMapper.setClient(mockMetaStoreClient);
        List<InstanceGraph> graphs = getInstanceGraphs(omrsDatabasePollingRepositoryEventMapper);
        assertNotNull(graphs);
        assert(graphs.size() == 2);
        assert(graphs.get(0).getEntities().size() == 5);
        assert(graphs.get(0).getRelationships().size() == 4);
        assert(graphs.get(1).getEntities().size() == 3);
        assert(graphs.get(1).getRelationships().size() == 3);

        checkQualifiedNamesAreUnique(graphs);

    }

    private void checkQualifiedNamesAreUnique(List<InstanceGraph> graphs) {
        Set<String> qualifiedNames = new HashSet<>();
        for (InstanceGraph graph: graphs) {
            List<EntityDetail> entityDetails = graph.getEntities();
            for (EntityDetail entityDetail: entityDetails) {
                PrimitivePropertyValue value = (PrimitivePropertyValue)entityDetail.getProperties().getInstanceProperties().get("qualifiedName");
                String qualifiedName = (String)value.getPrimitiveValue();
                assertNotNull(qualifiedName);
                assertFalse((qualifiedNames.contains(qualifiedName)));
                qualifiedNames.add(qualifiedName);
            }
        }
    }

    @Test
    protected void mapperTestHMSContentIncludeDeployedDBSchema() throws ConnectionCheckedException, IllegalAccessException, ConnectorCheckedException, NoSuchFieldException, IOException {
        Map<String , Object> configProperties = new HashMap<>();
        configProperties.put(  HMSOMRSRepositoryEventMapperProvider.DATABASE_NAME, "default");
        configProperties.put(  HMSOMRSRepositoryEventMapperProvider.CATALOG_NAME, "spark");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.QUALIFIED_NAME_PREFIX, "data-engine::");
        configProperties.put( HMSOMRSRepositoryEventMapperProvider.USE_SSL, true);
        configProperties.put( HMSOMRSRepositoryEventMapperProvider.CACHE_INTO_CACHING_REPOSITORY, true);
        configProperties.put( HMSOMRSRepositoryEventMapperProvider.SEND_POLL_EVENTS, true);
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_USER, "testUser");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.METADATA_STORE_PASSWORD, "testPW");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.INCLUDE_DEPLOYED_SCHEMA, true);
        Map<String, String> connectionSecuredPropertiesMap = new HashMap<>();
        connectionSecuredPropertiesMap.put("aaa","aaa-value");
        connectionSecuredPropertiesMap.put("bbb","bbb-value");
        connectionSecuredPropertiesMap.put("ccc","ccc-value");
        configProperties.put(HMSOMRSRepositoryEventMapperProvider.CONNECTION_SECURED_PROPERTIES,connectionSecuredPropertiesMap);


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
        sd.setCols(cols);
        FieldSchema fs2 = new FieldSchema();
        fs2.setName("col2");
        fs2.setType("string");
        cols.add(fs2);
        sd.setCols(cols);

        t.setSd(sd);

        mockMetaStoreClient.addTable(t);

        omrsDatabasePollingRepositoryEventMapper.setClient(mockMetaStoreClient);
        List<InstanceGraph> graphs = getInstanceGraphs(omrsDatabasePollingRepositoryEventMapper);
        assertNotNull(graphs);
        assert(graphs.size() == 2);
        assert(graphs.get(0).getEntities().size() == 6);
        assert(graphs.get(0).getRelationships().size() == 5);
        assert(graphs.get(1).getEntities().size() == 3);
        assert(graphs.get(1).getRelationships().size() == 3);

    }

    private List<InstanceGraph> getInstanceGraphs(OMRSDatabasePollingRepositoryEventMapper omrsDatabasePollingRepositoryEventMapper) throws IllegalAccessException, ConnectionCheckedException, ConnectorCheckedException, NoSuchFieldException {
        OMRSRepositoryHelper repositoryHelper = new MockRepositoryHelper();
        OMRSMetadataCollection collection = new MockMetadataCollection(null, "test-repoName",repositoryHelper,null,"test-md-collection-id");

        CachingOMRSRepositoryProxyConnector cachingOMRSRepositoryProxyConnector = getCachingOMRSRepositoryProxyConnector(collection);
        cachingOMRSRepositoryProxyConnector.setRepositoryHelper(repositoryHelper);


        //    Set the mock repository connector into the event mapper using reflection as it is a protected field.
        Field f1 = omrsDatabasePollingRepositoryEventMapper.getClass().getSuperclass().getSuperclass().getDeclaredField("repositoryConnector");
        f1.setAccessible(true);
        f1.set(omrsDatabasePollingRepositoryEventMapper, cachingOMRSRepositoryProxyConnector);

        Field f2 = omrsDatabasePollingRepositoryEventMapper.getClass().getSuperclass().getSuperclass().getDeclaredField("repositoryEventProcessor");
        f2.setAccessible(true);
        MockOMRSRepositoryEventProcessor mockOMRSRepositoryEventProcessor = new MockOMRSRepositoryEventProcessor("MockOMRSRepositoryEventProcessor");
        f2.set(omrsDatabasePollingRepositoryEventMapper, mockOMRSRepositoryEventProcessor);

        // run with the mock
        omrsDatabasePollingRepositoryEventMapper.start();

        Field f3 = omrsDatabasePollingRepositoryEventMapper.getClass().getDeclaredField("pollingThread");
        f3.setAccessible(true);
        OMRSDatabasePollingRepositoryEventMapper.PollingThread thread =  (OMRSDatabasePollingRepositoryEventMapper.PollingThread)f3.get(omrsDatabasePollingRepositoryEventMapper);
        thread.run();

        List<InstanceGraph> graphs = mockOMRSRepositoryEventProcessor.getInstanceGraphList();
        return graphs;
    }

    private static OMRSDatabasePollingRepositoryEventMapper getOmrsDatabasePollingRepositoryEventMapper(Map<String , Object> configProperties) throws ConnectionCheckedException, ConnectorCheckedException {
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
        OMRSDatabasePollingRepositoryEventMapper omrsDatabasePollingRepositoryEventMapper = (OMRSDatabasePollingRepositoryEventMapper)newConnector;
        return omrsDatabasePollingRepositoryEventMapper;
    }

    private CachingOMRSRepositoryProxyConnector getCachingOMRSRepositoryProxyConnector(OMRSMetadataCollection collection )
            throws  IllegalAccessException, ConnectionCheckedException, ConnectorCheckedException, NoSuchFieldException {
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
        CachingOMRSRepositoryProxyConnector cachingOMRSRepositoryProxyConnector = (CachingOMRSRepositoryProxyConnector)newConnector;
//        OMRSRepositoryHelper repositoryHelper = new MockRepositoryHelper();
//        cachingOMRSRepositoryProxyConnector.setRepositoryHelper(repositoryHelper);

        Field f1 = cachingOMRSRepositoryProxyConnector.getClass().getSuperclass().getDeclaredField("metadataCollection");
        f1.setAccessible(true);
        f1.set(cachingOMRSRepositoryProxyConnector, collection);
        // set parent collector in collection
        Field f2 = collection.getClass().getSuperclass().getSuperclass().getDeclaredField("parentConnector");
        f2.setAccessible(true);
        f2.set(collection, cachingOMRSRepositoryProxyConnector);


        OMRSRepositoryValidator MockRepositoryContentValidator = new MockRepositoryContentValidator();
        Field f3 = collection.getClass().getSuperclass().getSuperclass().getDeclaredField("repositoryValidator");
        f3.setAccessible(true);
        f3.set(collection, MockRepositoryContentValidator);
        Field f4 = cachingOMRSRepositoryProxyConnector.getClass().getSuperclass().getSuperclass().getDeclaredField("isActive");
        f4.setAccessible(true);
        f4.set(cachingOMRSRepositoryProxyConnector, true);


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
