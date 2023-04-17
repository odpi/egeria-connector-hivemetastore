/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;

import java.util.*;

/**
 * This is an interface that exposes the HMS Client methods that this connector uses.
 * This facade allows us to reflectively load different HMs client implementations, without effecting the
 * caller of the facade.
 */
interface IMetaStoreClientFacade  {
    /**
     * get the tables
     * @param catName catalog name
     * @param dbName data name
     * @param pattern pattern
     * @return list of table names
     * @throws TException thrift error
     */
    public List<String> getTables(String catName, String dbName, String pattern) throws TException;
    public List<String> getCatalogs() throws TException;
    public List<String> getAllDatabases() throws TException;
    public List<String> getAllDatabases(String catName) throws TException;
    public Table getTable(String catName, String dbName, String tableName) throws TException;


}