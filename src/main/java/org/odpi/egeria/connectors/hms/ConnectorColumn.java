/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms;

import java.util.Date;
import java.util.Objects;
/**
 * This is a representation of a Column for the connector without any reference to the technology column representation.
 */
public class ConnectorColumn {
    String name;
    String qualifiedName;
    String type;

    Date createTime;

    /**
     * connector column default constructor
     */
    public ConnectorColumn() {}

    /**
     * constructor
     * @param name name of the column
     * @param qualifiedName qualifiedName to use for the RelationalColumnEntity
     * @param type type of the column e.g. int
     * @param createTime create time for this column
     */
    public ConnectorColumn(String name, String qualifiedName, String type, Date createTime) {
        this.name= name;
        this.qualifiedName=qualifiedName;
        this.type=type;
        this.createTime = createTime;
    }

    /**
     * get name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * set name
     * @param name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get qualified name
     * @return qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * set qualified name
     * @param qualifiedName qualified name to set
     */
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    /**
     * get type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * set type
     * @param type type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get the create time
     * @return create time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Set the create time
     * @param createTime time to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectorColumn that = (ConnectorColumn) o;
        return Objects.equals(name, that.name) && Objects.equals(qualifiedName, that.qualifiedName) && Objects.equals(type, that.type) && Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, qualifiedName, type, createTime);
    }


}
