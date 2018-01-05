package com.baifendian.comp.dao.postgresql.mapper.dash;

import com.baifendian.comp.common.enums.ElementType;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.comp.dao.utils.JsonFieldUtil;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class DashElementMapperProvider {

  private final static String ELEMENT_TABLE = "dash_element";

  public String deleteByDash(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(ELEMENT_TABLE);
        WHERE(
            "dash_id = #{dashId} ");
      }
    }.toString();
  }

  public String deleteByChart(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(ELEMENT_TABLE);
        WHERE("id = #{chartId}");
      }
    }.toString();
  }
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(ELEMENT_TABLE);
        VALUES("id", "#{dashElement.id}");
        VALUES("x", "#{dashElement.x}");
        VALUES("y", "#{dashElement.y}");
        VALUES("w", "#{dashElement.w}");
        VALUES("h", "#{dashElement.h}");
        VALUES("dash_id", "#{dashElement.dashId}");
        VALUES("settings", JsonFieldUtil.genFieldStr("dashElement.settings"));
        VALUES("type",
            EnumFieldUtil.genFieldStr("dashElement.type", ElementType.class, "dash_element_type"));
      }
    }.toString();
  }

  public String queryByDashId(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("t1.*, t2.table_Id table_Id");
        FROM("dash_element t1 left join chart t2 on (t1.id = t2.id)");
        WHERE("t1.dash_Id = #{dashId}");
      }
    }.toString();
  }
}
