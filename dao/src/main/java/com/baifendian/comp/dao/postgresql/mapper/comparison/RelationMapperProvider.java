package com.baifendian.comp.dao.postgresql.mapper.comparison;

import com.baifendian.comp.common.enums.CompType;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.comp.dao.utils.JsonFieldUtil;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
public class RelationMapperProvider {
  private static String tableName = "\"comp_relation\"";
  /**
   * 插入一条节点关系
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("result_id", "#{comparisonsRelation.resultId}");
        VALUES("task_id", "#{comparisonsRelation.taskId}");
        VALUES("left_id", "#{comparisonsRelation.leftId}");
        VALUES("right_id", "#{comparisonsRelation.rightId}");
        VALUES("type", EnumFieldUtil.genFieldStr("comparisonsRelation.compType", CompType.class, "comp_type"));
        VALUES("conditions", JsonFieldUtil.genFieldStr("comparisonsRelation.conditions"));
        VALUES("filter", JsonFieldUtil.genFieldStr("comparisonsRelation.filter"));
        VALUES("left_cols", JsonFieldUtil.genFieldStr("comparisonsRelation.selectedLeftFields"));
        VALUES("right_cols", JsonFieldUtil.genFieldStr("comparisonsRelation.selectedRightFields"));
      }
    }.toString();
  }

  public String update(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(tableName);
        SET("left_id = #{comparisonsRelation.leftId}");
        SET("right_id = #{comparisonsRelation.rightId}");
        SET("comp_type = " + EnumFieldUtil.genFieldStr("comparisonsRelation.compType", CompType.class, "comp_type"));
        SET("conditions = " + JsonFieldUtil.genFieldStr("comparisonsRelation.conditions"));
        SET("filter = " + JsonFieldUtil.genFieldStr("comparisonsRelation.filter"));
//        SET("type = "+ EnumFieldUtil.genFieldStr("comparisonNode.type", CompNodeType.class, "node_type"));
        SET("left_cols = " + JsonFieldUtil.genFieldStr("comparisonsRelation.selectedLeftFields"));
        SET("right_cols = " + JsonFieldUtil.genFieldStr("comparisonsRelation.selectedRightFields"));
        WHERE("result_id = #{comparisonsRelation.resultId}");
      }
    }.toString();
  }

  public String deleteByResultId(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(tableName);
        WHERE("resultId = #{resultId}");
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

  public String findByResultId(Map<String, Object> parameter) {
    String sql = new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE("resultId = #{resultId}");
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
