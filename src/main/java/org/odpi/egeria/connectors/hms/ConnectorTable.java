/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * This is a representation of a Table for the connector without any reference to the technology table representation.
 */
public class ConnectorTable {
    String name;
    String qualifiedName;
    String type;
    Date createTime;
    String formula;

    List<ConnectorColumn> columns;

    /**
     * ConnectorTable describes a table without any dependency on any technical implementation.
     * The technical implementations (e.g. hms tables) can be mapped to this class and the subsequent processing not have any implementation
     * dependencies.
     *
     */
    public ConnectorTable() {}

    /**
     * ConnectorTable constructor
     * @param name name of Table
     * @param qualifiedName qualified nme of table
     * @param type table type
     * @param createTime create time as a Date
     * @param formula formula - can be set for a view
     */
    public ConnectorTable(String name, String qualifiedName, String type, Date createTime, String formula ) {
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.type = type;
        this.createTime = createTime;
        this.formula = formula;

    }

    /**
     * get name
     * @return table name
     */
    public String getName() {
        return name;
    }

    /**
     * Set table name
     * @param name name of the table
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get the qualified name of the table
     * @return qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * set the qualified name of the table
     * @param qualifiedName set qualified name
     */
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    /**
     * get the type of the table. The content is expected to be a HMS table type values
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the table type. The type is expected to be a HMS table type value
     * @param type table type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get create time as a date - as Egeria Entities expect dates
     * @return  create time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * set create time as a date - as Egeria Entities expect dates
     * @param createTime date to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * get formula for view
     * @return text
     */
    public String getFormula() {
        return formula;
    }

    /**
     * set formula for view
     * @param formula text
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * get columns
     * @return columns
     */
    public List<ConnectorColumn> getColumns() {
        return columns;
    }
        /**
         *  set columns
         * @param columns columns
         */
    public void setColumns(List<ConnectorColumn> columns) {
        this.columns = columns;
    }

    /**
     * add column
     * @param column column to add
     */
    public void addColumn(ConnectorColumn column) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectorTable that = (ConnectorTable) o;
        return Objects.equals(name, that.name) && Objects.equals(qualifiedName, that.qualifiedName) && Objects.equals(type, that.type) && Objects.equals(createTime, that.createTime) && Objects.equals(formula, that.formula) && Objects.equals(columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, qualifiedName, type, createTime, formula, columns);
    }

    @Override
    public String toString() {
        return "ConnectorTable{" +
                "name='" + name + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", type='" + type + '\'' +
                ", createTime=" + createTime +
                ", formula='" + formula + '\'' +
                ", columns=" + columns +
                '}';
    }
}
