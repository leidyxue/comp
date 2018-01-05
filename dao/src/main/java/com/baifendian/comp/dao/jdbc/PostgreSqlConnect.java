package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.dao.structs.sql.DbFieldMeta;
import com.baifendian.comp.dao.structs.sql.DbTableMeta;
import com.baifendian.bi.engine.util.SqlUtil;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgreSqlConnect extends DsConnect{
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public List<DbTableMeta> getTableMetas(String schema) {
    List<DbTableMeta> tableMetaList = new ArrayList<>();
    try {
      DatabaseMetaData dbmd = getConnection().getMetaData();
      try (ResultSet tables = dbmd.getTables(null, schema, "%"
          , TABLE_TYPE)) {
        while (tables.next()) {
          String orgTableName = tables.getString("TABLE_NAME");
          String tableName = orgTableName;
          if (schema == null){
            schema = tables.getString("TABLE_SCHEM");
          }
          if (StringUtils.isNotEmpty(schema)) {
            tableName = schema + "." + tableName;
          }
          DbTableMeta.DbTableMetaBuilder builder = DbTableMeta.builder()
              .tableName(tableName);

          try (ResultSet columns = dbmd.getColumns(null, schema, orgTableName, "%")) {
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
      //throw parseException(e);
    }
    return tableMetaList;

  }

  public PostgreSqlConnect(JDBCParam jdbcParam)
      throws Exception {
    super(jdbcParam);
  }


}
