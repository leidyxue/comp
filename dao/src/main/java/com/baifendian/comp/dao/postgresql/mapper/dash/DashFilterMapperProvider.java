package com.baifendian.comp.dao.postgresql.mapper.dash;

import com.google.common.base.Joiner;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class DashFilterMapperProvider {

  private final static String FILTER_TABLE = "dash_filter";

  private Joiner joiner = Joiner.on("','");

  public String deleteByDash(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(FILTER_TABLE);
        WHERE("chart_id in (select id from chart where dash_id = #{dashId})");
      }
    }.toString();
  }

  public String deleteByChart(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(FILTER_TABLE);
        WHERE("chart_id = #{chartId}");
      }
    }.toString();
  }
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(FILTER_TABLE);
        VALUES("id", "#{dashFilter.id}");
        VALUES("name", "#{dashFilter.name}");
        VALUES("field_key", "#{dashFilter.fieldId}");
        VALUES("chart_id", "#{dashFilter.chartId}");
      }
    }.toString();
  }
  public String queryByDashId(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("t1.*, t2.table_Id table_Id, t2.type field_Type, t3.dash_id dash_id");
        FROM("dash_filter t1 join field t2 on (t1.field_key = t2.id)");
        JOIN(" chart t3 on (t1.chart_id = t3.id) ");
        WHERE("t3.dash_Id = #{dashId}");
      }
    }.toString();
  }
}
