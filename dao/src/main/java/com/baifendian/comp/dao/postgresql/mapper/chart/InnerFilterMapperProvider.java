package com.baifendian.comp.dao.postgresql.mapper.chart;

import com.baifendian.comp.dao.postgresql.model.chart.InnerFilter;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class InnerFilterMapperProvider {

  private final static String TABLE_NAME = "chart_inner_filter";

  public String deleteByDash(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(TABLE_NAME);
        WHERE("chart_id in (SELECT id from chart where dash_id = #{deleteByDash} ) "
            + " OR chart_id in (SELECT id from share_chart where share_id in (SELECT id from share_dashboard where dash_id = #{deleteByDash}) )");
      }
    }.toString();
  }

  public String deleteByChart(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(TABLE_NAME);
        WHERE("chart_id = #{chartId} ");
      }
    }.toString();
  }
  /**
   * 插入一条chart filter
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(TABLE_NAME);
        VALUES("field_key", "#{innerFilter.fieldId}");
        VALUES("chart_id", "#{innerFilter.chartId}");
      }
    }.toString();
  }

  public String batchInsert(Map<String, Object> parameter) {
    List<InnerFilter> fieldList = (List<InnerFilter>) parameter.get("list");
    StringBuilder sb = new StringBuilder();
    sb.append("INSERT INTO ");
    sb.append(TABLE_NAME);
    sb.append(
        "(field_key, chart_id)");
    sb.append("VALUES ");
    String fm =
        "(#'{'list[{0}].fieldId}, #'{'list[{0}].chartId})";
    MessageFormat mf = new MessageFormat(fm);
    for (int i = 0; i < fieldList.size(); i++) {
      sb.append(mf.format(new Object[]{i}));
      if (i < fieldList.size() - 1) {
        sb.append(",");
      }
    }
    return sb.toString();
  }

  public String findByChartId(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("*");
        FROM("chart_inner_filter");
        WHERE("chart_id = #{chartId}");
      }
    }.toString();
  }
}
