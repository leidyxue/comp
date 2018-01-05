package com.baifendian.comp.dao.postgresql.mapper.chart;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.comp.dao.utils.JsonFieldUtil;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class ChartMapperProvider {
  private static String tableName = "\"chart\"";
  /**
   * 插入一条chart
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("id", "#{chart.id}");
        VALUES("name", "#{chart.name}");
        VALUES("table_id", "#{chart.tableId}");
        VALUES("dash_id", "#{chart.dashId}");
        VALUES("\"desc\"", "#{chart.desc}");
        VALUES("web_data", JsonFieldUtil.genFieldStr("chart.webData"));
        VALUES("chart_type",
            EnumFieldUtil.genFieldStr("chart.chartType", DSType.class, "chart_type"));
        VALUES("modify_time", "#{chart.modifyTime}");
        VALUES("create_time", "#{chart.createTime}");
        VALUES("owner", "#{chart.owner}");
        VALUES("filter_exp", "#{chart.filterExp}");
      }
    }.toString();
  }

  public String findByDId(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("c.*");
        SELECT("t.name as table_name");
        SELECT("d.name as dash_name");
        SELECT("p.name as project_name");
        SELECT("p.id as project_id");
        FROM(tableName + " as c");
        JOIN("\"table\" as t on t.id = c.table_id");
        JOIN("\"dashboard\" as d on d.id = c.dash_id");
        LEFT_OUTER_JOIN("\"project\" as p on p.id = d.project_id");
        WHERE("c.dash_id = #{dashId}");
      }
    }.toString();
  }

  public String findByTId(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("c.*");
        SELECT("t.name as table_name");
        SELECT("d.name as dash_name");
        SELECT("p.name as project_name");
        SELECT("p.id as project_id");
        FROM(tableName + " as c");
        JOIN("\"table\" as t on t.id = c.table_id");
        JOIN("\"dashboard\" as d on d.id = c.dash_id");
        LEFT_OUTER_JOIN("\"project\" as p on p.id = d.project_id");
        WHERE("t.id = #{tableId}");
      }
    }.toString();
  }
  public String findById(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("c.*");
        SELECT("t.name as table_name");
        SELECT("d.name as dash_name");
        FROM(tableName + " as c");
        JOIN("\"table\" as t on t.id = c.table_id");
        JOIN("\"dashboard\" as d on d.id = c.dash_id");
        WHERE("c.id = #{id}");
      }
    }.toString();
  }

  /**
   * 更新一个chart
   */
  public String update(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(tableName);
        SET("name = #{chart.name}");
        SET("table_id = #{chart.tableId}");
        SET("dash_id = #{chart.dashId}");
        SET("\"desc\" = #{chart.desc}");
        SET("web_data = " + JsonFieldUtil.genFieldStr("chart.webData"));
        SET("modify_time = #{chart.modifyTime}");
        SET("filter_exp = #{chart.filterExp}");
        SET("chart_type =" + EnumFieldUtil
            .genFieldStr("chart.chartType", DSType.class, "chart_type"));
        WHERE("id = #{chart.id}");
      }
    }.toString();
  }

  public String deleteById(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(tableName);
        WHERE("id = #{id}");
      }
    }.toString();
  }
}
