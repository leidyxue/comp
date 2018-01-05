package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.exception.BiException;
import com.baifendian.comp.common.structs.chart.QueryChart;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.common.utils.BaseResponseUtils;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.comp.dao.postgresql.model.table.TableAllData;
import com.baifendian.comp.dao.utils.GenerateSql;
import com.baifendian.bi.engine.factory.SqlBuilder;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.SQL;
import com.baifendian.bi.engine.meta.SQLTable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SQLContext {

  private final static Logger logger = LoggerFactory.getLogger(SQLContext.class);

  public static long getTableSize(JDBCParam jdbcParam, String tableName) {
    String sql = GenerateSql.tableSizeSql(jdbcParam.getType(), tableName);
    List<List<Object>> result = JDBCExec.dsExec(jdbcParam, sql);

    if (CollectionUtils.isNotEmpty(result)) {
      return (long) result.get(0).get(0);
    }

    return 0;
  }

  public static List<String> getRangeSql(TableAllData tableAllData, Field field) {
    Map<String, String> fieldMeta = JDBCExec
        .dsExec(tableAllData.jdbcParam(), c -> c.tableFieldMeta(tableAllData.tableOrgName()));

    SqlBuilder sqlBuilder = GenerateSql.createSqlBuilder(tableAllData.getDSType());
    SQLTable sqlTable = sqlBuilder.table(tableAllData.tableOrgName());
    Map<String, Column> columns = GenerateSql
        .createTableColumnNew(sqlTable, tableAllData.getFieldList(), fieldMeta);

    SQL sql = sqlBuilder.sql()
        .setDistinct()
        .addColumn(columns.get(field.getId()))
        .table(sqlTable)
        .limit(100);

    List<List<Object>> datas = JDBCExec
        .dsExec(tableAllData.jdbcParam(), dsConnect -> dsConnect.getTableData(sql.build()));
    return datas.stream().map(d -> d.get(0)).filter(Objects::nonNull).map(Object::toString)
        .collect(Collectors.toList());
  }

  public static SqlResultData getTableData(TableAllData tableAllData) {
    SqlResultData.SqlResultDataBuilder builder = SqlResultData.builder();
    try {
      SQL sql = GenerateSql.createTableDataSql(tableAllData
          , JDBCExec.dsExec(tableAllData.jdbcParam(),
              dsConnect -> dsConnect.tableFieldMeta(tableAllData.tableOrgName())));
      builder.titles(tableAllData.getFieldList().stream().filter(Field::nonDeleted).map(Field::getName).collect(
          Collectors.toList()));
      builder.dataList(JDBCExec.dsExec(tableAllData.jdbcParam(), sql.build()));
    } catch (BiException e) {
      builder.dataStatus(SqRunStatus.FAILURE);
      builder.dataMessage(BaseResponseUtils.baseResponseTranslation(e.getMsgKey(), e.getArgs()));
    } catch (Exception e) {
      logger.info("Get table data error.", e);
      builder.dataStatus(SqRunStatus.FAILURE);
      builder.dataMessage(BaseResponseUtils.baseResponseTranslation(
          "com.baifendian.bi.api.datasource.operator.error"));
    }

    return builder.build();
  }

  public static SqlResultData getChartData(TableAllData table, QueryChart query
      , DrillDownData drillDownLevel, String filter, boolean realTitle) {
    SQL sql = GenerateSql.createChartSQLNew(table, query,
        JDBCExec.dsExec(table.jdbcParam(),
            dsConnect -> dsConnect.tableFieldMeta(table.tableOrgName())), drillDownLevel, filter);

    SqlResultData.SqlResultDataBuilder builder = SqlResultData.builder();
    List<Column> columnList = sql.getColumns();

    try {
      builder.dataList(JDBCExec.dsExec(table.jdbcParam(), sql.build()));
//          .dsExec(table.jdbcParam(), sql.build(), resultSet -> {
//            List<Object> resultList = new ArrayList<>();
//            for (Column column : columnList) {
//              Object obj = resultSet.getObject(column.getAlisa());
//
//              if (column instanceof DateColumn) {
//                resultList.add(DateI18nUtil.dateFormat(obj, ((DateColumn) column).getDateType()));
//              } else {
//                resultList.add(obj);
//              }
//            }
//            return resultList;
//          }));
    } catch (BiException e) {
      builder.dataStatus(SqRunStatus.FAILURE);
      builder.dataMessage(BaseResponseUtils.baseResponseTranslation(e.getMsgKey(), e.getArgs()));
    } catch (Exception e) {
      logger.info("Get table data error.", e);
      builder.dataStatus(SqRunStatus.FAILURE);
      builder.dataMessage(BaseResponseUtils.baseResponseTranslation(
          "com.baifendian.bi.api.datasource.operator.error"));
    }

    if (realTitle) {
      for (String title : sql.getTitle()) {
        Optional<Field> fieldOptional = table.getField(drillDownLevel.getFieldId(title));
        builder.title(fieldOptional.isPresent() ? fieldOptional.get().getName() : "");
      }
    } else {
      builder.titles(sql.getTitle());
    }

    return builder.build();
  }

}
