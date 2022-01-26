package org.odpi.egeria.connectors.hive.metastore.connector;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

import java.util.ArrayList;
import java.util.List;

public class HiveMetastoreOMRSRepositoryConnectorProvider  extends OMRSRepositoryConnectorProviderBase {

    static final String CONNECTOR_TYPE_GUID = "7d200ca2-655d-4106-917d-adbbf2ec3aa4";
    static final String CONNECTOR_TYPE_NAME = "OMRS Hive Metastore Repository Connector";
    static final String CONNECTOR_TYPE_DESC = "OMRS Hive Metastore Repository Connector that processes events from the Hive Metastore.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public HiveMetastoreOMRSRepositoryConnectorProvider() {

        Class<?> connectorClass = HiveMetastoreOMRSRepositoryConnector.class;
        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(CONNECTOR_TYPE_GUID);
        connectorType.setQualifiedName(CONNECTOR_TYPE_NAME);
        connectorType.setDisplayName(CONNECTOR_TYPE_NAME);
        connectorType.setDescription(CONNECTOR_TYPE_DESC);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> knownConfigProperties = new ArrayList<>();
        connectorType.setRecognizedConfigurationProperties(knownConfigProperties);

        super.connectorTypeBean = connectorType;

    }
    
}