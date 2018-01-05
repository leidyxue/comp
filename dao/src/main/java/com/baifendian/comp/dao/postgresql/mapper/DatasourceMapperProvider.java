package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.comp.dao.utils.JsonFieldUtil;
import com.baifendian.bi.engine.util.SqlUtil;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class DatasourceMapperProvider {

  private static String tableName = "\"datasource\"";
  /**
   * 插入一条Datasource
   */
  public String insert(Map<String, Object> parameter) {

    return new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("id", "#{datasource.id}");
        VALUES("name", "#{datasource.name}");
        VALUES("type", EnumFieldUtil.genFieldStr("datasource.type", DSType.class, "ds_type"));
        VALUES("parameter", JsonFieldUtil.genFieldStr("datasource.parameter"));
        VALUES("create_time", "#{datasource.createTime}");
        VALUES("modify_time", "#{datasource.modifyTime}");
        VALUES("owner", "#{datasource.owner}");
        VALUES(SqlUtil.fieldAddQuote("desc"),"#{datasource.desc}");
       // VALUES(SqlUtil.fieldAddQuote("dir_id"),"#{datasource.dirId}");
      }
    }.toString();
  }

  public String updateInner(){
    return "INSERT INTO "+tableName+"(id,name,type,parameter, create_time, modify_time, "+
        SqlUtil.fieldAddQuote("desc")+")"
        + "VALUES(#{datasource.id}, #{datasource.name},"+EnumFieldUtil.genFieldStr("datasource.type", DSType.class, "ds_type")
        +","+JsonFieldUtil.genFieldStr("datasource.parameter")+", #{datasource.createTime}, #{datasource.modifyTime}, #{datasource.desc})"
        + "ON conflict(id) DO UPDATE SET parameter = " + JsonFieldUtil.genFieldStr("datasource.parameter");
  }

  public String update(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(tableName);
        SET("name = #{datasource.name}");
        SET("\"desc\" = #{datasource.desc}");
        SET("type = "+ EnumFieldUtil.genFieldStr("datasource.type", DSType.class, "ds_type"));
        SET("parameter = "+ JsonFieldUtil.genFieldStr("datasource.parameter"));
        SET("modify_time = #{datasource.modifyTime}");
        WHERE("id = #{datasource.id}");
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

  public String findById(Map<String, Object> parameter) {
    String sql = new SQL() {
      {
        SELECT("*");
        FROM("\"datasource\"");
        WHERE("id = #{id}");
      }
    }.toString();
    return sql;
  }

  public String findAll(Map<String, Object> parameter) {
    DSType type = (DSType) parameter.get("type");
    String sql = new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE("owner = #{userId}");
        if (type != null){
          WHERE("type = "+ EnumFieldUtil.genFieldStr("type", DSType.class, "ds_type"));
        }
      }
    }.toString();
    return sql;
  }

  public String findSummary(Map<String, Object> parameter) {

    return new SQL() {
      {
        SELECT("type, count(1) count");
        FROM(tableName);
        WHERE("owner = #{userId}");
        GROUP_BY("type");
      }
    }.toString();
  }
}
