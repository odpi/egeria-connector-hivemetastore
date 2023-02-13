/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSMetadataCollectionManager;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
@SuppressWarnings("deprecation")
public class StubMetadataCollectionManager implements OMRSMetadataCollectionManager {

    @Override
    public void setRepositoryHelper(OMRSRepositoryHelper repositoryHelper) {

    }

    @Override
    public void setRepositoryValidator(OMRSRepositoryValidator repositoryValidator) {

    }

    @Override
    public String getRepositoryName() {
        return null;
    }

    @Override
    public void setRepositoryName(String repositoryName) {

    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public void setServerName(String serverName) {

    }

    @Override
    public String getServerType() {
        return null;
    }

    @Override
    public void setServerType(String serverType) {

    }

    @Override
    public String getOrganizationName() {
        return null;
    }

    @Override
    public void setOrganizationName(String organizationName) {

    }

    @Override
    public String getServerUserId() {
        return null;
    }

    @Override
    public void setServerUserId(String serverUserId) {

    }

    @Override
    public int getMaxPageSize() {
        return 0;
    }

    @Override
    public void setMaxPageSize(int maxPageSize) {

    }

    @Override
    public String getMetadataCollectionId() {
        return null;
    }

    @Override
    public void setMetadataCollectionId(String metadataCollectionId) {

    }

    @Override
    public OMRSMetadataCollection getMetadataCollection() throws RepositoryErrorException {
        return null;
    }
}