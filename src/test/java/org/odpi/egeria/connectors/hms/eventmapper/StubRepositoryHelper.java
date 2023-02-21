/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;

@SuppressWarnings("deprecation")
public class StubRepositoryHelper implements OMRSRepositoryHelper {


    @Override
    public TypeDefGallery getActiveTypeDefGallery() {
        return null;
    }

    @Override
    public List<TypeDef> getActiveTypeDefs() {
        return null;
    }

    @Override
    public List<AttributeTypeDef> getActiveAttributeTypeDefs() {
        return null;
    }

    @Override
    public TypeDefGallery getKnownTypeDefGallery() {
        return null;
    }

    @Override
    public List<TypeDef> getKnownTypeDefs() {
        return null;
    }

    @Override
    public List<AttributeTypeDef> getKnownAttributeTypeDefs() {
        return null;
    }

    @Override
    public TypeDef getTypeDefByName(String sourceName, String typeDefName) {
        return null;
    }

    @Override
    public InstanceProperties getUniqueProperties(String sourceName, String typeName, InstanceProperties allProperties) {
        return null;
    }

    @Override
    public String getOtherEndName(String sourceName, String anchorEntityGUID, Relationship relationship) {
        return null;
    }

    @Override
    public EntityProxy getOtherEnd(String sourceName, String anchorEntityGUID, Relationship relationship) {
        return null;
    }

    @Override
    public AttributeTypeDef getAttributeTypeDefByName(String sourceName, String attributeTypeDefName) {
        return null;
    }

    @Override
    public TypeDef getTypeDef(String sourceName, String parameterName, String typeDefGUID, String methodName) throws TypeErrorException {
        return null;
    }

    @Override
    public AttributeTypeDef getAttributeTypeDef(String sourceName, String attributeTypeDefGUID, String methodName) throws TypeErrorException {
        return null;
    }

    @Override
    public TypeDef getTypeDef(String sourceName, String guidParameterName, String nameParameterName, String typeDefGUID, String typeDefName, String methodName) throws TypeErrorException {
        return null;
    }

    @Override
    public AttributeTypeDef getAttributeTypeDef(String sourceName, String attributeTypeDefGUID, String attributeTypeDefName, String methodName) throws TypeErrorException {
        return null;
    }

    @Override
    public InstanceProperties addEnumPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, String enumTypeGUID, String enumTypeName, int ordinal, String methodName) throws TypeErrorException {
        return null;
    }

    @Override
    public boolean isTypeOf(String sourceName, String actualTypeName, String expectedTypeName) {
        return false;
    }

    @Override
    public List<String> getSubTypesOf(String sourceName, String superTypeName) {
        return null;
    }

    @Override
    public List<TypeDefAttribute> getAllPropertiesForTypeDef(String sourceName, TypeDef typeDef, String methodName) {
        return null;
    }

    @Override
    public Set<String> getAllTypeDefsForProperty(String sourceName, String propertyName, String methodName) {
        return null;
    }

    @Override
    public TypeDef applyPatch(String sourceName, TypeDef originalTypeDef, TypeDefPatch typeDefPatch) throws InvalidParameterException, PatchErrorException {
        return null;
    }

    @Override
    public void registerMetadataCollection(String metadataCollectionId, String metadataCollectionName) {

    }

    @Override
    public String getMetadataCollectionName(String metadataCollectionId) {
        return null;
    }

    @Override
    public EntityDetail getSkeletonEntity(String sourceName, String metadataCollectionId, InstanceProvenanceType provenanceType, String userName, String typeName) throws TypeErrorException {
        return null;
    }

    @Override
    public EntityDetail getSkeletonEntity(String sourceName, String metadataCollectionId, String metadataCollectionName, InstanceProvenanceType provenanceType, String userName, String typeName) throws TypeErrorException {
        return null;
    }
    @Override
    public EntitySummary getSkeletonEntitySummary(String sourceName, String metadataCollectionId, InstanceProvenanceType provenanceType, String userName, String typeName) throws TypeErrorException {
        return null;
    }

    @Override
    public EntitySummary getSkeletonEntitySummary(String sourceName, String metadataCollectionId, String metadataCollectionName, InstanceProvenanceType provenanceType, String userName, String typeName) throws TypeErrorException {
        return null;
    }

    @Override
    public Classification getSkeletonClassification(String sourceName, String userName, String classificationTypeName, String entityTypeName) throws TypeErrorException {
        return null;
    }

    @Override
    public Classification getSkeletonClassification(String sourceName, String metadataCollectionId, InstanceProvenanceType provenanceType, String userName, String classificationTypeName, String entityTypeName) throws TypeErrorException {
        return null;
    }

    @Override
    public Classification getSkeletonClassification(String sourceName, String metadataCollectionId, String metadataCollectionName, InstanceProvenanceType provenanceType, String userName, String classificationTypeName, String entityTypeName) throws TypeErrorException {
        return null;
    }

    @Override
    public Relationship getSkeletonRelationship(String sourceName, String metadataCollectionId, InstanceProvenanceType provenanceType, String userName, String typeName) throws TypeErrorException {

        Relationship relationship = new Relationship();
        String       guid         = UUID.randomUUID().toString();

        relationship.setHeaderVersion(InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION);
        relationship.setInstanceProvenanceType(provenanceType);
        relationship.setMetadataCollectionId(metadataCollectionId);
        relationship.setMetadataCollectionName("metadataCollectionName");
        relationship.setCreateTime(new Date());
        relationship.setGUID(guid);
        relationship.setVersion(1L);
InstanceType instanceType = new InstanceType(TypeDefCategory.RELATIONSHIP_DEF,
       "typeDefGUID",
       typeName,
       0L,
        "typeDefDescription",
       "typeDefDescriptionGUID",
        null,
null, //        validStatusList,
        null);
        relationship.setType(instanceType);
        relationship.setStatus(InstanceStatus.ACTIVE);
        relationship.setCreatedBy(userName);
        relationship.setInstanceURL("instance URL");

        return relationship;
    }

    @Override
    public Relationship getSkeletonRelationship(String sourceName, String metadataCollectionId, String metadataCollectionName, InstanceProvenanceType provenanceType, String userName, String typeName) throws TypeErrorException {
        return null;
    }

    @Override
    public InstanceType getNewInstanceType(String sourceName, TypeDefSummary typeDefSummary) throws TypeErrorException {
        return null;
    }

    @Override
    public EntityDetail getNewEntity(String sourceName, String metadataCollectionId, InstanceProvenanceType provenanceType, String userName, String typeName, InstanceProperties properties, List<Classification> classifications) throws TypeErrorException {
        return null;
    }

    @Override
    public EntityDetail getNewEntity(String sourceName, String metadataCollectionId, String metadataCollectionName, InstanceProvenanceType provenanceType, String userName, String typeName, InstanceProperties properties, List<Classification> classifications) throws TypeErrorException {
        return null;
    }

    @Override
    public Relationship getNewRelationship(String sourceName, String metadataCollectionId, InstanceProvenanceType provenanceType, String userName, String typeName, InstanceProperties properties) throws TypeErrorException {
        return null;
    }

    @Override
    public Relationship getNewRelationship(String sourceName, String metadataCollectionId, String metadataCollectionName, InstanceProvenanceType provenanceType, String userName, String typeName, InstanceProperties properties) throws TypeErrorException {
        return null;
    }

    @Override
    public Classification getNewClassification(String sourceName, String metadataCollectionId, InstanceProvenanceType provenanceType, String userName, String typeName, String entityTypeName, ClassificationOrigin classificationOrigin, String classificationOriginGUID, InstanceProperties properties) throws TypeErrorException {
        return null;
    }

    @Override
    public Classification getNewClassification(String sourceName, String metadataCollectionId, String metadataCollectionName, InstanceProvenanceType provenanceType, String userName, String typeName, String entityTypeName, ClassificationOrigin classificationOrigin, String classificationOriginGUID, InstanceProperties properties) throws TypeErrorException {
        return null;
    }

    @Override
    public Classification getNewClassification(String sourceName, String userName, String typeName, String entityTypeName, ClassificationOrigin classificationOrigin, String classificationOriginGUID, InstanceProperties properties) throws TypeErrorException {
        return null;
    }

    @Override
    public void checkEntityNotClassifiedEntity(String sourceName, EntitySummary entity, String classificationName, String methodName) throws ClassificationErrorException {

    }

    @Override
    public Classification checkEntityNotClassifiedEntity(String sourceName, EntitySummary entity, String classificationName, InstanceProperties classificationProperties, AuditLog auditLog, String methodName) throws ClassificationErrorException {
        return null;
    }

    @Override
    public List<Classification> addClassificationToList(String sourceName, List<Classification> classificationList, Classification newClassification, String methodName) {
        return null;
    }

    @Override
    public EntityDetail addClassificationToEntity(String sourceName, EntityDetail entity, Classification newClassification, String methodName) {
        return null;
    }

    @Override
    public EntityProxy addClassificationToEntity(String sourceName, EntityProxy entity, Classification newClassification, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties getClassificationProperties(String sourceName, List<Classification> classifications, String classificationName, String methodName) {
        return null;
    }

    @Override
    public Classification getClassificationFromEntity(String sourceName, EntitySummary entity, String classificationName, String methodName) throws ClassificationErrorException {
        return null;
    }

    @Override
    public List<Classification> getHomeClassificationsFromEntity(String sourceName, EntityDetail entity, String metadataCollectionId, String methodName) {
        return null;
    }

    @Override
    public EntityDetail updateClassificationInEntity(String sourceName, String userName, EntityDetail entity, Classification newClassification, String methodName) {
        return null;
    }

    @Override
    public EntityProxy updateClassificationInEntity(String sourceName, String userName, EntityProxy entity, Classification newClassification, String methodName) {
        return null;
    }

    @Override
    public EntityDetail deleteClassificationFromEntity(String sourceName, EntityDetail entity, String oldClassificationName, String methodName) throws ClassificationErrorException {
        return null;
    }

    @Override
    public EntityProxy deleteClassificationFromEntity(String sourceName, EntityProxy entityProxy, String oldClassificationName, String methodName) throws ClassificationErrorException {
        return null;
    }

    @Override
    public InstanceProperties mergeInstanceProperties(String sourceName, InstanceProperties existingProperties, InstanceProperties newProperties) {
        return null;
    }

    @Override
    public Relationship incrementVersion(String userId, InstanceAuditHeader originalInstance, Relationship updatedInstance) {
        return null;
    }

    @Override
    public Classification incrementVersion(String userId, InstanceAuditHeader originalInstance, Classification updatedInstance) {
        return null;
    }

    @Override
    public EntityDetail incrementVersion(String userId, InstanceAuditHeader originalInstance, EntityDetail updatedInstance) {
        return null;
    }

    @Override
    public EntityProxy getNewEntityProxy(String sourceName, EntityDetail entity) throws RepositoryErrorException {
        return null;
    }

    @Override
    public EntityProxy getNewEntityProxy(String sourceName, String metadataCollectionId, InstanceProvenanceType provenanceType, String userName, String typeName, InstanceProperties properties, List<Classification> classifications) throws TypeErrorException {
        return null;
    }

    @Override
    public boolean relatedEntity(String sourceName, String entityGUID, Relationship relationship) {
        return false;
    }

    @Override
    public String getTypeName(InstanceAuditHeader instance) throws RepositoryErrorException, InvalidParameterException {
        return null;
    }

    @Override
    public String getEnd1EntityGUID(Relationship relationship) {
        return null;
    }

    @Override
    public String getEnd2EntityGUID(Relationship relationship) {
        return null;
    }

    @Override
    public List<EntityDetail> formatEntityResults(List<EntityDetail> fullResults, int fromElement, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws PagingErrorException, PropertyErrorException {
        return null;
    }

    @Override
    public List<Relationship> formatRelationshipResults(List<Relationship> fullResults, int fromElement, String sequencingProperty, SequencingOrder sequencingOrder, int pageSize) throws PagingErrorException, PropertyErrorException {
        return null;
    }

    @Override
    public String getExactMatchRegex(String searchString) {
        return null;
    }

    @Override
    public String getExactMatchRegex(String searchString, boolean insensitive) {
        return null;
    }

    @Override
    public boolean isExactMatchRegex(String searchString) {
        return false;
    }

    @Override
    public boolean isExactMatchRegex(String searchString, boolean insensitive) {
        return false;
    }

    @Override
    public String getContainsRegex(String searchString) {
        return null;
    }

    @Override
    public String getContainsRegex(String searchString, boolean insensitive) {
        return null;
    }

    @Override
    public boolean isContainsRegex(String searchString) {
        return false;
    }

    @Override
    public boolean isContainsRegex(String searchString, boolean insensitive) {
        return false;
    }

    @Override
    public String getStartsWithRegex(String searchString) {
        return null;
    }

    @Override
    public String getStartsWithRegex(String searchString, boolean insensitive) {
        return null;
    }

    @Override
    public boolean isStartsWithRegex(String searchString) {
        return false;
    }

    @Override
    public boolean isStartsWithRegex(String searchString, boolean insensitive) {
        return false;
    }

    @Override
    public String getEndsWithRegex(String searchString) {
        return null;
    }

    @Override
    public String getEndsWithRegex(String searchString, boolean insensitive) {
        return null;
    }

    @Override
    public boolean isEndsWithRegex(String searchString) {
        return false;
    }

    @Override
    public boolean isEndsWithRegex(String searchString, boolean insensitive) {
        return false;
    }

    @Override
    public String getUnqualifiedLiteralString(String searchString) {
        return null;
    }

    @Override
    public boolean isCaseInsensitiveRegex(String searchString) {
        return false;
    }

    @Override
    public RelationshipDifferences getRelationshipDifferences(Relationship left, Relationship right, boolean ignoreModificationStamps) {
        return null;
    }

    @Override
    public EntityDetailDifferences getEntityDetailDifferences(EntityDetail left, EntityDetail right, boolean ignoreModificationStamps) {
        return null;
    }

    @Override
    public EntityProxyDifferences getEntityProxyDifferences(EntityProxy left, EntityProxy right, boolean ignoreModificationStamps) {
        return null;
    }

    @Override
    public EntitySummaryDifferences getEntitySummaryDifferences(EntitySummary left, EntitySummary right, boolean ignoreModificationStamps) {
        return null;
    }

    @Override
    public SearchClassifications getSearchClassificationsFromList(List<String> classificationNames) {
        return null;
    }

    @Override
    public String getStringProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public String removeStringProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public int getEnumPropertyOrdinal(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return 0;
    }

    @Override
    public int removeEnumPropertyOrdinal(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return 0;
    }

    @Override
    public InstanceProperties getMapProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, String> getStringMapFromProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, String> removeStringMapFromProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, Boolean> getBooleanMapFromProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, Boolean> removeBooleanMapFromProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, Long> getLongMapFromProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, Long> removeLongMapFromProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, Integer> getIntegerMapFromProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, Integer> removeIntegerMapFromProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, Object> getMapFromProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, Object> removeMapFromProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Map<String, Object> getInstancePropertiesAsMap(InstanceProperties instanceProperties) {
        return null;
    }

    @Override
    public List<String> getStringArrayProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public List<String> removeStringArrayProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public int getIntProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return 0;
    }

    @Override
    public int removeIntProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return 0;
    }

    @Override
    public Date getDateProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public Date removeDateProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return null;
    }

    @Override
    public boolean getBooleanProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return false;
    }

    @Override
    public boolean removeBooleanProperty(String sourceName, String propertyName, InstanceProperties properties, String methodName) {
        return false;
    }

    @Override
    public InstanceProperties addStringPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, String propertyValue, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addIntPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, int propertyValue, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addLongPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, long propertyValue, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addFloatPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, float propertyValue, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addDatePropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, Date propertyValue, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addBooleanPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, boolean propertyValue, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addEnumPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, int ordinal, String symbolicName, String description, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addStringArrayPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, List<String> arrayValues, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addMapPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, Map<String, Object> mapValues, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addStringMapPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, Map<String, String> mapValues, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addBooleanMapPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, Map<String, Boolean> mapValues, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addLongMapPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, Map<String, Long> mapValues, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addIntMapPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, Map<String, Integer> mapValues, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addPropertyMapToInstance(String sourceName, InstanceProperties properties, Map<String, Object> mapValues, String methodName) throws InvalidParameterException {
        return null;
    }

    @Override
    public InstanceProperties addStringPropertyMapToInstance(String sourceName, InstanceProperties properties, String propertyName, Map<String, String> mapValues, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addBooleanPropertyMapToInstance(String sourceName, InstanceProperties properties, String propertyName, Map<String, Boolean> mapValues, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addLongPropertyMapToInstance(String sourceName, InstanceProperties properties, String propertyName, Map<String, Long> mapValues, String methodName) {
        return null;
    }

    @Override
    public InstanceProperties addIntPropertyMapToInstance(String sourceName, InstanceProperties properties, String propertyName, Map<String, Integer> mapValues, String methodName) {
        return null;
    }

    @Override
    public SearchProperties getSearchPropertiesFromInstanceProperties(String sourceName, InstanceProperties properties, MatchCriteria matchCriteria) {
        return null;
    }
}