package com.baifendian.comp.dao.postgresql.mapper;

import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class DsDirRefMapperProvider {
  final private static String TABLE_NAME = "\"ds_dir_ref\"";

  public String insert() {
    return new SQL() {
      {
        INSERT_INTO(TABLE_NAME);
        VALUES("ds_id", "#{dsDirRef.dsId}");
        VALUES("dir_id", "#{dsDirRef.dirId}");
        VALUES("owner", "#{dsDirRef.owner}");
      }
    }.toString();
  }

  public String selectByUser(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("*");
        FROM(TABLE_NAME);
        WHERE("owner = #{userId} ");
      }
    }.toString();
  }

  public String selectByDsId(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("*");
        FROM(TABLE_NAME);
        WHERE("owner = #{userId} ");
        WHERE("ds_Id = #{dsId} ");
      }
    }.toString();
  }

  public String deleteByDsId(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(TABLE_NAME);
        WHERE("ds_id = #{dsId} ");
        WHERE("owner = #{userId} ");
      }
    }.toString();
  }
}
