package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.comp.dao.utils.JsonFieldUtil;
import com.baifendian.bi.engine.util.SqlUtil;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class DashShareMapperProvider {

  private static String tableName = "\"share_dash\"";

  /**
   * 插入一条dashboard
   */
  public String insert(Map<String, Object> parameter) {

    String sql = new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("id", "#{shareDash.id}");
       // VALUES("createTime", "#{shareDash.createTime}");
        VALUES("dash_id", "#{shareDash.dashId}");
      }
    }.toString();
    return sql;
  }

  public String deleteShare(){
    return new SQL() {
      {
        DELETE_FROM(tableName);
        WHERE(" id = #{shareId}");
      }
    }.toString();
  }

  public String queryByDashId(){
    return new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE(" dash_id = #{dashId}");
      }
    }.toString();
  }
  public String queryById(){
    return new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE(" id = #{shareId}");
      }
    }.toString();
  }

  public String insertChart(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(SqlUtil.fieldAddQuote("share_chart"));
        VALUES("id", "#{chart.id}");
        VALUES("name", "#{chart.name}");
        VALUES("table_id", "#{chart.tableId}");
        VALUES("share_id", "#{chart.shareId}");
        VALUES("\"desc\"", "#{chart.desc}");
        VALUES("meta", JsonFieldUtil.genFieldStr("chart.meta"));
        VALUES("chart_type",
            EnumFieldUtil.genFieldStr("chart.chartType", DSType.class, "chart_type"));
        VALUES("create_time", "#{chart.createTime}");
      }
    }.toString();
  }

}
