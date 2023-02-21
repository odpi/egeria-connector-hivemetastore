/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;

public class MockMetadataCollection extends OMRSMetadataCollectionBase {
    private Map<String, EntityDetail>  guidEntityMap = new HashMap<>();
    private Map<String, Relationship>  guidRelationshipMap = new HashMap<>();

    private Map<String, List<Relationship>>  entityGuidRelationshipsMap = new HashMap<>();


    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection id.
     *
     * @param parentConnector      connector that this metadata collection supports.  The connector has the information
     *                             to call the metadata repository.
     * @param repositoryName       name of this repository.
     * @param repositoryHelper     helper class for building types and instances
     * @param repositoryValidator  validator class for checking open metadata repository objects and parameters.
     * @param metadataCollectionId unique identifier of the metadata collection Id.
     */
    public MockMetadataCollection(OMRSRepositoryConnector parentConnector, String repositoryName, OMRSRepositoryHelper repositoryHelper, OMRSRepositoryValidator repositoryValidator, String metadataCollectionId) {
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);
    }
    @Override
    public void saveEntityReferenceCopy(String         userId,
                                        EntityDetail entity)  {
        guidEntityMap.put(entity.getGUID(), entity);
    }
    @Override
    public void saveRelationshipReferenceCopy(String         userId,
                                              Relationship   relationship)  {
        guidRelationshipMap.put(relationship.getGUID(), relationship);

        String end1Guid = relationship.getEntityOneProxy().getGUID();
        String end2Guid = relationship.getEntityTwoProxy().getGUID();

        List<Relationship> end1Relationships =entityGuidRelationshipsMap.get(end1Guid);
        if (end1Relationships == null) {
            end1Relationships = new ArrayList<>();

        }
        end1Relationships.add(relationship);
        entityGuidRelationshipsMap.put(end1Guid,end1Relationships);

        List<Relationship> end2Relationships =entityGuidRelationshipsMap.get(end2Guid);
        if (end2Relationships == null) {
            end2Relationships = new ArrayList<>();

        }
        end2Relationships.add(relationship);
        entityGuidRelationshipsMap.put(end2Guid,end2Relationships);


    }
    @Override
    public List<Relationship> getRelationshipsForEntity(String                     userId,
                                                        String                     entityGUID,
                                                        String                     relationshipTypeGUID,
                                                        int                        fromRelationshipElement,
                                                        List<InstanceStatus>       limitResultsByStatus,
                                                        Date asOfTime,
                                                        String                     sequencingProperty,
                                                        SequencingOrder sequencingOrder,
                                                        int                        pageSize) {
        return entityGuidRelationshipsMap.get(entityGUID);
    }
    public EntityDetail getEntityDetail(String  userId,
                                        String     guid) {
        return guidEntityMap.get(guid);
    }
    public Relationship getRelationship(String    userId,
                                        String    guid) {
        return guidRelationshipMap.get(guid);
    }
}