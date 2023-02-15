/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

public class MockMetadataCollectionManager extends StubMetadataCollectionManager {
    public void setMetadataCollection(OMRSMetadataCollection omrsMetadataCollection) throws RepositoryErrorException {
        // use reflection to set
    }


}