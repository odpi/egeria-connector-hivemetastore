/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;

import java.util.List;

/**
 * This is a
 */
public class HMSMetaStoreClientFacade implements IMetaStoreClientFacade {
    HiveMetaStoreClient hiveMetaStoreClient =null;

    public HMSMetaStoreClientFacade(Configuration conf) throws MetaException {
        hiveMetaStoreClient = new HiveMetaStoreClient(conf, null, false);
    }


    @Override
    public List<String> getTables(String catName, String dbName, String pattern) throws TException {
        return  hiveMetaStoreClient.getTables(catName, dbName, pattern);
    }

    @Override
    public List<String> getCatalogs() throws TException {
       return hiveMetaStoreClient.getCatalogs();
    }

    @Override
    public List<String> getAllDatabases() throws TException {
        return hiveMetaStoreClient.getAllDatabases();
    }

    @Override
    public List<String> getAllDatabases(String catName) throws TException {
        return hiveMetaStoreClient.getAllDatabases(catName);
    }

    @Override
    public Table getTable(String catName, String dbName, String tableName) throws TException {
        return hiveMetaStoreClient.getTable(catName, dbName, tableName);
    }
}