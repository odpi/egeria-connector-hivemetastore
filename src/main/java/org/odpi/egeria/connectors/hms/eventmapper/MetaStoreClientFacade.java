/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;

import java.util.*;
interface IMetaStoreClientFacade  {

    public List<String> getTables(String catName, String dbName, String pattern) throws TException;
    public List<String> getCatalogs() throws TException;
    public List<String> getAllDatabases() throws TException;
    public List<String> getAllDatabases(String catName) throws TException;
    public Table getTable(String catName, String dbName, String tableName) throws TException;


}