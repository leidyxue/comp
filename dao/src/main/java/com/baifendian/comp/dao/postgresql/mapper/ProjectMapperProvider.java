package com.baifendian.comp.dao.postgresql.mapper;

import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class ProjectMapperProvider {

  private static String tableName = "\"project\"";

  /**
   * 插入一条Datasource
   */
  public String insert(Map<String, Object> parameter) {

    String sql = new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("id", "#{project.id}");
        VALUES("name", "#{project.name}");
        VALUES("create_time", "#{project.createTime}");
        VALUES("modify_time", "#{project.modifyTime}");
        VALUES("parent_id", "#{project.parentId}");
        VALUES("owner", "#{project.owner}");
      }
    }.toString();
    return sql;
  }


  public String update(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(tableName);
        SET("name = #{project.name}");
        SET("modify_time = #{project.modifyTime}");
        SET("parent_id = #{project.parentId}");
        SET("owner = #{project.owner}");
        WHERE("id = #{project.id}");
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

  public String findByName(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE("owner = #{owner}");
        WHERE("name = #{name}");
      }
    }.toString();
  }

  public String findByIdAndOwner(Map<String, Object> parameter){
    return new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE("id = #{id}");
        WHERE("owner = #{owner}");
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

  public String deleteById(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(tableName);
        WHERE("id = #{id}");
      }
    }.toString();
  }

  public String findChildById(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(tableName);
        WHERE("id = #{id}");
      }
    }.toString();
  }
}
