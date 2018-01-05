package com.baifendian.comp.dao.postgresql.mapper.chart;

import com.baifendian.comp.common.enums.ChartFieldType;
import com.baifendian.comp.dao.postgresql.model.chart.ChartField;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.bi.engine.enums.SortType;
import com.baifendian.bi.engine.util.SqlUtil;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class ChartFieldMapperProvider {
  private final static String TABLE_NAME = "chart_field";

  public String deleteByDash(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(TABLE_NAME);
        WHERE("chart_id in (SELECT id from chart where dash_id = #{deleteByDash} ) "
            + " OR chart_id in (SELECT id from share_chart where share_id in (SELECT id from share_dashboard where dash_id = #{deleteByDash}) )");
      }
    }.toString();
  }

  /**
   * 插入一条chart field
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(TABLE_NAME);
        VALUES("uniq_id", "#{chartField.uniqId}");
        VALUES("field_key", "#{chartField.fieldId}");
        VALUES("alias", "#{chartField.alias}");
        VALUES("operator", "#{chartField.operator}");
        VALUES("sort",
            EnumFieldUtil.genFieldStr("chartField.sort", SortType.class, "field_sort_type"));
        VALUES("chart_id", "#{chartField.chartId}");
        VALUES("level", "#{chartField.level}");
        VALUES(SqlUtil.fieldAddQuote("order"), "#{chartField.order}");
        VALUES("type",
            EnumFieldUtil.genFieldStr("chartField.type", ChartFieldType.class, "chart_meta_type"));
      }
    }.toString();
  }

  public String batchInsert(Map<String, Object> parameter) {
    List<ChartField> fieldList = (List<ChartField>) parameter.get("list");
    StringBuilder sb = new StringBuilder();
    sb.append("INSERT INTO ");
    sb.append(TABLE_NAME);
    sb.append(
        "(uniq_id, field_key, alias, operator, sort, chart_id, level, type)");
    sb.append(" VALUES ");
    String fm =
        "(#'{'list[{0}].uniqId}, #'{'list[{0}].fieldId}, #'{'list[{0}].alias}, #'{'list[{0}].operator}, "
            + EnumFieldUtil.genEnumFieldStr("list[{0}].sort", SortType.class, "field_sort_type")
            + ", #'{'list[{0}].chartId},  #'{'list[{0}].level}, "
            + EnumFieldUtil.genEnumFieldStr("list[{0}].type", SortType.class, "chart_meta_type")
        +")";
    MessageFormat mf = new MessageFormat(fm);
    for (int i = 0; i < fieldList.size(); i++) {
      sb.append(mf.format(new Object[]{i}));
      if (i < fieldList.size() - 1) {
        sb.append(",");
      }
    }
    return sb.toString();
  }

  public String deleteByChart(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(TABLE_NAME);
        WHERE("chart_id = #{chartId} ");
      }
    }.toString();
  }

  public String findByChartId(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("t1.*, t2.type field_Type");
        FROM("chart_field t1 join \"field\" t2 on (t1.field_key = t2.id)");
        WHERE("t1.chart_id = #{chartId}");
      }
    }.toString();
  }
  public String findByUser(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("t1.*, t2.type field_Type");
        FROM("chart_field t1 join \"field\" t2 on (t1.field_key = t2.id)");
        WHERE("t2.owner = #{userId}");
      }
    }.toString();
  }
}
