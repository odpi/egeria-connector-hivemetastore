/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.apache.hadoop.hive.metastore.api.Table;

import java.util.List;

public class MockMetaStoreClient extends StubMetaStoreClient {


    @Override
    public List<String> getTables(String catName, String dbName, String pattern) {
        return null;
    }

    @Override
    public List<String> getCatalogs() {
        return null;
    }

    @Override
    public List<String> getAllDatabases() {
        return null;
    }

    @Override
    public List<String> getAllDatabases(String catName) {
        return null;
    }
    @Override
    public Table getTable(String catName, String dbName, String tableName) {
        return null;
    }
}