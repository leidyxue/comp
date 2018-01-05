package com.baifendian.comp.dao.postgresql.mapper.comparison;

import com.baifendian.comp.common.enums.ScheduleStatus;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.comp.dao.utils.JsonFieldUtil;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
public class ScheduleMapperProvider {
  private static String tableName = "\"comp_schedule\"";
  /**
   * 插入一条比对任务
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("task_id", "#{comparisonSchedule.taskId}");
        VALUES("create_time", "#{comparisonSchedule.createTime}");
        VALUES("warn_email", "#{comparisonSchedule.warnEmail}");
        VALUES("status", EnumFieldUtil
            .genFieldStr("comparisonSchedule.status", ScheduleStatus.class, "schedule_status"));
        VALUES("crontab", JsonFieldUtil.genFieldStr("comparisonSchedule.crontab"));
      }
    }.toString();
  }

  public String update(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(tableName);
        SET("status = " + EnumFieldUtil.genFieldStr("comparisonSchedule.status", ScheduleStatus.class, "schedule_status"));
        SET("crontab = " + JsonFieldUtil.genFieldStr("comparisonSchedule.crontab"));
        SET("warn_email = #{comparisonSchedule.warnEmail}");
        WHERE("task_id = #{comparisonSchedule.taskId}");
      }
    }.toString();
  }

  public String findById(Map<String, Object> parameter) {
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
