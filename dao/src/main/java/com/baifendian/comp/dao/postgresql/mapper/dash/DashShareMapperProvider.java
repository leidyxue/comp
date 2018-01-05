package com.baifendian.comp.dao.postgresql.mapper.dash;

import org.apache.ibatis.jdbc.SQL;

public class DashShareMapperProvider {
  final private static String TABLE_NAME = "\"share_dashboard\"";

  /**
   * 插入
   */
  public String insert() {
    return new SQL() {
      {
        INSERT_INTO(TABLE_NAME);
        VALUES("id", "#{shareDash.id}");
        VALUES("dash_id", "#{shareDash.dashId}");
      }
    }.toString();
  }

}
