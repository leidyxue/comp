package com.baifendian.comp.dao.postgresql.mapper.comparison;

import com.baifendian.comp.common.enums.CompNodeType;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.comp.dao.utils.JsonFieldUtil;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
public class NodeMapperProvider {
  private static String tableName = "\"com_node\"";
  /**
   * 插入一条node
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("id", "#{comparisonNode.id}");
        VALUES("task_id", "#{comparisonNode.taskId}");
        VALUES("table_id", "#{comparisonNode.tableId}");
        VALUES("name", "#{comparisonNode.name}");
        VALUES("type", EnumFieldUtil.genFieldStr("comparisonNode.type", CompNodeType.class, "node_type"));
        VALUES("position", JsonFieldUtil.genFieldStr("comparisonNode.position"));
      }
    }.toString();
  }

  public String update(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(tableName);
        SET("name = #{comparisonNode.name}");
//        SET("type = "+ EnumFieldUtil.genFieldStr("comparisonNode.type", CompNodeType.class, "node_type"));
        SET("position = "+ JsonFieldUtil.genFieldStr("comparisonNode.position"));
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

  public String deleteByTaskId(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(tableName);
        WHERE("task_id = #{taskId}");
      }
    }.toString();
  }

  public String findById(Map<String, Object> parameter) {
    String sql = new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE("id = #{id}");
      }
    }.toString();
    return sql;
  }

  public String findAll(Map<String, Object> parameter) {
    String sql = new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE("task_id = #{taskId}");
      }
    }.toString();
    return sql;
  }

}
