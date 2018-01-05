package com.baifendian.comp.dao.postgresql.mapper.comparison;

import com.baifendian.comp.common.enums.ExecStatus;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.comp.dao.utils.JsonFieldUtil;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by Administrator on 2017/11/18 0018.
 */
public class ResultMapperProvider {
  private static String tableName = "\"comp_result\"";

  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("id", "#{comparisonResult.id}");
        VALUES("exec_time", "#{comparisonResult.execTime}");
        VALUES("finished_time", "#{comparisonResult.finishedTime}");
        VALUES("task_id", "#{comparisonResult.taskId}");
        VALUES("status", EnumFieldUtil
            .genFieldStr("comparisonResult.status", ExecStatus.class, "status_type"));
        VALUES("result_table", JsonFieldUtil.genFieldStr("comparisonResult.resultTable"));
      }
    }.toString();
  }

  public String update(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(tableName);
        SET("finished_time = #{comparisonResult.finishedTime}");
        SET("status = " + EnumFieldUtil.genFieldStr("comparisonResult.status", ExecStatus.class, "status_type"));
        SET("result_table = " + JsonFieldUtil.genFieldStr("comparisonResult.resultTable"));
        WHERE("id = #{comparisonResult.id}");
      }
    }.toString();
  }

  public String findById(Map<String, Object> parameter) {
    String sql = new SQL() {
      {
        SELECT("*");
        FROM(tableName);
        WHERE("id = #{resultId}");
      }
    }.toString();
    return sql;
  }

  public String findAll(Map<String, Object> parameter) {
    String sql = new SQL() {
      {
        SELECT("*");
        FROM(tableName + "where task_id = #{taskId} ORDER BY finished_time DESC");
      }
    }.toString();
    return sql;
  }

  public String deleteById(Map<String, Object> parameter) {
    return new SQL() {
      {
        DELETE_FROM(tableName);
        WHERE("id = #{id}");
      }
    }.toString();
  }
}
