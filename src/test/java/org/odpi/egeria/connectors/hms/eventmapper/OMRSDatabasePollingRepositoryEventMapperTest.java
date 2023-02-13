/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

//import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;


public class OMRSDatabasePollingRepositoryEventMapperTest
{

    public static final String TEST_DB = "testDB";
    public static final String TEST_CAT = "testCat";
    public static final String TEST_EP = "testEP";
    public static final String TEST_USER = "testUser";
    public static final String TEST_PASSWORD = "testPassword";
    public static final String TEST_QUALIFIEDNAME_PREFIX = "d.d.d";
    public static final int TEST_CONFIG_REFRESH_INTERVAL = 5;

    @Test
    protected void mapperTest() throws ConnectionCheckedException, IllegalAccessException, ConnectorCheckedException, NoSuchFieldException {

        //OMRSRepositoryConnector parentConnector, String repositoryName, OMRSRepositoryHelper repositoryHelper, OMRSRepositoryValidator repositoryValidator, String metadataCollectionId
        OMRSRepositoryHelper repositoryHelper = new MockRepositoryHelper();
        OMRSMetadataCollection collection = new MockMetadataCollection(null, "test-repoName",repositoryHelper,null,"test-md-collection-id");

        CachingOMRSRepositoryProxyConnector cachingOMRSRepositoryProxyConnector = getCachingOMRSRepositoryProxyConnector(collection);
        OMRSDatabasePollingRepositoryEventMapper omrsDatabasePollingRepositoryEventMapper = getOmrsDatabasePollingRepositoryEventMapper();


//    Set the mock repository connector into the event mapper using reflection a it is a protected field.
        Field f1 = omrsDatabasePollingRepositoryEventMapper.getClass().getSuperclass().getSuperclass().getDeclaredField("repositoryConnector");
        f1.setAccessible(true);
        f1.set(omrsDatabasePollingRepositoryEventMapper, cachingOMRSRepositoryProxyConnector);



        MockMetaStoreClient mockclient = new MockMetaStoreClient();

        omrsDatabasePollingRepositoryEventMapper.setTesting();
        omrsDatabasePollingRepositoryEventMapper.setClient(mockclient);

        // run with the mock
        omrsDatabasePollingRepositoryEventMapper.start();



    }

    private static OMRSDatabasePollingRepositoryEventMapper getOmrsDatabasePollingRepositoryEventMapper() throws ConnectionCheckedException, ConnectorCheckedException {
        ConnectorBroker cb = new ConnectorBroker();

        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.ConnectorType");
        testConnType.setDisplayName("TestCT");
        testConnType.setConnectorProviderClassName(HMSOMRSRepositoryEventMapperProvider.class.getName());

        Connection testConnection = new Connection();
        Map<String , Object> configProperties = configProperties = new HashMap<>();
        configProperties.put(  "DatabaseName", "default");
        configProperties.put(  "CatalogName", "spark");
      //  configProperties.put(   "endpointAddress", "jdbc:test.url");
        configProperties.put("qualifiedNamePrefix", "data-engine::");
        configProperties.put( "useSSL", true);
        configProperties.put( "cacheIntoCachingRepository", true);
        configProperties.put( "sendPollEvents", true);
        configProperties.put("MetadataStoreUserId", "testUser");
        configProperties.put("MetadataStorePassword", "testPW");
        Map<String, String> connectionSecuredPropertiesMap = new HashMap<>();
        connectionSecuredPropertiesMap.put("aaa","aaa-value");
        connectionSecuredPropertiesMap.put("bbb","bbb-value");
        connectionSecuredPropertiesMap.put("ccc","ccc-value");
        configProperties.put("connectionSecuredProperties",connectionSecuredPropertiesMap);

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
