package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.bi.engine.util.SqlUtil;
import com.google.common.base.Joiner;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class FieldMapperProvider {

  final private static String TABLE_NAME = "\"field\"";
  final private static String joinTableName = "\"field\" f";

  /**
   * 插入
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(TABLE_NAME);
        VALUES("id", "#{field.id}");
        VALUES("name", "#{field.name}");
        VALUES("origin_name", "#{field.originName}");
        VALUES("f_origin_name", "#{field.fileOriginName}");
        VALUES("origin_type", "#{field.originType}");
        VALUES("type",
            EnumFieldUtil.genFieldStr("field.type", FieldType.class, "field_value_type"));
        VALUES("gen_type",
            EnumFieldUtil.genFieldStr("field.genType", FieldGenType.class, "field_gen_type"));
        VALUES("aggregator", "#{field.aggregator}");
//        VALUES("parameter", JsonFieldUtil.genFieldStr("field.parameter"));
        VALUES("create_time", "#{field.createTime}");
        VALUES("modify_time", "#{field.modifyTime}");
        VALUES("owner", "#{field.owner}");
        VALUES("sort_id", "#{field.sortId}");
        VALUES(SqlUtil.fieldAddQuote("desc"), "#{field.desc}");
        VALUES("table_id", "#{field.tableId}");
        //VALUES("table_id", "#{field.tableId}");
      }
    }.toString();
  }

  public String batchInsert(Map<String, Object> parameter) {
    List<Field> fieldList = (List<Field>) parameter.get("list");
    StringBuilder sb = new StringBuilder();
    sb.append("INSERT INTO ");
    sb.append(TABLE_NAME);
    sb.append(
        "(id, name, origin_name, f_origin_name, origin_type, type, gen_type, aggregator, create_time, modify_time"
            + ", owner, table_id, sort_id ) ");
    sb.append("VALUES ");
    String fm =
        "(#'{'list[{0}].id}, #'{'list[{0}].name}, #'{'list[{0}].originName}, #'{'list[{0}].fileOriginName}, #'{'list[{0}].originType}, "
            + EnumFieldUtil.genEnumFieldStr("list[{0}].type", FieldType.class, "field_value_type")
            + "," + EnumFieldUtil
            .genEnumFieldStr("list[{0}].genType", FieldGenType.class, "field_gen_type")
            + ", #'{'list[{0}].aggregator}, now(), now(), #'{'list[{0}].owner}, #'{'list[{0}].tableId}"
            +", #'{'list[{0}].sortId})";
    MessageFormat mf = new MessageFormat(fm);
    for (int i = 0; i < fieldList.size(); i++) {
      sb.append(mf.format(new Object[]{i}));
      if (i < fieldList.size() - 1) {
        sb.append(",");
      }
    }
    return sb.toString();
  }

  public String updateById(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(TABLE_NAME);
        SET("name = #{field.name}");
        SET("origin_type = #{field.originType}");
        SET("type = " + EnumFieldUtil.genFieldStr("field.type", FieldType.class, "field_value_type"));
        SET("aggregator = #{field.aggregator}");
        SET("modify_time = #{field.modifyTime}");
        SET("table_id = #{field.tableId}");
        SET(SqlUtil.fieldAddQuote("desc") + " = #{field.desc}");
        WHERE("id = #{field.id}");
      }
    }.toString();
  }
  public String updateDeleteById(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(TABLE_NAME);
        SET("deleted = true");
        WHERE("id = #{fieldId}");
      }
    }.toString();
  }

  public String findByDsId(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("f.*, t.name table_name, t.origin_name table_org_name");
        FROM(joinTableName);
        JOIN("\"table\" t on (f.table_id = t.id)");
        WHERE("t.ds_id = #{id}");
      }
    }.toString();
  }

  public String selectById(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("f.*, t.name table_name");
        FROM(joinTableName);
        JOIN("\"table\" t on (f.table_id = t.id)");
        WHERE("f.id = #{id}");
      }
    }.toString();
  }

  public String findUsedById(Map<String, Object> parameter) {
    return new SQL() {
      {
        SELECT("count(1)");
        FROM("\"field\" f ");
        LEFT_OUTER_JOIN("chart_field cf ON(f.id = cf.field_key)");
        LEFT_OUTER_JOIN("chart_inner_filter cif ON(f.id = cif.field_key)");
        LEFT_OUTER_JOIN("dash_filter df ON(f.id = df.field_key)");
        LEFT_OUTER_JOIN("dash_link dl ON(f.id = dl.field_key)");
        WHERE("f.id = #{id} ");
        WHERE("(cf.field_key is not null or cif.field_key is not null or df.field_key is not null or dl.field_key is not null)");
      }
    }.toString();
  }

  public String selectByIds(Map<String, Object> parameter) {
    Set<String> ids = (Set<String>) parameter.get("ids");

    Joiner joiner = Joiner.on("','");

    return new SQL() {
      {
        SELECT("f.*, t.name table_name");
        FROM(joinTableName);
        JOIN("\"table\" t on (f.table_id = t.id)");
        WHERE("f.id in ('" + joiner.join(ids) + "')");
      }
    }.toString();
  }

  public String findByTId(@Param("tId") String tId) {
    return new SQL() {
      {
        SELECT("*");
        FROM(TABLE_NAME);
        WHERE("table_id = #{tId}");
        ORDER_BY("sort_id ASC");
        ORDER_BY("create_time ASC");
      }
    }.toString();
  }

  public String findDelByTId(@Param("tId") String tId) {
    return new SQL() {
      {
        SELECT("t1.*");
        FROM("\"field\" t1 left join chart_field t2 on (t1.id = t2.field_key)");
        WHERE("t1.table_id = #{tId}");
        WHERE("deleted = true");
      }
    }.toString();
  }

  public String selectChartFieldByUser() {
    return new SQL() {
      {
        SELECT("t2.*");
        FROM("chart_field t1 join \"field\" t2 on (t1.field_key = t2.id)");
        WHERE("t2.owner = #{userId}");
      }
    }.toString();
  }

  public String findByTIds(@Param("tIds") Set<String> tIds) {

    return new SQL() {
      {
        SELECT("f.*,t.name as table_name");
        FROM(TABLE_NAME + " AS f");
        JOIN("\"table\" as t on f.table_id=t.id");
        WHERE("f.table_id in ('"+tIds.stream().collect(Collectors.joining("','"))+"')");
        ORDER_BY("f.sort_id ASC");
        ORDER_BY("f.create_time ASC");
      }
    }.toString();
  }

  public String deleteByDsId() {
    return "DELETE FROM field f WHERE f.table_id IN ( SELECT ID FROM \"table\" WHERE ds_id = #{dsId} )";
  }

  public String deleteById() {
    return new SQL() {
      {
        DELETE_FROM(TABLE_NAME);
        WHERE("id = #{id}");
      }
    }.toString();
  }
}
