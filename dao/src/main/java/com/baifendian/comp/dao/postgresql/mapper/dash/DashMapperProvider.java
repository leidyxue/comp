package com.baifendian.comp.dao.postgresql.mapper.dash;

import com.baifendian.comp.dao.utils.JsonFieldUtil;
import com.baifendian.bi.engine.util.SqlUtil;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class DashMapperProvider {
  private static String tableName = "\"dashboard\"";

  /**
   * 插入一条dashboard
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("id", "#{dashboard.id}");
        VALUES("name", "#{dashboard.name}");
        VALUES(SqlUtil.fieldAddQuote("desc"), "#{dashboard.desc}");
        VALUES("web_data", JsonFieldUtil.genFieldStr("dashboard.webData"));
        VALUES("create_time", "#{dashboard.createTime}");
        VALUES("modify_time", "#{dashboard.modifyTime}");
        VALUES("project_id", "#{dashboard.projectId}");
        VALUES("owner", "#{dashboard.owner}");
      }
    }.toString();
  }

  public String findById(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE("id = #{id}");
      }
    }.toString();
  }

  public String findByOwner(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE("owner = #{owner}");
      }
    }.toString();
  }

  public String update(Map<String, Object> parameter){
    return new SQL() {
      {
        UPDATE(tableName);
        SET("name = #{dashboard.name}");
        SET("\"desc\" = #{dashboard.desc}");
        SET("web_data = "+JsonFieldUtil.genFieldStr("dashboard.webData"));
        SET("modify_time = #{dashboard.modifyTime}");
        SET("project_id = #{dashboard.projectId}");
        //SET("owner = #{dashboard.owner}");
        WHERE("id = #{dashboard.id}");
      }
    }.toString();
  }

  public String deleteById(Map<String, Object> parameter){
    return new SQL() {
      {
        DELETE_FROM(tableName);
        WHERE("id = #{id}");
      }
    }.toString();
  }

  public String findByProjectId(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE("project_id = #{projectId}");
      }
    }.toString();
  }
}
