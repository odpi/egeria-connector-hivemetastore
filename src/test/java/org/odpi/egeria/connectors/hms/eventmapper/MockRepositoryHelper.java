/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.apache.hadoop.hive.metastore.api.Table;
import org.odpi.egeria.connectors.hms.helpers.SupportedTypes;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MockRepositoryHelper extends StubRepositoryHelper {

    @Override
    public TypeDef getTypeDefByName(String sourceName,
                                    String typeDefName){
        EntityDef entityDef = null;
        Set supportedTypesSet = new HashSet<>( SupportedTypes.supportedTypeNames);
        if (supportedTypesSet.contains(typeDefName)) {entityDef = new EntityDef();
            entityDef.setName(typeDefName);
            entityDef.setGUID(typeDefName+"-guid");

        }
        return entityDef;

    }
    @Override
    public Classification getSkeletonClassification(String                 sourceName,
                                                    String                 userName,
                                                    String                 classificationTypeName,
                                                    String                 entityTypeName) throws TypeErrorException
    {
        Classification classification = new Classification();
        classification.setHeaderVersion(InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION);
        classification.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
        classification.setMetadataCollectionId(null);
        classification.setMetadataCollectionName(null);
        classification.setName(classificationTypeName);
        classification.setCreateTime(new Date());
        classification.setCreatedBy(userName);
        classification.setVersion(1L);
        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefCategory(TypeDefCategory.CLASSIFICATION_DEF);
        instanceType.setTypeDefGUID("test");
        instanceType.setTypeDefName(classificationTypeName);
        classification.setType(instanceType);
        classification.setStatus(InstanceStatus.ACTIVE);
        return classification;
    }
    @Override
    public InstanceType getNewInstanceType(String         sourceName,
                                           TypeDefSummary typeDefSummary) throws TypeErrorException
    {
        InstanceType instanceType = new InstanceType();
        instanceType.setTypeDefName(typeDefSummary.getName());
        return instanceType;
    }
    @Override
    public InstanceProperties addStringPropertyToInstance(String             sourceName,
                                                          InstanceProperties properties,
                                                          String             propertyName,
                                                          String             propertyValue,
                                                          String             methodName)
    {
        InstanceProperties  resultingProperties;

        if (propertyValue != null)
        {
            if (properties == null)
            {
                resultingProperties = new InstanceProperties();
            }
            else
            {
                resultingProperties = properties;
            }


            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
            primitivePropertyValue.setPrimitiveValue(propertyValue);
            primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
            primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

            resultingProperties.setProperty(propertyName, primitivePropertyValue);

            return resultingProperties;
        }
        else
        {
            return properties;
        }
    }
}