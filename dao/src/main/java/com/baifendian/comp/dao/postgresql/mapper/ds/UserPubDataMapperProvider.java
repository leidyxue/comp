package com.baifendian.comp.dao.postgresql.mapper.ds;

import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class UserPubDataMapperProvider {

  private static String tableName = "\"user_public_data\"";

  public String insert(Map<String, Object> parameter) {

    String sql = new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("id", "#{pubData.id}");
        VALUES("create_time", "#{pubData.createTime}");
        VALUES("owner", "#{pubData.userId}");
      }
    }.toString();
    return sql;
  }

  public String deleteById(){
    return new SQL() {
      {
        DELETE_FROM(tableName);
        WHERE(" id = #{pubId}");
        WHERE(" owner = #{userId}");
      }
    }.toString();
  }

  public String findByUserId(){
    return new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE(" owner = #{userId}");
      }
    }.toString();
  }

}
