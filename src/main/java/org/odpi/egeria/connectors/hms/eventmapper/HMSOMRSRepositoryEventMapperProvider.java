/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The HMSOMRSRepositoryEventMapperProvider is the connector provider for the HMSOMRSRepositoryEventMapper
 * It extends  OMRSDatabasePollingRepositoryEventMapperProvider which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 *
 * The HMSOMRSRepositoryEventMapperProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class HMSOMRSRepositoryEventMapperProvider extends OMRSDatabasePollingRepositoryEventMapperProvider {

    static final String CONNECTOR_TYPE_GUID = "fd923c81-4bfb-445f-a866-2ae85b2bdefa";
    static final String CONNECTOR_TYPE_NAME = "OMRS Hive Metastore Event Mapper Connector";
    static final String CONNECTOR_TYPE_DESC = "OMRS Hive Metastore Event Mapper Connector that polls for content.";

    static final String METADATA_STORE_USER = "MetadataStoreUserId";
    static final String METADATA_STORE_PASSWORD = "MetadataStorePassword";

    static final String USE_SSL = "useSSL";
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public HMSOMRSRepositoryEventMapperProvider() {
        super();
        Class<?> connectorClass = HMSOMRSRepositoryEventMapper.class;
        setConnectorClassName(connectorClass.getName());
        ConnectorType connectorType = super.getConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(CONNECTOR_TYPE_GUID);
        connectorType.setQualifiedName(CONNECTOR_TYPE_NAME);
        connectorType.setDisplayName(CONNECTOR_TYPE_NAME);
        connectorType.setDescription(CONNECTOR_TYPE_DESC);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        ArrayList<String> knownConfigProperties = (ArrayList<String>) connectorType.getRecognizedConfigurationProperties();
        knownConfigProperties.add(METADATA_STORE_USER);
        knownConfigProperties.add(METADATA_STORE_PASSWORD);
        knownConfigProperties.add(USE_SSL);

        connectorType.setRecognizedConfigurationProperties(knownConfigProperties);

        super.setConnectorTypeProperties(connectorType);
    }

}
