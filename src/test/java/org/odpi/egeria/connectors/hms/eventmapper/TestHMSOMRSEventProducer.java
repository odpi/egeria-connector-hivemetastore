/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;



public class TestHMSOMRSEventProducer
{

    public static final String TEST_DB = "testDB";
    public static final String TEST_CAT = "testCat";
    public static final String TEST_EP = "testEP";
    public static final String TEST_USER = "testUser";
    public static final String TEST_PASSWORD = "testPassword";
    public static final String TEST_QUALIFIEDNAME_PREFIX = "d.d.d";
    public static final int TEST_CONFIG_REFRESH_INTERVAL = 5;

    @Test
    protected void testEmptyConfig() throws IOException, ConnectorCheckedException {
        assertTrue(true);
        HMSOMRSEventProducer hmsomrsEventProducer = new HMSOMRSEventProducer();
        hmsomrsEventProducer.extractConfigurationProperties(null);
        assertNull(hmsomrsEventProducer.getConnectionSecuredProperties());
        assertNull(hmsomrsEventProducer.getEndpoint());
        assertEquals("hive", hmsomrsEventProducer.getCatName());
        assertEquals(hmsomrsEventProducer.getDbName(), "default");
    }

    @Test
    protected void testConfigProperties() throws IOException, ConnectorCheckedException {
        String textPath = "src/test/resources/securedProperties.json";
        Map<String,String> mapPropertyValues = getMapPropertyValue(textPath);
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(HMSOMRSRepositoryEventMapperProvider.CONNECTION_SECURED_PROPERTIES, mapPropertyValues);
        configMap.put(HMSOMRSRepositoryEventMapperProvider.DATABASE_NAME, TEST_DB);
        configMap.put(HMSOMRSRepositoryEventMapperProvider.CATALOG_NAME, TEST_CAT);
//        configMap.put(HMSOMRSRepositoryEventMapperProvider.ENDPOINT_ADDRESS, TEST_EP);
        configMap.put(HMSOMRSRepositoryEventMapperProvider.QUALIFIED_NAME_PREFIX, TEST_QUALIFIEDNAME_PREFIX);


        HMSOMRSEventProducer hmsomrsEventProducer = new HMSOMRSEventProducer();
        hmsomrsEventProducer.extractConfigurationProperties(configMap);

        Map<String, String> resultantConfigurationMap = hmsomrsEventProducer.getConnectionSecuredProperties();
        assertTrue("aaa-value".equals(resultantConfigurationMap.get("aaa")));
        assertTrue("bbb-value".equals(resultantConfigurationMap.get("bbb")));
        assertTrue("ccc-value".equals(resultantConfigurationMap.get("ccc")));

        assertTrue(TEST_DB.equals( hmsomrsEventProducer.getDbName()));
        assertTrue(TEST_CAT.equals( hmsomrsEventProducer.getCatName()));
    }
    @SuppressWarnings("unchecked")
    private static  Map<String,String> getMapPropertyValue(String textPath) throws IOException {
        Path path = Paths.get(textPath);
        String content = Files.readString(path);
        ObjectMapper om = new ObjectMapper();
        return  om.readValue(content,  Map.class);
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
