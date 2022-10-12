/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.repository;

import org.odpi.egeria.connectors.hms.auditlog.HMSOMRSErrorCode;
import org.odpi.egeria.connectors.hms.helpers.ExceptionHelper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.List;

/**
 * This is a helper class providing methods to access the cached repository (the store). The methods here are a thin wrapper
 * around the metadata collection methods, normalising the Exceptions.
 */
public class CachedRepositoryAccessor {

    private String className = this.getClass().getName();
    private String userId =null;
    private String serverName;
    private OMRSMetadataCollection metadataCollection = null;
    public CachedRepositoryAccessor() {}
    public CachedRepositoryAccessor(String userId, String serverName,OMRSMetadataCollection metadataCollection) {
        this.userId = userId;
        this.serverName = serverName;
        this.metadataCollection = metadataCollection;
    }

    /**
     * 
     * Return the relationships for a specific entity.
     * @param  entityGUID – String unique identifier for the entity.
     * @param relationshipTypeGUID – String GUID of the type of relationship required (null for all).
     * @return list of relationships assoviated with the entity
     * @throws ConnectorCheckedException onnector Exception
     */
    public List<Relationship> getRelationshipsForEntityFromStore(
            String entityGUID,
            String relationshipTypeGUID) throws ConnectorCheckedException {
        String methodName = "getRelationshipsForEntityHelper";
        List<Relationship> relationships = null;
        try {
            relationships = metadataCollection.getRelationshipsForEntity(userId, entityGUID, relationshipTypeGUID, 0, null, null, null, null, 0);
        } catch (InvalidParameterException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.INVALID_PARAMETER_EXCEPTION, methodName, e, serverName, methodName);
        } catch (RepositoryErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.REPOSITORY_ERROR_EXCEPTION, methodName, e, serverName, methodName);
        } catch (TypeErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.TYPE_ERROR_EXCEPTION, methodName, e, serverName, methodName);
        } catch (PropertyErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.PROPERTY_ERROR_EXCEPTION, methodName, e, serverName, methodName);
        } catch (PagingErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.PAGING_ERROR_EXCEPTION, methodName, e, serverName, methodName);
        } catch (FunctionNotSupportedException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.FUNCTION_NOT_SUPPORTED_ERROR_EXCEPTION, methodName, e, serverName, methodName);
        } catch (UserNotAuthorizedException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.USER_NOT_AUTHORIZED_EXCEPTION, methodName, e, serverName, methodName);
        } catch (EntityNotKnownException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.ENTITY_NOT_KNOWN, methodName, e, serverName, methodName, entityGUID);
        }
        return relationships;
    }

    /**
     * Return the header, classifications and properties of a specific entity. This requires the full entity object to be available.
     * @param guid String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws ConnectorCheckedException connector exception
     */
    public EntityDetail getEntityDetailFromStore(String guid) throws ConnectorCheckedException {
        String methodName = "getEntityDetailFromStore";
        EntityDetail entityDetail = null;
        try {
            entityDetail = metadataCollection.getEntityDetail(userId, guid);
        } catch (InvalidParameterException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.INVALID_PARAMETER_EXCEPTION, methodName, e, serverName, methodName);
        } catch (RepositoryErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.REPOSITORY_ERROR_EXCEPTION, methodName, e, serverName, methodName);
        } catch (UserNotAuthorizedException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.USER_NOT_AUTHORIZED_EXCEPTION, methodName, e, serverName, methodName);
        } catch (EntityNotKnownException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.ENTITY_NOT_KNOWN, methodName, e, serverName, methodName, guid);
        } catch (EntityProxyOnlyException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.ENTITY_PROXY_ONLY, methodName, e, serverName, methodName, guid);
        }
        return entityDetail;


    }

    /**
      * Return the current version of a requested relationship.
      * 
      * @param guid String unique identifier for the relationship.
      * @return  relationship structure.
     * @throws ConnectorCheckedException connector Excpetion
     */
    public Relationship getRelationshipFromStore(String guid) throws ConnectorCheckedException {
        String methodName = " getRelationshipFromStore";
        Relationship relationship =null;
        try {
            relationship = metadataCollection.getRelationship(userId, guid);
        } catch (InvalidParameterException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.INVALID_PARAMETER_EXCEPTION, methodName, e, serverName, methodName);
        } catch (RepositoryErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.REPOSITORY_ERROR_EXCEPTION, methodName, e, serverName, methodName);
        } catch (UserNotAuthorizedException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.USER_NOT_AUTHORIZED_EXCEPTION, methodName, e, serverName, methodName);
        } catch (RelationshipNotKnownException e ) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.RELATIONSHIP_NOT_KNOWN, methodName, e, serverName, methodName, guid);
        }
        return relationship;


    }

    /**
     * Save the relationship as a reference copy. The id of the home metadata collection is already set up in the relationship.
     * @param relationship – relationship to save.
     * @throws ConnectorCheckedException connector Exception
     */
   public void saveRelationshipReferenceCopyToStore(Relationship relationship) throws ConnectorCheckedException {
        String methodName = "storeRelationshipReferenceCopy";
        try {
            metadataCollection.saveRelationshipReferenceCopy(
                    userId,
                    relationship);
        } catch (InvalidParameterException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.INVALID_PARAMETER_EXCEPTION, methodName, e);
        } catch (RepositoryErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.REPOSITORY_ERROR_EXCEPTION, methodName, e);
        } catch (TypeErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.TYPE_ERROR_EXCEPTION, methodName, e);
        } catch (EntityNotKnownException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.ENTITY_NOT_KNOWN, methodName, e);
        } catch (PropertyErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.PROPERTY_ERROR_EXCEPTION, methodName, e);
        } catch (HomeRelationshipException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.HOME_RELATIONSHIP_ERROR_EXCEPTION, methodName, e);
        } catch (RelationshipConflictException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.RELATIONSHIP_CONFLICT_ERROR_EXCEPTION, methodName, e);
        } catch (InvalidRelationshipException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.INVALID_RELATIONSHIP_ERROR_EXCEPTION, methodName, e);
        } catch (FunctionNotSupportedException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.FUNCTION_NOT_SUPPORTED_ERROR_EXCEPTION, methodName, e);
        } catch (UserNotAuthorizedException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.USER_NOT_AUTHORIZED_EXCEPTION, methodName, e);
        }
    }

    /**
     * Save the entity as a reference copy. The id of the home metadata collection is already set up in the entity.
     * @param entityToAdd entity to save

     * @throws ConnectorCheckedException connector exception
     */
    public void saveEntityReferenceCopyToStore(EntityDetail entityToAdd) throws ConnectorCheckedException {
        String methodName = "storeEntityReferenceCopy";

        try {
            metadataCollection.saveEntityReferenceCopy(
                    userId,
                    entityToAdd);
        } catch (InvalidParameterException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.INVALID_PARAMETER_EXCEPTION, methodName, e);
        } catch (RepositoryErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.REPOSITORY_ERROR_EXCEPTION, methodName, e);
        } catch (TypeErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.TYPE_ERROR_EXCEPTION, methodName, e);
        } catch (PropertyErrorException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.PROPERTY_ERROR_EXCEPTION, methodName, e);
        } catch (HomeEntityException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.HOME_ENTITY_ERROR_EXCEPTION, methodName, e);
        } catch (EntityConflictException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.ENTITY_CONFLICT_ERROR_EXCEPTION, methodName, e);
        } catch (InvalidEntityException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.INVALID_ENTITY_ERROR_EXCEPTION, methodName, e);
        } catch (FunctionNotSupportedException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.FUNCTION_NOT_SUPPORTED_ERROR_EXCEPTION, methodName, e);
        } catch (UserNotAuthorizedException e) {
            ExceptionHelper.raiseConnectorCheckedException(className, HMSOMRSErrorCode.USER_NOT_AUTHORIZED_EXCEPTION, methodName, e);
        }
    }
}