/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The ApacheAtlasOMRSRepositoryEventMapperProvider is the connector provider for the ApacheAtlasOMRSRepositoryEventMapperProvider.
 * It extends OMRSRepositoryEventMapperProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 *
 * The ApacheAtlasOMRSRepositoryEventMapperProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class HMSOMRSRepositoryEventMapperProvider extends OMRSRepositoryConnectorProviderBase {

    static final String CONNECTOR_TYPE_GUID = "fd923c81-4bfb-445f-a866-2ae85b2bdefa";
    static final String CONNECTOR_TYPE_NAME = "OMRS Hive Metastore Event Mapper Connector";
    static final String CONNECTOR_TYPE_DESC = "OMRS Hive Metastore Event Mapper Connector that polls for content.";
    static final String QUALIFIED_NAME_PREFIX = "qualifiedNamePrefix";

    static final String REFRESH_TIME_INTERVAL = "refreshTimeInterval";

    static final String CATALOG_NAME = "CatalogName";
    static final String DATABASE_NAME = "DatabaseName";
    static final String METADATA_STORE_USER = "MetadataStoreUserId";
    static final String METADATA_STORE_PASSWORD = "MetadataStorePassword";

    static final String THRIFT_URL = "ThriftURL";

    static final String SEND_POLL_EVENTS = "sendPollEvents";


//    private String metadata_store_userId = "crn:v1:bluemix:public:sql-query:eu-de:a/f5e2ac71094077500e0d4b1ef8b9de0a:598f929e-2446-4f1d-9283-7b906465bd3e::";
//    private String metadata_store_password = "UgfMaCLGaKeRy3C9ctEUk0EieDs5ibILrLvGUaDlJoJP";

    private String thriftURL= "thrift://catalog.eu-de.dataengine.cloud.ibm.com:9083";



    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public HMSOMRSRepositoryEventMapperProvider() {
        Class<?> connectorClass = HMSOMRSRepositoryEventMapper.class;
        super.setConnectorClassName(connectorClass.getName());
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(CONNECTOR_TYPE_GUID);
        connectorType.setQualifiedName(CONNECTOR_TYPE_NAME);
        connectorType.setDisplayName(CONNECTOR_TYPE_NAME);
        connectorType.setDescription(CONNECTOR_TYPE_DESC);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> knownConfigProperties = new ArrayList<>();
        knownConfigProperties.add(QUALIFIED_NAME_PREFIX);
        knownConfigProperties.add(REFRESH_TIME_INTERVAL);
        connectorType.setRecognizedConfigurationProperties(knownConfigProperties);

        super.setConnectorTypeProperties(connectorType);
    }

}
