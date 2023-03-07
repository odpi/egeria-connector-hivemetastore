/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.apache.hadoop.hive.metastore.api.FieldSchema;

import java.util.ArrayList;
import java.util.List;

public class SparkSchemaBean {

    List<FieldSchema> fields = new ArrayList<>();

    public List<FieldSchema> getFields() {
        return fields;
    }

    public void setFields(List<FieldSchema> fields) {
        this.fields = fields;
    }

    public void addField(FieldSchema fieldSchema) {
        fields.add(fieldSchema);
    }
}
