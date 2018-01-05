package com.baifendian.comp.dao.utils;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.comp.common.exception.BiException;
import com.baifendian.comp.common.utils.AggregatorUtil;
import com.baifendian.comp.dao.jdbc.DrillDownData;
import com.baifendian.comp.dao.jdbc.TableColumnData;
import com.baifendian.comp.dao.postgresql.model.chart.ChartField;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.comp.dao.postgresql.model.table.TableAllData;
import com.baifendian.bi.engine.element.condition.ExpressionCondition;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.comp.common.structs.chart.QueryChart;
import com.baifendian.bi.engine.element.condition.BetweenCondition;
import com.baifendian.bi.engine.element.condition.InCondition;
import com.baifendian.bi.engine.element.condition.MatchCondition;
import com.baifendian.bi.engine.element.column.CombineColumn;
import com.baifendian.bi.engine.enums.CombineLogic;
import com.baifendian.bi.engine.enums.FuncType;
import com.baifendian.bi.engine.enums.MatchType;
import com.baifendian.bi.engine.enums.SortType;
import com.baifendian.bi.engine.factory.KylinFactory;
import com.baifendian.bi.engine.factory.MysqlFactory;
import com.baifendian.bi.engine.factory.PostgreSqlFactory;
import com.baifendian.bi.engine.factory.SqlBuilder;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.ColumnType;
import com.baifendian.bi.engine.meta.GroupBy;
import com.baifendian.bi.engine.meta.OrderBy;
import com.baifendian.bi.engine.meta.SQL;
import com.baifendian.bi.engine.meta.SQLTable;
import com.baifendian.bi.engine.util.SqlUtil;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;

public class GenerateSql {

  public static String tableSizeSql(DSType type, String tableName) {
    SqlBuilder sqlBuilder = createSqlBuilder(type);
    SQLTable sqlTable = sqlBuilder.table(tableName);

    return sqlBuilder.sql()
        .table(sqlTable)
        .addColumn(sqlTable
            .createFuncColumn(FuncType.COUNT, sqlTable.createConstsColumn("1", "1", FieldType.TEXT),
                "2"))
        .build();
  }

  public static Map<String, Column> createTableColumnNew(SQLTable sqlTable,
      final List<Field> fields, final Map<String, String> fieldTypeMeta) {
    Optional<Field> delField = fields.stream().filter(Field::isNative)
        .filter(f -> !fieldTypeMeta.containsKey(f.getOriginName()))
        .findFirst();
    if (delField.isPresent()) {
      throw new BiException("com.bfd.bi.api.common.field.del", HttpStatus.PRECONDITION_FAILED,
          delField.get().getName());
    }
    Map<String, Column> columns = fields.stream()
        .filter(f -> f.getGenType() == FieldGenType.NATIVE)
        .map(f -> sqlTable.createColumn(f.getOriginName(), f.getId(),
            new ColumnType(f.getType(),
                SqlUtil.createValueType(fieldTypeMeta.get(f.getOriginName())))))
        .collect(Collectors.toMap(Column::getId, c -> c));

    List<Field> tfList = fields.stream()
        .filter(f -> f.getGenType() == FieldGenType.T_GENERATE)
        .collect(Collectors.toList());
    while (!tfList.isEmpty()) {
      // TODO  field -> ${null}
      Map<String, Column> cols = tfList.stream()
          .filter(f -> AggregatorUtil.getFieldIds(f.getAggregator()).stream()
              .allMatch(columns::containsKey))
          .map((Field f) -> {
            // create new column
            List<String> ids = AggregatorUtil.getFieldIds(f.getAggregator());
            if (ids.isEmpty()) {
              return sqlTable.createConstsColumn(f.getId(), f.getAggregator(), f.getType());
            }
            // check whether id is deleted
            Optional<String> idOpt = ids.stream().filter(id -> !columns.containsKey(id))
                .findFirst();
            if (idOpt.isPresent()) {
              throw new BiException("com.bfd.bi.api.common.field.del",
                  HttpStatus.PRECONDITION_FAILED);
            }
            return new CombineColumn(f.getAggregator(), f.getId(), f.getType(),
                ids.stream().map(columns::get).collect(Collectors.toList()));
          })
          .collect(Collectors.toMap(Column::getId, c -> c));

      columns.putAll(cols);

      Map<String, Column> errorField = tfList.stream()
          .filter(f -> !(AggregatorUtil.getFieldIds(f.getAggregator()).stream()
              .allMatch(columns::containsKey)))
          .map(f -> sqlTable.createNullColumn(f.getId()))
          .collect(Collectors.toMap(Column::getId, c -> c));

      columns.putAll(errorField);

      tfList = tfList.stream().filter(f -> !columns.containsKey(f.getId()))
          .collect(Collectors.toList());
    }

    return columns;
  }

  static public SQL createChartSQLNew(TableAllData table, QueryChart query,
      final Map<String, String> fieldTypeMeta, DrillDownData drillDownLevel, String filter) {
    SqlBuilder sqlBuilder = createSqlBuilder(table.getDSType());
    SQLTable sqlTable = sqlBuilder.table(table.tableOrgName());
    Map<String, Field> fieldMap = table.getFieldList().stream()
        .collect(Collectors.toMap(Field::getId, f -> f));
    TableColumnData tableColumnData = new TableColumnData(createTableColumnNew(sqlTable, table.nonDeletedField(),
        fieldTypeMeta), fieldMap);

    /**
     * 不要影响原始数据
     */
    List<ChartField> x = drillDownLevel.getX().stream().collect(Collectors.toList());
    List<ChartField> y = drillDownLevel.getY().stream().collect(Collectors.toList());
    if (CollectionUtils.isNotEmpty(drillDownLevel.getYOptional())) {
      y.addAll(drillDownLevel.getYOptional());
    }

    List<Column> columnList = x.stream()
        .map(cf -> {
          Column column = tableColumnData.findColumn(cf.getFieldId()).modifyId(cf.getUniqId());
          if (cf.getOperator() != null) {
            return sqlBuilder.createFuncColumn(cf.getUniqId(), cf.getOperator(), column);
          }
          return column;
        })
        .collect(Collectors.toList());

    // x轴字段，加入group 列表
    GroupBy groupBy = sqlBuilder.createGroupBy();
    columnList.forEach(groupBy::addGroupBy);

    // 将y及次轴字段，加入select列表
    columnList.addAll(y.stream()
        .map(cf -> {
          Column column = tableColumnData.findColumn(cf.getFieldId());
          if (column == null){
            throw new BiException("com.baifendian.bi.api.datasource.table.field.name.error", HttpStatus.PRECONDITION_FAILED, fieldMap.get(cf.getFieldId()).getName());
          }
          return sqlTable
              .createFuncColumn(cf.getOperator(), column, cf.getUniqId());
        })
        .collect(Collectors.toList()));

    OrderBy orderBy = sqlBuilder.createOrderBy();
    x.forEach(cf -> {
      if (cf.getSort() != SortType.DEFAULT) {
        orderBy.addColumn(
            columnList.stream().filter(c -> c.getId().equals(cf.getUniqId())).findFirst().get(),
            cf.getSort());
      }
    });
    y.forEach(cf -> {
      if (cf.getSort() != SortType.DEFAULT) {
        orderBy.addColumn(
            columnList.stream().filter(c -> c.getId().equals(cf.getUniqId())).findFirst().get(),
            cf.getSort());
      }
    });

    SQL sql = sqlBuilder.sql().table(sqlTable)
        .addColumn(columnList)
        .setGroupBy(groupBy)
        .setOrderBy(orderBy)
        .limit(1000);

    // 联动
    if (CollectionUtils.isNotEmpty(query.getLinkValues())) {
      query.getLinkValues().stream().map(lv -> {
        Field field = fieldMap.get(lv.getFieldId());
        return new MatchCondition(tableColumnData.findColumn(lv.getFieldId()),
            sqlBuilder.createSQLValue(lv.getValue(), field.getType()), MatchType.EQUAL);
      }).filter(Objects::nonNull)
          .forEach(condition -> sql.setCondition(CombineLogic.AND, condition));
    }

    // 图表筛选条件
    if (CollectionUtils.isNotEmpty(query.getFilterInner())) {
      query.getFilterInner().stream().filter(qcf -> CollectionUtils.isNotEmpty(qcf.getRange()))
          .map(qcf -> {
            Column column = tableColumnData.findColumn(qcf.getFieldId());
            Field field = fieldMap.get(qcf.getFieldId());
            if (field.getType() == FieldType.TEXT || field.getType() == FieldType.NUM) {
              return new InCondition(column, sqlBuilder.createTextValue(qcf.getRange()));
            } else {
              if (qcf.getRange().size() == 1) {
                String first = qcf.getRange().get(0);
                return new MatchCondition(column, sqlBuilder.createDateValue(first),
                    MatchType.EQUAL);
              } else {
                String first = qcf.getRange().get(0);
                String sec = qcf.getRange().get(1);

                if (first != null && sec != null) {
                  return new BetweenCondition(column,
                      sqlBuilder.createDateValue(first + " 00:00:00"),
                      sqlBuilder.createDateValue(sec + " 23:59:59"));
                } else if (first != null) {
                  return new MatchCondition(column, sqlBuilder.createDateValue(first + " 00:00:00"),
                      MatchType.GREATER);
                } else if (sec != null) {
                  return new MatchCondition(column, sqlBuilder.createDateValue(sec + " 23:59:59"),
                      MatchType.LESS);
                }

                return null;
              }
            }
          }).filter(Objects::nonNull)
          .forEach(condition -> sql.setCondition(CombineLogic.AND, condition));
    }

    // 处理下钻
    for (int i = 0; i < query.getDrillDownValues().size() && i < drillDownLevel.getDrills().size();
        ++i) {
      //ChartFieldParam field = meta.getDrillDownFields().get(i);
      Field field = fieldMap.get(drillDownLevel.getDrills().get(i));
      Column column = tableColumnData.findColumn(field.getId());
      String val = query.getDrillDownValues().get(i);

      switch (field.getType()) {
        case TEXT:
          sql.setCondition(CombineLogic.AND,
              new MatchCondition(column, sqlBuilder.createTextValue(val), MatchType.EQUAL));
          break;

        case NUM:
          sql.setCondition(CombineLogic.AND,
              new MatchCondition(column, sqlBuilder.createNumValue(val), MatchType.EQUAL));
          break;

        case DATE:
          sql.setCondition(CombineLogic.AND,
              new MatchCondition(column, sqlBuilder.createDateValue(val), MatchType.EQUAL));
          break;
        default:
          break;
      }
    }

    if (filter != null) {
      // create new column
      // TODO filete
      List<String> ids = AggregatorUtil.getFieldIds(filter);
      if (!ids.isEmpty()) {
        //return sqlTable.createConstsColumn(f.getId(), f.getAggregator(), f.getType());
        sql.setCondition(new ExpressionCondition(filter,
            ids.stream().map(tableColumnData::findColumn).collect(Collectors.toList())));
      } else {
        sql.setCondition(new ExpressionCondition(filter, null));
      }
    }

    return sql;
  }

  static public SQL  createTableDataSql(TableAllData tableAllData,
      final Map<String, String> fieldTypeMeta) {
    SqlBuilder sqlBuilder = createSqlBuilder(tableAllData.getDSType());

    SQLTable sqlTable = sqlBuilder.table(tableAllData.tableOrgName());
    List<Field> fields = tableAllData.tableDataField();

    Map<String, Column> columns = createTableColumnNew(sqlTable, fields, fieldTypeMeta);

    return sqlBuilder.sql()
        .addColumn(fields.stream().filter(Field::nonDeleted).map(f ->
        {
          Column col = columns.get(f.getId());
          if (f.isNative()) {
            // 原始数据，直接当原始类型显示
            return sqlTable.createColumn(f.getOriginName(), f.getId(), col.getType().getOrgType());
          }
          return col;
        }).collect(Collectors.toList()))
        .table(sqlTable)
        .limit(100);
  }

  public static SqlBuilder createSqlBuilder(DSType type) {
    switch (type) {
      case KYLIN:
        return new KylinFactory();

      case POSTGRESQL:
        return new PostgreSqlFactory();

      case MYSQL:
      default:
        return new MysqlFactory();
    }
  }
}
