package com.baifendian.comp.dao.postgresql.mapper;

import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class FolderMapperProvider {

  private static String TableName = "\"dir\"";

  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(TableName);
        VALUES("id", "#{folder.id}");
        VALUES("name", "#{folder.name}");
        VALUES("parent_id", "#{folder.parentId}");
        VALUES("create_time", "#{folder.createTime}");
        VALUES("modify_time", "#{folder.modifyTime}");
        VALUES("owner", "#{folder.owner}");
      }
    }.toString();
  }

  public String deleteById(Map<String, Object> parameter) {

    return new SQL() {
      {
        DELETE_FROM(TableName);
        WHERE("id = #{id}");
      }
    }.toString();
  }

  public String deleteByName(Map<String, Object> parameter) {

    return new SQL() {
      {
        DELETE_FROM(TableName);
        WHERE("name = #{name}");
      }
    }.toString();
  }

  public String updateById(Map<String, Object> parameter) {

    return new SQL() {
      {
        UPDATE(TableName);
        SET("name=#{folder.name}");
        SET("parent_id=#{folder.parentId}");
        SET("modify_time=#{folder.modifyTime}");
        WHERE("id = #{folder.id}");
      }
    }.toString();
  }

  public String selectById(Map<String, Object> parameter) {

    return new SQL() {
      {
        SELECT("*");
        FROM(TableName);
        WHERE("id = #{id}");
      }
    }.toString();
  }

  public String selectByName(Map<String, Object> parameter) {

    return new SQL() {
      {
        SELECT("*");
        FROM(TableName);
        WHERE("name = #{name}");
        WHERE("owner = #{user}");
      }
    }.toString();
  }

  public String selectByUser(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("*");
        FROM(TableName);
        WHERE("owner = #{userId} ");
        ORDER_BY("create_time");
      }
    }.toString();
  }
}
