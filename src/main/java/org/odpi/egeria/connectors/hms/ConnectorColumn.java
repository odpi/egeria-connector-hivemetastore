/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms;

import java.util.Objects;
/**
 * This is a representation of a Column for the connector without any reference to the technology column representation.
 */
public class ConnectorColumn {
    String name;
    String qualifiedName;
    String type;

    /**
     * connector column default constructor
     */
    public ConnectorColumn() {}

    /**
     * constructor
     * @param name name of the column
     * @param qualifiedName qualifiedName to use for the RelationalColumnEntity
     * @param type type of the column e.g. int
     */
    public ConnectorColumn(String name, String qualifiedName, String type) {
        this.name= name;
        this.qualifiedName=qualifiedName;
        this.type=type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectorColumn that = (ConnectorColumn) o;
        return Objects.equals(name, that.name) && Objects.equals(qualifiedName, that.qualifiedName) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, qualifiedName, type);
    }


}
