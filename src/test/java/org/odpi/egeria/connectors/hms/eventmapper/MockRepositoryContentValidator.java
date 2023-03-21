/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MockRepositoryContentValidator implements OMRSRepositoryValidator {


    @Override
    public void validateEnterpriseTypeDefs(String sourceName, List<TypeDef> typeDefs, String methodName) throws RepositoryErrorException {

    }

    @Override
    public void validateEnterpriseAttributeTypeDefs(String sourceName, List<AttributeTypeDef> attributeTypeDefs, String methodName) throws RepositoryErrorException {

    }

    @Override
    public boolean isActiveType(String sourceName, String typeGUID, String typeName) {
        return false;
    }

    @Override
    public boolean isActiveTypeId(String sourceName, String typeGUID) {
        return false;
    }

    @Override
    public boolean isOpenType(String sourceName, String typeGUID, String typeName) {
        return false;
    }

    @Override
    public boolean isOpenTypeId(String sourceName, String typeGUID) {
        return false;
    }

    @Override
    public boolean isKnownType(String sourceName, String typeGUID, String typeName) {
        return false;
    }

    @Override
    public boolean isKnownTypeId(String sourceName, String typeGUID) {
        return false;
    }

    @Override
    public boolean validTypeId(String sourceName, String typeGUID, String typeName) {
        return false;
    }

    @Override
    public boolean validTypeDefId(String sourceName, String typeDefGUID, String typeDefName, TypeDefCategory category) {
        return false;
    }

    @Override
    public boolean validAttributeTypeDefId(String sourceName, String attributeTypeDefGUID, String attributeTypeDefName, AttributeTypeDefCategory category) {
        return false;
    }

    @Override
    public boolean validTypeDefId(String sourceName, String typeDefGUID, String typeDefName, String typeDefVersion, TypeDefCategory category) {
        return false;
    }

    @Override
    public boolean validAttributeTypeDefId(String sourceName, String attributeTypeDefGUID, String attributeTypeDefName, String attributeTypeDefVersion, AttributeTypeDefCategory category) {
        return false;
    }

    @Override
    public boolean validTypeDef(String sourceName, TypeDef typeDef) {
        return false;
    }

    @Override
    public boolean validAttributeTypeDef(String sourceName, AttributeTypeDef attributeTypeDef) {
        return false;
    }

    @Override
    public boolean validTypeDefSummary(String sourceName, TypeDefSummary typeDefSummary) {
        return false;
    }

    @Override
    public boolean validEntity(String sourceName, EntitySummary entity) {
        return false;
    }

    @Override
    public boolean validEntity(String sourceName, EntityProxy entity) {
        return false;
    }

    @Override
    public boolean validEntity(String sourceName, EntityDetail entity) {
        return false;
    }

    @Override
    public boolean validRelationship(String sourceName, Relationship relationship) {
        return false;
    }

    @Override
    public boolean validInstanceId(String sourceName, String typeDefGUID, String typeDefName, TypeDefCategory category, String instanceGUID) {
        return false;
    }

    @Override
    public void validateUserId(String sourceName, String userId, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateTypeDefIds(String sourceName, String guidParameterName, String nameParameterName, String guid, String name, String methodName) throws InvalidParameterException {

    }

    @Override
    public TypeDef getValidTypeDefFromIds(String sourceName, String guidParameterName, String nameParameterName, String guid, String name, String methodName) throws InvalidParameterException {
        return null;
    }

    @Override
    public void validateAttributeTypeDefIds(String sourceName, String guidParameterName, String nameParameterName, String guid, String name, String methodName) throws InvalidParameterException {

    }

    @Override
    public AttributeTypeDef getValidAttributeTypeDefFromIds(String sourceName, String guidParameterName, String nameParameterName, String guid, String name, String methodName) throws InvalidParameterException {
        return null;
    }

    @Override
    public void validateTypeGUID(String sourceName, String guidParameterName, String guid, String methodName) throws InvalidParameterException, TypeErrorException {

    }

    @Override
    public void validateOptionalTypeGUID(String sourceName, String guidParameterName, String guid, String methodName) throws TypeErrorException {

    }

    @Override
    public void validateOptionalTypeGUIDs(String sourceName, String guidParameterName, String guid, String subtypeParameterName, List<String> subtypeGuids, String methodName) throws TypeErrorException {

    }

    @Override
    public TypeDef validateTypeDefPatch(String sourceName, TypeDefPatch patch, String methodName) throws InvalidParameterException, TypeDefNotKnownException, PatchErrorException {
        return null;
    }

    @Override
    public void validateInstanceTypeGUID(String sourceName, String guidParameterName, String guid, String methodName) throws TypeErrorException {

    }

    @Override
    public void validateTypeName(String sourceName, String nameParameterName, String name, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateTypeDefCategory(String sourceName, String nameParameterName, TypeDefCategory category, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateAttributeTypeDefCategory(String sourceName, String nameParameterName, AttributeTypeDefCategory category, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateTypeDef(String sourceName, String parameterName, TypeDef typeDef, String methodName) throws InvalidParameterException, InvalidTypeDefException {

    }

    @Override
    public void validateActiveType(String sourceName, String typeParameterName, TypeDefSummary typeDefSummary, TypeDefCategory category, String methodName) throws TypeErrorException, InvalidParameterException {

    }

    @Override
    public void validateKnownTypeDef(String sourceName, String parameterName, TypeDef typeDef, String methodName) throws TypeDefNotKnownException {

    }

    @Override
    public void validateUnknownTypeDef(String sourceName, String parameterName, TypeDef typeDef, String methodName) throws TypeDefKnownException, TypeDefConflictException {

    }

    @Override
    public void validateUnknownAttributeTypeDef(String sourceName, String parameterName, AttributeTypeDef attributeTypeDef, String methodName) throws TypeDefKnownException, TypeDefConflictException {

    }

    @Override
    public void validateTypeDefForInstance(String sourceName, String parameterName, TypeDef typeDef, String methodName) throws TypeErrorException, RepositoryErrorException {

    }

    @Override
    public void validateTypeForInstanceDelete(String sourceName, String typeDefGUID, String typeDefName, InstanceHeader instance, String methodName) throws InvalidParameterException, RepositoryErrorException {

    }

    @Override
    public void validateAttributeTypeDef(String sourceName, String parameterName, AttributeTypeDef attributeTypeDef, String methodName) throws InvalidParameterException, InvalidTypeDefException {

    }

    @Override
    public void validateTypeDefGallery(String sourceName, String parameterName, TypeDefGallery gallery, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateExternalId(String sourceName, String standard, String organization, String identifier, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateGUID(String sourceName, String guidParameterName, String guid, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateHomeMetadataGUID(String sourceName, String guidParameterName, String guid, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateHomeMetadataGUID(String sourceName, InstanceHeader instance, String methodName) throws RepositoryErrorException {

    }

    @Override
    public void validateHomeMetadataGUID(String sourceName, Classification classification, String methodName) throws RepositoryErrorException {

    }

    @Override
    public void validateAsOfTime(String sourceName, String parameterName, Date asOfTime, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateAsOfTimeNotNull(String sourceName, String parameterName, Date asOfTime, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateDateRange(String sourceName, String parameterName, Date fromTime, Date toTime, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validatePageSize(String sourceName, String parameterName, int pageSize, String methodName) throws PagingErrorException {

    }

    @Override
    public TypeDef validateClassificationName(String sourceName, String parameterName, String classificationName, String methodName) throws InvalidParameterException {
        return null;
    }

    @Override
    public void validateClassificationProperties(String sourceName, String classificationName, String propertiesParameterName, InstanceProperties classificationProperties, String methodName) throws PropertyErrorException {

    }

    @Override
    public void validateClassification(String sourceName, String classificationParameterName, String classificationName, String entityTypeName, String methodName) throws InvalidParameterException, ClassificationErrorException {

    }

    @Override
    public void validateClassificationList(String sourceName, String parameterName, List<Classification> classifications, String entityTypeName, String methodName) throws InvalidParameterException, ClassificationErrorException, PropertyErrorException, TypeErrorException {

    }

    @Override
    public void validateMatchCriteria(String sourceName, String parameterName, TypeDefProperties matchCriteria, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateMatchCriteria(String sourceName, String matchCriteriaParameterName, String matchPropertiesParameterName, MatchCriteria matchCriteria, InstanceProperties matchProperties, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateSearchCriteria(String sourceName, String parameterName, String searchCriteria, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateSearchProperties(String sourceName, String parameterName, SearchProperties matchProperties, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateSearchClassifications(String sourceName, String parameterName, SearchClassifications matchClassifications, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validatePropertiesForType(String sourceName, String parameterName, TypeDef typeDef, InstanceProperties properties, String methodName) throws PropertyErrorException {

    }

    @Override
    public void validatePropertiesForType(String sourceName, String parameterName, TypeDefSummary typeDefSummary, InstanceProperties properties, String methodName) throws PropertyErrorException, TypeErrorException {

    }

    @Override
    public void validateNewPropertiesForType(String sourceName, String parameterName, TypeDef typeDef, InstanceProperties properties, String methodName) throws PropertyErrorException {

    }

    @Override
    public boolean verifyInstanceType(String sourceName, String instanceTypeGUID, InstanceHeader instance) {
        return false;
    }

    @Override
    public boolean verifyInstanceType(String sourceName, String instanceTypeGUID, List<String> subtypeGUIDs, InstanceHeader instance) {
        return false;
    }

    @Override
    public void validateEntityFromStore(String sourceName, String guid, EntitySummary entity, String methodName) throws RepositoryErrorException, EntityNotKnownException {

    }

    @Override
    public void validateEntityFromStore(String sourceName, String guid, EntityDetail entity, String methodName) throws RepositoryErrorException, EntityNotKnownException {

    }

    @Override
    public void validateRelationshipFromStore(String sourceName, String guid, Relationship relationship, String methodName) throws RepositoryErrorException, RelationshipNotKnownException {

    }

    @Override
    public void validateInstanceType(String sourceName, InstanceHeader instance) throws RepositoryErrorException {

    }

    @Override
    public void validateInstanceType(String sourceName, InstanceHeader instance, String typeGUIDParameterName, String typeNameParameterName, String expectedTypeGUID, String expectedTypeName) throws RepositoryErrorException, TypeErrorException, InvalidParameterException {

    }

    @Override
    public boolean verifyInstanceHasRightStatus(List<InstanceStatus> validStatuses, InstanceHeader instance) {
        return false;
    }

    @Override
    public void validateInstanceStatus(String sourceName, String instanceStatusParameterName, InstanceStatus instanceStatus, TypeDef typeDef, String methodName) throws StatusNotSupportedException {

    }

    @Override
    public void validateNewStatus(String sourceName, String instanceStatusParameterName, InstanceStatus instanceStatus, TypeDef typeDef, String methodName) throws StatusNotSupportedException, InvalidParameterException {

    }

    @Override
    public void validateInstanceStatusForDelete(String sourceName, InstanceHeader instance, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateEntityIsNotDeleted(String sourceName, InstanceHeader instance, String methodName) throws EntityNotKnownException {

    }

    @Override
    public void validateEntityIsDeleted(String sourceName, InstanceHeader instance, String methodName) throws EntityNotDeletedException {

    }

    @Override
    public void validateEntityCanBeUpdated(String sourceName, String metadataCollectionId, InstanceHeader instance, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateEntityCanBeRehomed(String sourceName, String metadataCollectionId, InstanceHeader instance, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateRelationshipIsNotDeleted(String sourceName, InstanceHeader instance, String methodName) throws RelationshipNotKnownException {

    }

    @Override
    public void validateRelationshipIsDeleted(String sourceName, InstanceHeader instance, String methodName) throws RelationshipNotDeletedException {

    }

    @Override
    public void validateRelationshipCanBeUpdated(String sourceName, String metadataCollectionId, InstanceHeader instance, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateRelationshipCanBeRehomed(String sourceName, String metadataCollectionId, InstanceHeader instance, String methodName) throws InvalidParameterException {

    }

    @Override
    public void validateRelationshipEnds(String sourceName, EntityProxy entityOneProxy, EntityProxy entityTwoProxy, TypeDef typeDef, String methodName) throws InvalidParameterException {

    }

    @Override
    public boolean verifyEntityIsClassified(List<String> requiredClassifications, EntitySummary entity) {
        return false;
    }

    @Override
    public int countMatchingPropertyValues(InstanceProperties matchProperties, InstanceProperties instanceProperties) throws InvalidParameterException {
        return 0;
    }

    @Override
    public int countMatchingHeaderPropertyValues(InstanceProperties matchProperties, InstanceAuditHeader instanceHeader, InstanceProperties instanceProperties) throws InvalidParameterException {
        return 0;
    }

    @Override
    public boolean verifyMatchingInstancePropertyValues(InstanceProperties matchProperties, InstanceAuditHeader instanceHeader, InstanceProperties instanceProperties, MatchCriteria matchCriteria) throws InvalidParameterException {
        return false;
    }

    @Override
    public BigDecimal getNumericRepresentation(InstancePropertyValue value) {
        return null;
    }

    @Override
    public boolean verifyMatchingInstancePropertyValues(SearchProperties matchProperties, InstanceAuditHeader instanceHeader, InstanceProperties instanceProperties) throws InvalidParameterException {
        return false;
    }

    @Override
    public boolean verifyMatchingClassifications(SearchClassifications matchClassifications, EntitySummary entity) throws InvalidParameterException {
        return false;
    }

    @Override
    public void validateReferenceInstanceHeader(String sourceName, String localMetadataCollectionId, String instanceParameterName, InstanceHeader instance, String methodName) throws InvalidParameterException, RepositoryErrorException {

    }

    @Override
    public void validateReferenceInstanceHeader(String sourceName, String localMetadataCollectionId,
                                                String instanceParameterName, InstanceHeader instance,
                                                org.odpi.openmetadata.frameworks.auditlog.AuditLog auditLog,
                                                String methodName) throws InvalidParameterException, RepositoryErrorException {

    }


    @Override
    public void validateEntityProxy(String sourceName, String localMetadataCollectionId, String proxyParameterName, EntityProxy entityProxy, String methodName) throws InvalidParameterException {

    }

    @Override
    public boolean verifyInstancePropertiesMatchSearchCriteria(String sourceName, InstanceProperties properties, String searchCriteria, String methodName) throws RepositoryErrorException {
        return false;
    }

    @Override
    public boolean verifyInstancePropertiesMatchPropertyValue(String sourceName, InstanceProperties properties, String searchPropertyValue, String methodName) throws RepositoryErrorException {
        return false;
    }

    @Override
    public String getStringFromPropertyValue(InstancePropertyValue instancePropertyValue) {
        return null;
    }

    @Override
    public String getStringValuesFromInstancePropertiesAsArray(InstanceProperties instanceProperties) {
        return null;
    }

    @Override
    public String getStringValuesFromInstancePropertiesAsMap(InstanceProperties instanceProperties) {
        return null;
    }

    @Override
    public String getStringValuesFromInstancePropertiesAsStruct(InstanceProperties instanceProperties) {
        return null;
    }

    @Override
    public boolean isATypeOf(String sourceName, InstanceAuditHeader instance, String typeName, String localMethodName) {
        return false;
    }

    @Override
    public void validateAtMostOneEntityResult(List<EntityDetail> findResults, String typeName, String serviceName, String methodName) throws RepositoryErrorException {

    }

    @Override
    public void validateAtMostOneRelationshipResult(List<Relationship> findResults, String typeName, String serviceName, String methodName) throws RepositoryErrorException {

    }
}
