/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEvent;

public class StubOMRSRepositoryEventProcessor extends OMRSRepositoryEventProcessor {
    /**
     * Constructor to update the event processor name.
     *
     * @param eventProcessorName string name
     */
    protected StubOMRSRepositoryEventProcessor(String eventProcessorName) {
        super(eventProcessorName);
    }

    @Override
    public void sendInstanceEvent(String sourceName, OMRSInstanceEvent instanceEvent) {

    }

    @Override
    public void processNewEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {

    }

    @Override
    public void processUpdatedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail oldEntity, EntityDetail newEntity) {

    }

    @Override
    public void processUndoneEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {

    }

    @Override
    public void processClassifiedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity, Classification classification) {

    }

    @Override
    public void processDeclassifiedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity, Classification originalClassification) {

    }

    @Override
    public void processReclassifiedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity, Classification originalClassification, Classification classification) {

    }

    @Override
    public void processDeletedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {

    }

    @Override
    public void processRestoredEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {

    }

    @Override
    public void processPurgedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {

    }

    @Override
    public void processPurgedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName, String instanceGUID) {

    }

    @Override
    public void processDeletePurgedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {

    }

    @Override
    public void processReTypedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, TypeDefSummary originalTypeDef, EntityDetail entity) {

    }

    @Override
    public void processReHomedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String originalHomeMetadataCollectionId, EntityDetail entity) {

    }

    @Override
    public void processReIdentifiedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String originalEntityGUID, EntityDetail entity) {

    }

    @Override
    public void processRefreshEntityRequested(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName, String instanceGUID, String homeMetadataCollectionId) {

    }

    @Override
    public void processRefreshEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {

    }

    @Override
    public void processNewRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {

    }

    @Override
    public void processUpdatedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship oldRelationship, Relationship newRelationship) {

    }

    @Override
    public void processUndoneRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {

    }

    @Override
    public void processRestoredRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {

    }

    @Override
    public void processDeletedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {

    }

    @Override
    public void processPurgedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {

    }

    @Override
    public void processPurgedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName, String instanceGUID) {

    }

    @Override
    public void processDeletePurgedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {

    }

    @Override
    public void processReTypedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, TypeDefSummary originalTypeDef, Relationship relationship) {

    }

    @Override
    public void processReHomedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String originalHomeMetadataCollectionId, Relationship relationship) {

    }

    @Override
    public void processReIdentifiedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String originalRelationshipGUID, Relationship relationship) {

    }

    @Override
    public void processRefreshRelationshipRequest(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName, String instanceGUID, String homeMetadataCollectionId) {

    }

    @Override
    public void processRefreshRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {

    }

    @Override
    public void processInstanceBatchEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, InstanceGraph instances) {

    }

    @Override
    public void processConflictingInstancesEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String targetMetadataCollectionId, TypeDefSummary targetTypeDef, String targetInstanceGUID, String otherMetadataCollectionId, InstanceProvenanceType otherOrigin, TypeDefSummary otherTypeDef, String otherInstanceGUID, String errorMessage) {

    }

    @Override
    public void processConflictingTypeEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String targetMetadataCollectionId, TypeDefSummary targetTypeDef, String targetInstanceGUID, TypeDefSummary otherTypeDef, String errorMessage) {

    }

    @Override
    public void sendTypeDefEvent(String sourceName, OMRSTypeDefEvent typeDefEvent) {

    }

    @Override
    public void processNewTypeDefEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, TypeDef typeDef) {

    }

    @Override
    public void processNewAttributeTypeDefEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, AttributeTypeDef attributeTypeDef) {

    }

    @Override
    public void processUpdatedTypeDefEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, TypeDefPatch typeDefPatch) {

    }

    @Override
    public void processDeletedTypeDefEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName) {

    }

    @Override
    public void processDeletedAttributeTypeDefEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String attributeTypeDefGUID, String attributeTypeDefName) {

    }

    @Override
    public void processReIdentifiedTypeDefEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, TypeDefSummary originalTypeDef, TypeDef typeDef) {

    }

    @Override
    public void processReIdentifiedAttributeTypeDefEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, AttributeTypeDef originalAttributeTypeDef, AttributeTypeDef attributeTypeDef) {

    }

    @Override
    public void processTypeDefConflictEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, TypeDefSummary originatorTypeDef, String otherMetadataCollectionId, TypeDefSummary conflictingTypeDef, String errorMessage) {

    }

    @Override
    public void processAttributeTypeDefConflictEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, AttributeTypeDef originatorAttributeTypeDef, String otherMetadataCollectionId, AttributeTypeDef conflictingAttributeTypeDef, String errorMessage) {

    }

    @Override
    public void processTypeDefPatchMismatchEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String targetMetadataCollectionId, TypeDefSummary targetTypeDef, TypeDef otherTypeDef, String errorMessage) {

    }
}
