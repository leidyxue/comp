package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.common.structs.datasource.TableMeta;
import com.baifendian.comp.common.structs.datasource.TableMeta.FieldMeta;
import com.baifendian.comp.dao.structs.sql.DbMeta;
import com.baifendian.comp.dao.utils.GenerateSql;
import com.baifendian.comp.common.utils.HttpUtil;
import com.baifendian.comp.dao.structs.sql.DbFieldMeta;
import com.baifendian.comp.dao.structs.sql.DbTableMeta;
import com.baifendian.bi.engine.factory.SqlBuilder;
import com.baifendian.bi.engine.meta.SQL;
import com.baifendian.bi.engine.meta.SQLTable;
import com.baifendian.bi.engine.util.SqlUtil;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DsConnect implements AutoCloseable {

  public static final String[] TABLE_TYPE = new String[]{"TABLE", "VIEW"};

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private Connection connection;
  private JDBCParam jdbcParam;

  public Connection getConnection(){
    return connection;
  }

  public DsConnect(JDBCParam jdbcParam) throws Exception {
    HttpUtil.checkParam(jdbcParam);

    connection = JDBCPool.getInstance().borrowConn(jdbcParam);

    this.jdbcParam = jdbcParam;
  }

  public Map<String, String> tableFieldMeta(String tableName) {
    Map<String, String> fieldMeta = new HashMap<>();

    try {
      DatabaseMetaData dbmd = connection.getMetaData();

      String[] names = tableName.split("\\.");
      String schema = null;
      String orgName = tableName;

      if (names.length == 2) {
        schema = names[0];
        orgName = names[1];
      }

      try (ResultSet columns = dbmd.getColumns(null, schema, orgName, "%")) {
        while (columns.next()) {
          fieldMeta.put(columns.getString("COLUMN_NAME"),
              SqlUtil.typeClean(columns.getString("TYPE_NAME")));
        }
      }
    } catch (SQLException e) {
      logger.info("Get table sql error.", e);
      throw parseException(e);
    }

    return fieldMeta;
  }

  public DbMeta getAllTableMeta() {
    return new DbMeta(getTableMetas());
  }

  public List<DbTableMeta> getTableMetas() {
    List<DbTableMeta> tableMetaList = new ArrayList<>();
    try {
      DatabaseMetaData dbmd = connection.getMetaData();
      try (ResultSet tables = dbmd.getTables(null, null, "%"
          , TABLE_TYPE)) {
        while (tables.next()) {
          String orgTableName = tables.getString("TABLE_NAME");
          String tableName = orgTableName;
          String schem = tables.getString("TABLE_SCHEM");
          if (StringUtils.isNotEmpty(schem)) {
            tableName = schem + "." + tableName;
          }
          DbTableMeta.DbTableMetaBuilder builder = DbTableMeta.builder()
              .tableName(tableName);

          try (ResultSet columns = dbmd.getColumns(null, schem, orgTableName, "%")) {
            int sortId = 0;
            while (columns.next()) {

              builder.field(DbFieldMeta.builder()
                  .desc(columns.getString("REMARKS"))
                  .name(columns.getString("COLUMN_NAME"))
                  .type(SqlUtil.createValueType(columns.getInt("DATA_TYPE")))
                  .orgType(SqlUtil.typeClean(columns.getString("TYPE_NAME")))
                  .sortId(sortId++).build());
            }
          }

          tableMetaList.add(builder.build());
        }
      }
    } catch (SQLException e) {
      logger.info("Get table sql error.", e);
      throw parseException(e);
    }
    return tableMetaList;
  }

  public List<TableMeta> tableMetas() {
    List<TableMeta> tableMetaList = new ArrayList<>();
    try {
      DatabaseMetaData dbmd = connection.getMetaData();
      try (ResultSet tables = dbmd.getTables(null, null, "%"
          , TABLE_TYPE)) {
        while (tables.next()) {
          String orgTableName = tables.getString("TABLE_NAME");
          String tableName = orgTableName;
          String schem = tables.getString("TABLE_SCHEM");
          if (StringUtils.isNotEmpty(schem)) {
            tableName = schem + "." + tableName;
          }
          TableMeta.TableMetaBuilder builder = TableMeta.builder()
              .tableName(tableName);

          List<String> titles = new ArrayList<>();
          try (ResultSet columns = dbmd.getColumns(null, schem, orgTableName
              , "%")) {
            while (columns.next()) {
              TableMeta.FieldMeta fieldMeta = new TableMeta.FieldMeta();
              fieldMeta.setName(columns.getString("COLUMN_NAME"));
              fieldMeta.setDesc(columns.getString("REMARKS"));
              fieldMeta.setType(SqlUtil.createValueType(columns.getInt("DATA_TYPE")));
              fieldMeta.setOrgType(SqlUtil.typeClean(columns.getString("TYPE_NAME")));

              builder.field(fieldMeta);
              titles.add(fieldMeta.getName());
            }
          }

          builder.titles(titles);
          tableMetaList.add(builder.build());
        }
      }
    } catch (SQLException e) {
      logger.info("Get table sql error.", e);
      throw parseException(e);
    }

    return tableMetaList;
  }

  public List<TableMeta> tableMetaAndData() {
    List<TableMeta> tableMetaList = tableMetas();
    for (TableMeta tableMeta : tableMetaList) {
      tableMeta.setData(getTableData(tableMeta.getTableName(), tableMeta.getSchema()));
    }

    return tableMetaList;
  }

  public List<List<Object>> getTableData(String tableName, List<FieldMeta> fieldMetas) {
    SqlBuilder sqlBuilder = GenerateSql.createSqlBuilder(jdbcParam.getType());

    SQLTable table = sqlBuilder.table(tableName);
    SQL sql = sqlBuilder.sql()
        .addColumn(
            fieldMetas.stream().map(f -> table.createColumn(f.getName(), f.getName(), f.getType()))
                .collect(Collectors.toList()))
        .table(table)
        .limit(10);
    try {
      return getTableData(sql.build());
    } catch (Exception e) {
      return null;
    }
  }

  void execute(String sql) {
    try (Statement statement = connection.createStatement()) {
      statement.execute(sql);
    } catch (SQLException e) {
      logger.info("Get table sql error.", e);
      throw parseException(e);
    }
  }

  public List<List<Object>> getTableData(String sql, ResultSetFunc function) {
    List<List<Object>> result = new ArrayList<>();

    try (Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement.executeQuery(sql)) {
        while (resultSet.next()) {
          result.add(function.apply(resultSet));
        }
      }
    } catch (SQLException e) {
      throw JdbcUtil.exceptionParse(e, jdbcParam.getType());
    }

    return result;
  }

  public List<List<Object>> getTableData(String sql) {
    logger.info("Begin run sql:{}", sql);
    List<List<Object>> result = new ArrayList<>();

    try (Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement.executeQuery(sql)) {
        int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
          List<Object> objectList = new ArrayList<>();

          for (int i = 1; i <= columnCount; ++i) {
            objectList.add(resultSet.getObject(i));
          }
          result.add(objectList);
        }
      }
    } catch (SQLException e) {
      logger.info("Get table sql error.", e);
      //throw parseException(e);
    }

    return result;
  }

  @Override
  public void close() throws Exception {
    JDBCPool.getInstance().returnConn(jdbcParam, connection);
  }

  private RuntimeException parseException(SQLException e) {
    return new JdbcException(e);
  }
}
