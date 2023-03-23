/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.apache.hadoop.hive.metastore.api.Table;

import java.util.*;

public class MockMetaStoreClient extends StubMetaStoreClientv3 {

    private Map<String, Table> tableMap = new HashMap<>();
    @Override
    public List<String> getTables(String catName, String dbName, String pattern) {
        // TODO test different catNames and dbNames
        List<String> tables = new ArrayList<>();
        Set<Map.Entry<String, Table>> entrySet = tableMap.entrySet();
        for (Map.Entry<String, Table> entry: entrySet) {
            tables.add(entry.getKey());
        }
        return tables;
    }

    @Override
    public List<String> getCatalogs() {
        List<String> catalogList = new ArrayList<>();
        catalogList.add("cat1");

        return catalogList;
    }

    @Override
    public List<String> getAllDatabases() {
        List<String> dbList = new ArrayList<>();
        dbList.add("db1");

        return dbList;
    }

    @Override
    public List<String> getAllDatabases(String catName) {
        return null;
    }
    @Override
    public Table getTable(String catName, String dbName, String tableName) {
        return tableMap.get(tableName);
    }

    public void addTable(Table table) {
        tableMap.put(table.getTableName(), table);
    }
}