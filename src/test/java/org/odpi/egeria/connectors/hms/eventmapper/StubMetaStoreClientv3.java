/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.common.ValidTxnList;
import org.apache.hadoop.hive.common.ValidWriteIdList;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.PartitionDropOptions;
import org.apache.hadoop.hive.metastore.TableType;
import org.apache.hadoop.hive.metastore.api.*;
import org.apache.hadoop.hive.metastore.partition.spec.PartitionSpecProxy;
import org.apache.hadoop.hive.metastore.utils.ObjectPair;
import org.apache.thrift.TException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Stub client for the IMetaStoreClient. This is a minimal placeholder implementation of the interface. This should be extended to so the child
 * class only contains the changes it is interested in and not all of these stubs.
 *
 * Some methods have been added so that the code can work with a 3.1.2 HMS interface.
 * Deprecation warning as suppressed.
 */
public class StubMetaStoreClientv3 implements IMetaStoreClient {

    // needed to work with 3.1.2 client
    public void alter_partitions(String catName, String dbName, List<Partition> newParts, EnvironmentContext environmentContext, String a) throws InvalidOperationException, MetaException, TException {

    }
    // needed to work with 3.1.2 client
    public void alter_partitions(String catName, String dbName, String tblName, List<Partition> newParts, EnvironmentContext environmentContext, String a) throws InvalidOperationException, MetaException, TException {

    }
    // needed to work with 3.1.2 client
    public void alter_partitions(String catName, String dbName, String tblName,Table t, EnvironmentContext environmentContext, String a) throws InvalidOperationException, MetaException, TException {

    }
    // needed to work with 3.1.2 client
    public void alter_partitions(String catName, String dbName, List<Partition> newParts, EnvironmentContext environmentContext, String a, long b) throws InvalidOperationException, MetaException, TException {

    }
    // needed to work with 3.1.2 client
    public void alter_table(String a ,String b,String c,Table t,EnvironmentContext e,String f) {

    }



    @Override
    public boolean isCompatibleWith(Configuration conf) {
        return false;
    }

    @Override
    public void setHiveAddedJars(String addedJars) {

    }

    @Override
    public boolean isLocalMetaStore() {
        return false;
    }

    @Override
    public void reconnect() throws MetaException {

    }

    @Override
    public void close() {

    }

    @Override
    public void setMetaConf(String key, String value) throws MetaException, TException {

    }

    @Override
    public String getMetaConf(String key) throws MetaException, TException {
        return null;
    }

    @Override
    public void createCatalog(Catalog catalog) throws AlreadyExistsException, InvalidObjectException, MetaException, TException {

    }

    @Override
    public void alterCatalog(String catalogName, Catalog newCatalog) throws NoSuchObjectException, InvalidObjectException, MetaException, TException {

    }

    @Override
    public Catalog getCatalog(String catName) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<String> getCatalogs() throws MetaException, TException {
        return null;
    }

    @Override
    public void dropCatalog(String catName) throws NoSuchObjectException, InvalidOperationException, MetaException, TException {

    }

    @Override
    public List<String> getDatabases(String databasePattern) throws MetaException, TException {
        return null;
    }

    @Override
    public List<String> getDatabases(String catName, String databasePattern) throws MetaException, TException {
        return null;
    }

    @Override
    public List<String> getAllDatabases() throws MetaException, TException {
        return null;
    }

    @Override
    public List<String> getAllDatabases(String catName) throws MetaException, TException {
        return null;
    }

    @Override
    public List<String> getTables(String dbName, String tablePattern) throws MetaException, TException, UnknownDBException {
        return null;
    }

    @Override
    public List<String> getTables(String catName, String dbName, String tablePattern) throws MetaException, TException, UnknownDBException {
        return null;
    }

    @Override
    public List<String> getTables(String dbName, String tablePattern, TableType tableType) throws MetaException, TException, UnknownDBException {
        return null;
    }

    @Override
    public List<String> getTables(String catName, String dbName, String tablePattern, TableType tableType) throws MetaException, TException, UnknownDBException {
        return null;
    }

    @Override
    public List<String> getMaterializedViewsForRewriting(String dbName) throws MetaException, TException, UnknownDBException {
        return null;
    }

    @Override
    public List<String> getMaterializedViewsForRewriting(String catName, String dbName) throws MetaException, TException, UnknownDBException {
        return null;
    }

    @Override
    public List<TableMeta> getTableMeta(String dbPatterns, String tablePatterns, List<String> tableTypes) throws MetaException, TException, UnknownDBException {
        return null;
    }

    @Override
    public List<TableMeta> getTableMeta(String catName, String dbPatterns, String tablePatterns, List<String> tableTypes) throws MetaException, TException, UnknownDBException {
        return null;
    }

    @Override
    public List<String> getAllTables(String dbName) throws MetaException, TException, UnknownDBException {
        return null;
    }

    @Override
    public List<String> getAllTables(String catName, String dbName) throws MetaException, TException, UnknownDBException {
        return null;
    }

    @Override
    public List<String> listTableNamesByFilter(String dbName, String filter, short maxTables) throws TException, InvalidOperationException, UnknownDBException {
        return null;
    }

    @Override
    public List<String> listTableNamesByFilter(String catName, String dbName, String filter, int maxTables) throws TException, InvalidOperationException, UnknownDBException {
        return null;
    }

    @Override
    public void dropTable(String dbname, String tableName, boolean deleteData, boolean ignoreUnknownTab) throws MetaException, TException, NoSuchObjectException {

    }

    @Override
    public void dropTable(String dbname, String tableName, boolean deleteData, boolean ignoreUnknownTab, boolean ifPurge) throws MetaException, TException, NoSuchObjectException {

    }

    @Override
    public void dropTable(String dbname, String tableName) throws MetaException, TException, NoSuchObjectException {

    }

    @Override
    public void dropTable(String catName, String dbName, String tableName, boolean deleteData, boolean ignoreUnknownTable, boolean ifPurge) throws MetaException, NoSuchObjectException, TException {

    }

    @Override
    public void truncateTable(String dbName, String tableName, List<String> partNames) throws MetaException, TException {

    }

    @Override
    public void truncateTable(String catName, String dbName, String tableName, List<String> partNames) throws MetaException, TException {

    }

    @Override
    public CmRecycleResponse recycleDirToCmPath(CmRecycleRequest request) throws MetaException, TException {
        return null;
    }

    @Override
    public boolean tableExists(String databaseName, String tableName) throws MetaException, TException, UnknownDBException {
        return false;
    }

    @Override
    public boolean tableExists(String catName, String dbName, String tableName) throws MetaException, TException, UnknownDBException {
        return false;
    }

    @Override
    public Database getDatabase(String databaseName) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public Database getDatabase(String catalogName, String databaseName) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public Table getTable(String dbName, String tableName) throws MetaException, TException, NoSuchObjectException {
        return null;
    }

    @Override
    public Table getTable(String catName, String dbName, String tableName) throws MetaException, TException {
        return null;
    }

    @Override
    public List<Table> getTableObjectsByName(String dbName, List<String> tableNames) throws MetaException, InvalidOperationException, UnknownDBException, TException {
        return null;
    }

    @Override
    public List<Table> getTableObjectsByName(String catName, String dbName, List<String> tableNames) throws MetaException, InvalidOperationException, UnknownDBException, TException {
        return null;
    }

    @Override
    public Materialization getMaterializationInvalidationInfo(CreationMetadata cm, String validTxnList) throws MetaException, InvalidOperationException, UnknownDBException, TException {
        return null;
    }

    @Override
    public void updateCreationMetadata(String dbName, String tableName, CreationMetadata cm) throws MetaException, TException {

    }

    @Override
    public void updateCreationMetadata(String catName, String dbName, String tableName, CreationMetadata cm) throws MetaException, TException {

    }

    @Override
    public Partition appendPartition(String dbName, String tableName, List<String> partVals) throws InvalidObjectException, AlreadyExistsException, MetaException, TException {
        return null;
    }

    @Override
    public Partition appendPartition(String catName, String dbName, String tableName, List<String> partVals) throws InvalidObjectException, AlreadyExistsException, MetaException, TException {
        return null;
    }

    @Override
    public Partition appendPartition(String dbName, String tableName, String name) throws InvalidObjectException, AlreadyExistsException, MetaException, TException {
        return null;
    }

    @Override
    public Partition appendPartition(String catName, String dbName, String tableName, String name) throws InvalidObjectException, AlreadyExistsException, MetaException, TException {
        return null;
    }

    @Override
    public Partition add_partition(Partition partition) throws InvalidObjectException, AlreadyExistsException, MetaException, TException {
        return null;
    }

    @Override
    public int add_partitions(List<Partition> partitions) throws InvalidObjectException, AlreadyExistsException, MetaException, TException {
        return 0;
    }

    @Override
    public int add_partitions_pspec(PartitionSpecProxy partitionSpec) throws InvalidObjectException, AlreadyExistsException, MetaException, TException {
        return 0;
    }

    @Override
    public List<Partition> add_partitions(List<Partition> partitions, boolean ifNotExists, boolean needResults) throws InvalidObjectException, AlreadyExistsException, MetaException, TException {
        return null;
    }

    @Override
    public Partition getPartition(String dbName, String tblName, List<String> partVals) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public Partition getPartition(String catName, String dbName, String tblName, List<String> partVals) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public Partition exchange_partition(Map<String, String> partitionSpecs, String sourceDb, String sourceTable, String destdb, String destTableName) throws MetaException, NoSuchObjectException, InvalidObjectException, TException {
        return null;
    }

    @Override
    public Partition exchange_partition(Map<String, String> partitionSpecs, String sourceCat, String sourceDb, String sourceTable, String destCat, String destdb, String destTableName) throws MetaException, NoSuchObjectException, InvalidObjectException, TException {
        return null;
    }

    @Override
    public List<Partition> exchange_partitions(Map<String, String> partitionSpecs, String sourceDb, String sourceTable, String destdb, String destTableName) throws MetaException, NoSuchObjectException, InvalidObjectException, TException {
        return null;
    }

    @Override
    public List<Partition> exchange_partitions(Map<String, String> partitionSpecs, String sourceCat, String sourceDb, String sourceTable, String destCat, String destdb, String destTableName) throws MetaException, NoSuchObjectException, InvalidObjectException, TException {
        return null;
    }

    @Override
    public Partition getPartition(String dbName, String tblName, String name) throws MetaException, UnknownTableException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public Partition getPartition(String catName, String dbName, String tblName, String name) throws MetaException, UnknownTableException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public Partition getPartitionWithAuthInfo(String dbName, String tableName, List<String> pvals, String userName, List<String> groupNames) throws MetaException, UnknownTableException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public Partition getPartitionWithAuthInfo(String catName, String dbName, String tableName, List<String> pvals, String userName, List<String> groupNames) throws MetaException, UnknownTableException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public List<Partition> listPartitions(String db_name, String tbl_name, short max_parts) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<Partition> listPartitions(String catName, String db_name, String tbl_name, int max_parts) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public PartitionSpecProxy listPartitionSpecs(String dbName, String tableName, int maxParts) throws TException {
        return null;
    }

    @Override
    public PartitionSpecProxy listPartitionSpecs(String catName, String dbName, String tableName, int maxParts) throws TException {
        return null;
    }

    @Override
    public List<Partition> listPartitions(String db_name, String tbl_name, List<String> part_vals, short max_parts) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<Partition> listPartitions(String catName, String db_name, String tbl_name, List<String> part_vals, int max_parts) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<String> listPartitionNames(String db_name, String tbl_name, short max_parts) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<String> listPartitionNames(String catName, String db_name, String tbl_name, int max_parts) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<String> listPartitionNames(String db_name, String tbl_name, List<String> part_vals, short max_parts) throws MetaException, TException, NoSuchObjectException {
        return null;
    }

    @Override
    public List<String> listPartitionNames(String catName, String db_name, String tbl_name, List<String> part_vals, int max_parts) throws MetaException, TException, NoSuchObjectException {
        return null;
    }

    @Override
    public PartitionValuesResponse listPartitionValues(PartitionValuesRequest request) throws MetaException, TException, NoSuchObjectException {
        return null;
    }

    @Override
    public int getNumPartitionsByFilter(String dbName, String tableName, String filter) throws MetaException, NoSuchObjectException, TException {
        return 0;
    }

    @Override
    public int getNumPartitionsByFilter(String catName, String dbName, String tableName, String filter) throws MetaException, NoSuchObjectException, TException {
        return 0;
    }

    @Override
    public List<Partition> listPartitionsByFilter(String db_name, String tbl_name, String filter, short max_parts) throws MetaException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public List<Partition> listPartitionsByFilter(String catName, String db_name, String tbl_name, String filter, int max_parts) throws MetaException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public PartitionSpecProxy listPartitionSpecsByFilter(String db_name, String tbl_name, String filter, int max_parts) throws MetaException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public PartitionSpecProxy listPartitionSpecsByFilter(String catName, String db_name, String tbl_name, String filter, int max_parts) throws MetaException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public boolean listPartitionsByExpr(String db_name, String tbl_name, byte[] expr, String default_partition_name, short max_parts, List<Partition> result) throws TException {
        return false;
    }

    @Override
    public boolean listPartitionsByExpr(String catName, String db_name, String tbl_name, byte[] expr, String default_partition_name, int max_parts, List<Partition> result) throws TException {
        return false;
    }

    @Override
    public List<Partition> listPartitionsWithAuthInfo(String dbName, String tableName, short maxParts, String userName, List<String> groupNames) throws MetaException, TException, NoSuchObjectException {
        return null;
    }

    @Override
    public List<Partition> listPartitionsWithAuthInfo(String catName, String dbName, String tableName, int maxParts, String userName, List<String> groupNames) throws MetaException, TException, NoSuchObjectException {
        return null;
    }

    @Override
    public List<Partition> getPartitionsByNames(String db_name, String tbl_name, List<String> part_names) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<Partition> getPartitionsByNames(String catName, String db_name, String tbl_name, List<String> part_names) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<Partition> listPartitionsWithAuthInfo(String dbName, String tableName, List<String> partialPvals, short maxParts, String userName, List<String> groupNames) throws MetaException, TException, NoSuchObjectException {
        return null;
    }

    @Override
    public List<Partition> listPartitionsWithAuthInfo(String catName, String dbName, String tableName, List<String> partialPvals, int maxParts, String userName, List<String> groupNames) throws MetaException, TException, NoSuchObjectException {
        return null;
    }

    @Override
    public void markPartitionForEvent(String db_name, String tbl_name, Map<String, String> partKVs, PartitionEventType eventType) throws MetaException, NoSuchObjectException, TException, UnknownTableException, UnknownDBException, UnknownPartitionException, InvalidPartitionException {

    }

    @Override
    public void markPartitionForEvent(String catName, String db_name, String tbl_name, Map<String, String> partKVs, PartitionEventType eventType) throws MetaException, NoSuchObjectException, TException, UnknownTableException, UnknownDBException, UnknownPartitionException, InvalidPartitionException {

    }

    @Override
    public boolean isPartitionMarkedForEvent(String db_name, String tbl_name, Map<String, String> partKVs, PartitionEventType eventType) throws MetaException, NoSuchObjectException, TException, UnknownTableException, UnknownDBException, UnknownPartitionException, InvalidPartitionException {
        return false;
    }

    @Override
    public boolean isPartitionMarkedForEvent(String catName, String db_name, String tbl_name, Map<String, String> partKVs, PartitionEventType eventType) throws MetaException, NoSuchObjectException, TException, UnknownTableException, UnknownDBException, UnknownPartitionException, InvalidPartitionException {
        return false;
    }

    @Override
    public void validatePartitionNameCharacters(List<String> partVals) throws TException, MetaException {

    }

    @Override
    public void createTable(Table tbl) throws AlreadyExistsException, InvalidObjectException, MetaException, NoSuchObjectException, TException {

    }

    @Override
    public void alter_table(String databaseName, String tblName, Table table) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public void alter_table(String catName, String dbName, String tblName, Table newTable, EnvironmentContext envContext) throws InvalidOperationException, MetaException, TException {

    }
    @SuppressWarnings("deprecation")
    @Override
    public void alter_table(String defaultDatabaseName, String tblName, Table table, boolean cascade) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public void alter_table_with_environmentContext(String databaseName, String tblName, Table table, EnvironmentContext environmentContext) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public void createDatabase(Database db) throws InvalidObjectException, AlreadyExistsException, MetaException, TException {

    }

    @Override
    public void dropDatabase(String name) throws NoSuchObjectException, InvalidOperationException, MetaException, TException {

    }

    @Override
    public void dropDatabase(String name, boolean deleteData, boolean ignoreUnknownDb) throws NoSuchObjectException, InvalidOperationException, MetaException, TException {

    }

    @Override
    public void dropDatabase(String name, boolean deleteData, boolean ignoreUnknownDb, boolean cascade) throws NoSuchObjectException, InvalidOperationException, MetaException, TException {

    }

    @Override
    public void dropDatabase(String catName, String dbName, boolean deleteData, boolean ignoreUnknownDb, boolean cascade) throws NoSuchObjectException, InvalidOperationException, MetaException, TException {

    }

    @Override
    public void alterDatabase(String name, Database db) throws NoSuchObjectException, MetaException, TException {

    }

    @Override
    public void alterDatabase(String catName, String dbName, Database newDb) throws NoSuchObjectException, MetaException, TException {

    }

    @Override
    public boolean dropPartition(String db_name, String tbl_name, List<String> part_vals, boolean deleteData) throws NoSuchObjectException, MetaException, TException {
        return false;
    }

    @Override
    public boolean dropPartition(String catName, String db_name, String tbl_name, List<String> part_vals, boolean deleteData) throws NoSuchObjectException, MetaException, TException {
        return false;
    }

    @Override
    public boolean dropPartition(String db_name, String tbl_name, List<String> part_vals, PartitionDropOptions options) throws NoSuchObjectException, MetaException, TException {
        return false;
    }

    @Override
    public boolean dropPartition(String catName, String db_name, String tbl_name, List<String> part_vals, PartitionDropOptions options) throws NoSuchObjectException, MetaException, TException {
        return false;
    }

    @Override
    public List<Partition> dropPartitions(String dbName, String tblName, List<ObjectPair<Integer, byte[]>> partExprs, boolean deleteData, boolean ifExists) throws NoSuchObjectException, MetaException, TException {
        return null;
    }
    @SuppressWarnings("deprecation")
    @Override
    public List<Partition> dropPartitions(String dbName, String tblName, List<ObjectPair<Integer, byte[]>> partExprs, boolean deleteData, boolean ifExists, boolean needResults) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<Partition> dropPartitions(String dbName, String tblName, List<ObjectPair<Integer, byte[]>> partExprs, PartitionDropOptions options) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<Partition> dropPartitions(String catName, String dbName, String tblName, List<ObjectPair<Integer, byte[]>> partExprs, PartitionDropOptions options) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public boolean dropPartition(String db_name, String tbl_name, String name, boolean deleteData) throws NoSuchObjectException, MetaException, TException {
        return false;
    }

    @Override
    public boolean dropPartition(String catName, String db_name, String tbl_name, String name, boolean deleteData) throws NoSuchObjectException, MetaException, TException {
        return false;
    }

    @Override
    public void alter_partition(String dbName, String tblName, Partition newPart) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public void alter_partition(String dbName, String tblName, Partition newPart, EnvironmentContext environmentContext) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public void alter_partition(String catName, String dbName, String tblName, Partition newPart, EnvironmentContext environmentContext) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public void alter_partitions(String dbName, String tblName, List<Partition> newParts) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public void alter_partitions(String dbName, String tblName, List<Partition> newParts, EnvironmentContext environmentContext) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public void alter_partitions(String catName, String dbName, String tblName, List<Partition> newParts, EnvironmentContext environmentContext) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public void renamePartition(String dbname, String tableName, List<String> part_vals, Partition newPart) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public void renamePartition(String catName, String dbname, String tableName, List<String> part_vals, Partition newPart) throws InvalidOperationException, MetaException, TException {

    }

    @Override
    public List<FieldSchema> getFields(String db, String tableName) throws MetaException, TException, UnknownTableException, UnknownDBException {
        return null;
    }

    @Override
    public List<FieldSchema> getFields(String catName, String db, String tableName) throws MetaException, TException, UnknownTableException, UnknownDBException {
        return null;
    }

    @Override
    public List<FieldSchema> getSchema(String db, String tableName) throws MetaException, TException, UnknownTableException, UnknownDBException {
        return null;
    }

    @Override
    public List<FieldSchema> getSchema(String catName, String db, String tableName) throws MetaException, TException, UnknownTableException, UnknownDBException {
        return null;
    }

    @Override
    public String getConfigValue(String name, String defaultValue) throws TException, ConfigValSecurityException {
        return null;
    }

    @Override
    public List<String> partitionNameToVals(String name) throws MetaException, TException {
        return null;
    }

    @Override
    public Map<String, String> partitionNameToSpec(String name) throws MetaException, TException {
        return null;
    }

    @Override
    public boolean updateTableColumnStatistics(ColumnStatistics statsObj) throws NoSuchObjectException, InvalidObjectException, MetaException, TException, InvalidInputException {
        return false;
    }

    @Override
    public boolean updatePartitionColumnStatistics(ColumnStatistics statsObj) throws NoSuchObjectException, InvalidObjectException, MetaException, TException, InvalidInputException {
        return false;
    }

    @Override
    public List<ColumnStatisticsObj> getTableColumnStatistics(String dbName, String tableName, List<String> colNames) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<ColumnStatisticsObj> getTableColumnStatistics(String catName, String dbName, String tableName, List<String> colNames) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public Map<String, List<ColumnStatisticsObj>> getPartitionColumnStatistics(String dbName, String tableName, List<String> partNames, List<String> colNames) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public Map<String, List<ColumnStatisticsObj>> getPartitionColumnStatistics(String catName, String dbName, String tableName, List<String> partNames, List<String> colNames) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public boolean deletePartitionColumnStatistics(String dbName, String tableName, String partName, String colName) throws NoSuchObjectException, MetaException, InvalidObjectException, TException, InvalidInputException {
        return false;
    }

    @Override
    public boolean deletePartitionColumnStatistics(String catName, String dbName, String tableName, String partName, String colName) throws NoSuchObjectException, MetaException, InvalidObjectException, TException, InvalidInputException {
        return false;
    }

    @Override
    public boolean deleteTableColumnStatistics(String dbName, String tableName, String colName) throws NoSuchObjectException, MetaException, InvalidObjectException, TException, InvalidInputException {
        return false;
    }

    @Override
    public boolean deleteTableColumnStatistics(String catName, String dbName, String tableName, String colName) throws NoSuchObjectException, MetaException, InvalidObjectException, TException, InvalidInputException {
        return false;
    }

    @Override
    public boolean create_role(Role role) throws MetaException, TException {
        return false;
    }

    @Override
    public boolean drop_role(String role_name) throws MetaException, TException {
        return false;
    }

    @Override
    public List<String> listRoleNames() throws MetaException, TException {
        return null;
    }

    @Override
    public boolean grant_role(String role_name, String user_name, PrincipalType principalType, String grantor, PrincipalType grantorType, boolean grantOption) throws MetaException, TException {
        return false;
    }

    @Override
    public boolean revoke_role(String role_name, String user_name, PrincipalType principalType, boolean grantOption) throws MetaException, TException {
        return false;
    }

    @Override
    public List<Role> list_roles(String principalName, PrincipalType principalType) throws MetaException, TException {
        return null;
    }

    @Override
    public PrincipalPrivilegeSet get_privilege_set(HiveObjectRef hiveObject, String user_name, List<String> group_names) throws MetaException, TException {
        return null;
    }

    @Override
    public List<HiveObjectPrivilege> list_privileges(String principal_name, PrincipalType principal_type, HiveObjectRef hiveObject) throws MetaException, TException {
        return null;
    }

    @Override
    public boolean grant_privileges(PrivilegeBag privileges) throws MetaException, TException {
        return false;
    }

    @Override
    public boolean revoke_privileges(PrivilegeBag privileges, boolean grantOption) throws MetaException, TException {
        return false;
    }

    @Override
    public boolean refresh_privileges(HiveObjectRef objToRefresh, String authorizer, PrivilegeBag grantPrivileges) throws MetaException, TException {
        return false;
    }

    @Override
    public String getDelegationToken(String owner, String renewerKerberosPrincipalName) throws MetaException, TException {
        return null;
    }

    @Override
    public long renewDelegationToken(String tokenStrForm) throws MetaException, TException {
        return 0;
    }

    @Override
    public void cancelDelegationToken(String tokenStrForm) throws MetaException, TException {

    }

    @Override
    public String getTokenStrForm() throws IOException {
        return null;
    }

    @Override
    public boolean addToken(String tokenIdentifier, String delegationToken) throws TException {
        return false;
    }

    @Override
    public boolean removeToken(String tokenIdentifier) throws TException {
        return false;
    }

    @Override
    public String getToken(String tokenIdentifier) throws TException {
        return null;
    }

    @Override
    public List<String> getAllTokenIdentifiers() throws TException {
        return null;
    }

    @Override
    public int addMasterKey(String key) throws MetaException, TException {
        return 0;
    }

    @Override
    public void updateMasterKey(Integer seqNo, String key) throws NoSuchObjectException, MetaException, TException {

    }

    @Override
    public boolean removeMasterKey(Integer keySeq) throws TException {
        return false;
    }

    @Override
    public String[] getMasterKeys() throws TException {
        return new String[0];
    }

    @Override
    public void createFunction(Function func) throws InvalidObjectException, MetaException, TException {

    }

    @Override
    public void alterFunction(String dbName, String funcName, Function newFunction) throws InvalidObjectException, MetaException, TException {

    }

    @Override
    public void alterFunction(String catName, String dbName, String funcName, Function newFunction) throws InvalidObjectException, MetaException, TException {

    }

    @Override
    public void dropFunction(String dbName, String funcName) throws MetaException, NoSuchObjectException, InvalidObjectException, InvalidInputException, TException {

    }

    @Override
    public void dropFunction(String catName, String dbName, String funcName) throws MetaException, NoSuchObjectException, InvalidObjectException, InvalidInputException, TException {

    }

    @Override
    public Function getFunction(String dbName, String funcName) throws MetaException, TException {
        return null;
    }

    @Override
    public Function getFunction(String catName, String dbName, String funcName) throws MetaException, TException {
        return null;
    }

    @Override
    public List<String> getFunctions(String dbName, String pattern) throws MetaException, TException {
        return null;
    }

    @Override
    public List<String> getFunctions(String catName, String dbName, String pattern) throws MetaException, TException {
        return null;
    }

    @Override
    public GetAllFunctionsResponse getAllFunctions() throws MetaException, TException {
        return null;
    }

    @Override
    public ValidTxnList getValidTxns() throws TException {
        return null;
    }

    @Override
    public ValidTxnList getValidTxns(long currentTxn) throws TException {
        return null;
    }

    @Override
    public ValidWriteIdList getValidWriteIds(String fullTableName) throws TException {
        return null;
    }

    @Override
    public List<TableValidWriteIds> getValidWriteIds(List<String> tablesList, String validTxnList) throws TException {
        return null;
    }

    @Override
    public long openTxn(String user) throws TException {
        return 0;
    }

    @Override
    public List<Long> replOpenTxn(String replPolicy, List<Long> srcTxnIds, String user) throws TException {
        return null;
    }

    @Override
    public OpenTxnsResponse openTxns(String user, int numTxns) throws TException {
        return null;
    }

    @Override
    public void rollbackTxn(long txnid) throws NoSuchTxnException, TException {

    }

    @Override
    public void replRollbackTxn(long srcTxnid, String replPolicy) throws NoSuchTxnException, TException {

    }

    @Override
    public void commitTxn(long txnid) throws NoSuchTxnException, TxnAbortedException, TException {

    }

    @Override
    public void replCommitTxn(long srcTxnid, String replPolicy) throws NoSuchTxnException, TxnAbortedException, TException {

    }

    @Override
    public void abortTxns(List<Long> txnids) throws TException {

    }

    @Override
    public long allocateTableWriteId(long txnId, String dbName, String tableName) throws TException {
        return 0;
    }

    @Override
    public void replTableWriteIdState(String validWriteIdList, String dbName, String tableName, List<String> partNames) throws TException {

    }

    @Override
    public List<TxnToWriteId> allocateTableWriteIdsBatch(List<Long> txnIds, String dbName, String tableName) throws TException {
        return null;
    }

    @Override
    public List<TxnToWriteId> replAllocateTableWriteIdsBatch(String dbName, String tableName, String replPolicy, List<TxnToWriteId> srcTxnToWriteIdList) throws TException {
        return null;
    }

    @Override
    public GetOpenTxnsInfoResponse showTxns() throws TException {
        return null;
    }

    @Override
    public LockResponse lock(LockRequest request) throws NoSuchTxnException, TxnAbortedException, TException {
        return null;
    }

    @Override
    public LockResponse checkLock(long lockid) throws NoSuchTxnException, TxnAbortedException, NoSuchLockException, TException {
        return null;
    }

    @Override
    public void unlock(long lockid) throws NoSuchLockException, TxnOpenException, TException {

    }
    @SuppressWarnings("deprecation")
    @Override
    public ShowLocksResponse showLocks() throws TException {
        return null;
    }

    @Override
    public ShowLocksResponse showLocks(ShowLocksRequest showLocksRequest) throws TException {
        return null;
    }

    @Override
    public void heartbeat(long txnid, long lockid) throws NoSuchLockException, NoSuchTxnException, TxnAbortedException, TException {

    }

    @Override
    public HeartbeatTxnRangeResponse heartbeatTxnRange(long min, long max) throws TException {
        return null;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void compact(String dbname, String tableName, String partitionName, CompactionType type) throws TException {

    }
    @SuppressWarnings("deprecation")
    @Override
    public void compact(String dbname, String tableName, String partitionName, CompactionType type, Map<String, String> tblproperties) throws TException {

    }

    @Override
    public CompactionResponse compact2(String dbname, String tableName, String partitionName, CompactionType type, Map<String, String> tblproperties) throws TException {
        return null;
    }

    @Override
    public ShowCompactResponse showCompactions() throws TException {
        return null;
    }
    @SuppressWarnings("deprecation")
    @Override
    public void addDynamicPartitions(long txnId, long writeId, String dbName, String tableName, List<String> partNames) throws TException {

    }

    @Override
    public void addDynamicPartitions(long txnId, long writeId, String dbName, String tableName, List<String> partNames, DataOperationType operationType) throws TException {

    }

    @Override
    public void insertTable(Table table, boolean overwrite) throws MetaException {

    }

    @Override
    public NotificationEventResponse getNextNotification(long lastEventId, int maxEvents, NotificationFilter filter) throws TException {
        return null;
    }

    @Override
    public CurrentNotificationEventId getCurrentNotificationEventId() throws TException {
        return null;
    }

    @Override
    public NotificationEventsCountResponse getNotificationEventsCount(NotificationEventsCountRequest rqst) throws TException {
        return null;
    }

    @Override
    public FireEventResponse fireListenerEvent(FireEventRequest request) throws TException {
        return null;
    }

    @Override
    public GetPrincipalsInRoleResponse get_principals_in_role(GetPrincipalsInRoleRequest getPrincRoleReq) throws MetaException, TException {
        return null;
    }

    @Override
    public GetRoleGrantsForPrincipalResponse get_role_grants_for_principal(GetRoleGrantsForPrincipalRequest getRolePrincReq) throws MetaException, TException {
        return null;
    }

    @Override
    public AggrStats getAggrColStatsFor(String dbName, String tblName, List<String> colNames, List<String> partName) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public AggrStats getAggrColStatsFor(String catName, String dbName, String tblName, List<String> colNames, List<String> partNames) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public boolean setPartitionColumnStatistics(SetPartitionsStatsRequest request) throws NoSuchObjectException, InvalidObjectException, MetaException, TException, InvalidInputException {
        return false;
    }

    @Override
    public void flushCache() {

    }

    @Override
    public Iterable<Map.Entry<Long, ByteBuffer>> getFileMetadata(List<Long> fileIds) throws TException {
        return null;
    }

    @Override
    public Iterable<Map.Entry<Long, MetadataPpdResult>> getFileMetadataBySarg(List<Long> fileIds, ByteBuffer sarg, boolean doGetFooters) throws TException {
        return null;
    }

    @Override
    public void clearFileMetadata(List<Long> fileIds) throws TException {

    }

    @Override
    public void putFileMetadata(List<Long> fileIds, List<ByteBuffer> metadata) throws TException {

    }

    @Override
    public boolean isSameConfObj(Configuration c) {
        return false;
    }

    @Override
    public boolean cacheFileMetadata(String dbName, String tableName, String partName, boolean allParts) throws TException {
        return false;
    }

    @Override
    public List<SQLPrimaryKey> getPrimaryKeys(PrimaryKeysRequest request) throws MetaException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public List<SQLForeignKey> getForeignKeys(ForeignKeysRequest request) throws MetaException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public List<SQLUniqueConstraint> getUniqueConstraints(UniqueConstraintsRequest request) throws MetaException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public List<SQLNotNullConstraint> getNotNullConstraints(NotNullConstraintsRequest request) throws MetaException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public List<SQLDefaultConstraint> getDefaultConstraints(DefaultConstraintsRequest request) throws MetaException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public List<SQLCheckConstraint> getCheckConstraints(CheckConstraintsRequest request) throws MetaException, NoSuchObjectException, TException {
        return null;
    }

    @Override
    public void createTableWithConstraints(Table tTbl, List<SQLPrimaryKey> primaryKeys, List<SQLForeignKey> foreignKeys, List<SQLUniqueConstraint> uniqueConstraints, List<SQLNotNullConstraint> notNullConstraints, List<SQLDefaultConstraint> defaultConstraints, List<SQLCheckConstraint> checkConstraints) throws AlreadyExistsException, InvalidObjectException, MetaException, NoSuchObjectException, TException {

    }

    @Override
    public void dropConstraint(String dbName, String tableName, String constraintName) throws MetaException, NoSuchObjectException, TException {

    }

    @Override
    public void dropConstraint(String catName, String dbName, String tableName, String constraintName) throws MetaException, NoSuchObjectException, TException {

    }

    @Override
    public void addPrimaryKey(List<SQLPrimaryKey> primaryKeyCols) throws MetaException, NoSuchObjectException, TException {

    }

    @Override
    public void addForeignKey(List<SQLForeignKey> foreignKeyCols) throws MetaException, NoSuchObjectException, TException {

    }

    @Override
    public void addUniqueConstraint(List<SQLUniqueConstraint> uniqueConstraintCols) throws MetaException, NoSuchObjectException, TException {

    }

    @Override
    public void addNotNullConstraint(List<SQLNotNullConstraint> notNullConstraintCols) throws MetaException, NoSuchObjectException, TException {

    }

    @Override
    public void addDefaultConstraint(List<SQLDefaultConstraint> defaultConstraints) throws MetaException, NoSuchObjectException, TException {

    }

    @Override
    public void addCheckConstraint(List<SQLCheckConstraint> checkConstraints) throws MetaException, NoSuchObjectException, TException {

    }

    @Override
    public String getMetastoreDbUuid() throws MetaException, TException {
        return null;
    }

    @Override
    public void createResourcePlan(WMResourcePlan resourcePlan, String copyFromName) throws InvalidObjectException, MetaException, TException {

    }

    @Override
    public WMFullResourcePlan getResourcePlan(String resourcePlanName) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public List<WMResourcePlan> getAllResourcePlans() throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public void dropResourcePlan(String resourcePlanName) throws NoSuchObjectException, MetaException, TException {

    }

    @Override
    public WMFullResourcePlan alterResourcePlan(String resourcePlanName, WMNullableResourcePlan resourcePlan, boolean canActivateDisabled, boolean isForceDeactivate, boolean isReplace) throws NoSuchObjectException, InvalidObjectException, MetaException, TException {
        return null;
    }

    @Override
    public WMFullResourcePlan getActiveResourcePlan() throws MetaException, TException {
        return null;
    }

    @Override
    public WMValidateResourcePlanResponse validateResourcePlan(String resourcePlanName) throws NoSuchObjectException, InvalidObjectException, MetaException, TException {
        return null;
    }

    @Override
    public void createWMTrigger(WMTrigger trigger) throws InvalidObjectException, MetaException, TException {

    }

    @Override
    public void alterWMTrigger(WMTrigger trigger) throws NoSuchObjectException, InvalidObjectException, MetaException, TException {

    }

    @Override
    public void dropWMTrigger(String resourcePlanName, String triggerName) throws NoSuchObjectException, MetaException, TException {

    }

    @Override
    public List<WMTrigger> getTriggersForResourcePlan(String resourcePlan) throws NoSuchObjectException, MetaException, TException {
        return null;
    }

    @Override
    public void createWMPool(WMPool pool) throws NoSuchObjectException, InvalidObjectException, MetaException, TException {

    }

    @Override
    public void alterWMPool(WMNullablePool pool, String poolPath) throws NoSuchObjectException, InvalidObjectException, TException {

    }

    @Override
    public void dropWMPool(String resourcePlanName, String poolPath) throws TException {

    }

    @Override
    public void createOrUpdateWMMapping(WMMapping mapping, boolean isUpdate) throws TException {

    }

    @Override
    public void dropWMMapping(WMMapping mapping) throws TException {

    }

    @Override
    public void createOrDropTriggerToPoolMapping(String resourcePlanName, String triggerName, String poolPath, boolean shouldDrop) throws AlreadyExistsException, NoSuchObjectException, InvalidObjectException, MetaException, TException {

    }

    @Override
    public void createISchema(ISchema schema) throws TException {

    }

    @Override
    public void alterISchema(String catName, String dbName, String schemaName, ISchema newSchema) throws TException {

    }

    @Override
    public ISchema getISchema(String catName, String dbName, String name) throws TException {
        return null;
    }

    @Override
    public void dropISchema(String catName, String dbName, String name) throws TException {

    }

    @Override
    public void addSchemaVersion(SchemaVersion schemaVersion) throws TException {

    }

    @Override
    public SchemaVersion getSchemaVersion(String catName, String dbName, String schemaName, int version) throws TException {
        return null;
    }

    @Override
    public SchemaVersion getSchemaLatestVersion(String catName, String dbName, String schemaName) throws TException {
        return null;
    }

    @Override
    public List<SchemaVersion> getSchemaAllVersions(String catName, String dbName, String schemaName) throws TException {
        return null;
    }

    @Override
    public void dropSchemaVersion(String catName, String dbName, String schemaName, int version) throws TException {

    }

    @Override
    public FindSchemasByColsResp getSchemaByCols(FindSchemasByColsRqst rqst) throws TException {
        return null;
    }

    @Override
    public void mapSchemaVersionToSerde(String catName, String dbName, String schemaName, int version, String serdeName) throws TException {

    }

    @Override
    public void setSchemaVersionState(String catName, String dbName, String schemaName, int version, SchemaVersionState state) throws TException {

    }

    @Override
    public void addSerDe(SerDeInfo serDeInfo) throws TException {

    }

    @Override
    public SerDeInfo getSerDe(String serDeName) throws TException {
        return null;
    }

    @Override
    public LockResponse lockMaterializationRebuild(String dbName, String tableName, long txnId) throws TException {
        return null;
    }

    @Override
    public boolean heartbeatLockMaterializationRebuild(String dbName, String tableName, long txnId) throws TException {
        return false;
    }

    @Override
    public void addRuntimeStat(RuntimeStat stat) throws TException {

    }

    @Override
    public List<RuntimeStat> getRuntimeStats(int maxWeight, int maxCreateTime) throws TException {
        return null;
    }
}
