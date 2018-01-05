package com.baifendian.comp.dao.postgresql.mapper.dash;

import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class DashLinkMapperProvider {

  private static String linkTableName = "\"dash_link\"";
  /**
   * 插入一条dashboard
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(linkTableName);
        VALUES("dash_id", "#{dashLink.dashId}");
        VALUES("chart_id", "#{dashLink.chartId}");
        VALUES("field_key", "#{dashLink.fieldId}");
        VALUES("link_chart_id", "#{dashLink.linkChartId}");
        VALUES("link_field_key", "#{dashLink.linkFieldId}");
      }
    }.toString();
  }

  public String deleteByChart(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(linkTableName);
        WHERE("chart_id = #{chartId} OR link_chart_id = #{chartId}");
      }
    }.toString();
  }

  public String deleteByDash(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(linkTableName);
        WHERE("dash_id = #{dashId}");
      }
    }.toString();
  }
  public String queryByDashId(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("t1.*, t2.table_Id table_Id, t3.table_id link_table_id");
        FROM("dash_link t1 join field t2 on (t1.field_key = t2.id)");
        JOIN(" field t3 on (t1.link_field_key = t3.id)");
        WHERE("t1.dash_Id = #{dashId}");
      }
    }.toString();
  }
}
