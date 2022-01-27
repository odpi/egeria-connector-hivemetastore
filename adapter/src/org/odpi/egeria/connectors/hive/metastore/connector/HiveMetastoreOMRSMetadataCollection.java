package org.odpi.egeria.connectors.hive.metastore.connector;

import org.odpi.egeria.connectors.hive.metastore.auditlog.HiveMetastoreOMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;

public class HiveMetastoreOMRSMetadataCollection extends OMRSMetadataCollectionBase {

    /*
    holds the list of RelationTable fields
     */
    private List<TypeDefAttribute> propertyNames = new ArrayList<>();

    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection Id.
     *
     * @param parentConnector      connector that this metadata collection supports.  The connector has the information
     *                             to call the metadata repository.
     * @param repositoryName       name of this repository.
     * @param repositoryHelper     helper class for building types and instances
     * @param repositoryValidator  validator class for checking open metadata repository objects and parameters.
     * @param metadataCollectionId unique identifier of the metadata collection Id.
     */
    public HiveMetastoreOMRSMetadataCollection(OMRSRepositoryConnector parentConnector, String repositoryName, OMRSRepositoryHelper repositoryHelper, OMRSRepositoryValidator repositoryValidator, String metadataCollectionId) {
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);

        //TODO Do we need to do the below ?
        this.parentConnector = parentConnector;
        this.repositoryName = repositoryName;
        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.metadataCollectionId = metadataCollectionId;
    }

    @Override
    public void addTypeDef(String userId, TypeDef newTypeDef) throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefKnownException, TypeDefConflictException, InvalidTypeDefException, FunctionNotSupportedException, UserNotAuthorizedException {

        String methodName = "addTypeDef";

        raiseTypeDefNotSupportedException(HiveMetastoreOMRSErrorCode.TYPEDEF_NOT_SUPPORTED,
                methodName,
                null,
                newTypeDef.getName(),
                repositoryName);
    }


    @Override
    public boolean verifyTypeDef(String userId, TypeDef typeDef) throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException, TypeDefConflictException, InvalidTypeDefException, UserNotAuthorizedException {
        final String  methodName           = "verifyTypeDef";
        final String  typeDefParameterName = "typeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, typeDef, methodName);

        boolean ret = false;

        /*
        //TODO return just basic data untill HMS connected.
         */
        if (!typeDef.getName().equals("Referenceable") ) {
            raiseTypeDefNotSupportedException(HiveMetastoreOMRSErrorCode.TYPEDEF_NOT_SUPPORTED,
                    methodName,
                    null,
                    typeDef.getName(),
                    repositoryName);
        }
        else
        {

            ret = true;
            //TODO
            /*
            fake that we support all attributes for now
             */
            propertyNames = this.repositoryHelper.getAllPropertiesForTypeDef(userId, typeDef, methodName);

            //TODO debug
            propertyNames.forEach(System.out::println);


        }

        return ret;
    }

    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid   String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException    the requested entity instance is not known in the metadata collection.
     * @throws EntityProxyOnlyException   the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail getEntityDetail(String userId, String guid) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, EntityProxyOnlyException, UserNotAuthorizedException {

        String methodName = "getEntityDetail";
        EntityDetail detail = null;
        try {

            detail = repositoryHelper.getSkeletonEntity(
                    repositoryName,
                    metadataCollectionId,
                    InstanceProvenanceType.LOCAL_COHORT,
                    userId,
                    "RelationalTable");

            detail.setMetadataCollectionName(metadataCollectionName);
            detail.setStatus(InstanceStatus.ACTIVE);
            /*
            real GUID and Qualified name is going to be
            (JDBC URL -persona) + symbol + name
             */
            detail.setGUID(guid);
            detail.setInstanceURL(guid);

            detail.setCreatedBy("HiveMetastoreConnector");
            detail.setCreateTime( new Date());


        } catch (TypeErrorException e) {
            raiseRepositoryErrorException(HiveMetastoreOMRSErrorCode.SKELETON_ERROR, methodName, e);
        }

        return detail;

    }

    /**
     * Throw a TypeDefNotSupportedException using the provided parameters.
     * @param errorCode the error code for the exception
     * @param methodName the method throwing the exception
     * @param cause the underlying cause of the exception (if any, otherwise null)
     * @param params any parameters for formatting the error message
     * @throws TypeDefNotSupportedException always
     */
    private void raiseRepositoryErrorException(HiveMetastoreOMRSErrorCode errorCode, String methodName, TypeErrorException cause, String ...params) throws RepositoryErrorException
    {
        if (cause == null) {
            throw new RepositoryErrorException(errorCode.getMessageDefinition(params),
                    this.getClass().getName(),
                    methodName);
        } else {
            throw new RepositoryErrorException(errorCode.getMessageDefinition(params),
                    this.getClass().getName(),
                    methodName,
                    cause);
        }
    }

    /**
     * Throw a TypeDefNotSupportedException using the provided parameters.
     * @param errorCode the error code for the exception
     * @param methodName the method throwing the exception
     * @param cause the underlying cause of the exception (if any, otherwise null)
     * @param params any parameters for formatting the error message
     * @throws TypeDefNotSupportedException always
     */
    private void raiseTypeDefNotSupportedException(HiveMetastoreOMRSErrorCode errorCode, String methodName, TypeDefNotSupportedException cause, String ...params) throws TypeDefNotSupportedException {
        if (cause == null) {
            throw new TypeDefNotSupportedException(errorCode.getMessageDefinition(params),
                    this.getClass().getName(),
                    methodName);
        } else {
            throw new TypeDefNotSupportedException(errorCode.getMessageDefinition(params),
                    this.getClass().getName(),
                    methodName,
                    cause);
        }
    }




}
