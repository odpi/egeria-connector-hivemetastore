/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventProcessor;

import java.util.ArrayList;
import java.util.List;

public class MockOMRSRepositoryEventProcessor extends StubOMRSRepositoryEventProcessor {
    private List<InstanceGraph> instanceGraphList = new ArrayList<>();
    /**
     * Constructor to update the event processor name.
     *
     * @param eventProcessorName string name
     */
    protected MockOMRSRepositoryEventProcessor(String eventProcessorName) {
        super(eventProcessorName);
    }
    @Override
    public void processInstanceBatchEvent(String        sourceName,
                                   String        originatorMetadataCollectionId,
                                   String        originatorServerName,
                                   String        originatorServerType,
                                   String        originatorOrganizationName,
                                   InstanceGraph instances) {
        instanceGraphList.add(instances);

    }
    public List<InstanceGraph> getInstanceGraphList() {
        return instanceGraphList;
    }
}
