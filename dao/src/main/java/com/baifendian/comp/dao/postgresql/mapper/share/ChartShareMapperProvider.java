package com.baifendian.comp.dao.postgresql.mapper.share;

import org.apache.ibatis.jdbc.SQL;

public class ChartShareMapperProvider {
  public String queryById(){
    return new SQL() {
      {
        SELECT("s.*, t.name table_name");
        FROM("share_chart s join \"table\" t on s.table_id = t.id");
        WHERE("s.id = #{chartId}");
      }
    }.toString();
  }

}
