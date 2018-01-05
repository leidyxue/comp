package com.baifendian.comp.dao.postgresql.mapper.comparison;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.enums.ExecStatus;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import java.util.Date;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
public class TaskMapperProvider {
  private static String tableName = "\"comp_task\"";
  /**
   * 插入一条比对任务
   */
  public String insert(Map<String, Object> parameter) {
    return new SQL() {
      {
        INSERT_INTO(tableName);
        VALUES("id", "#{comparisonTask.id}");
        VALUES("name", "#{comparisonTask.name}");
        VALUES("create_time", "#{comparisonTask.createTime}");
        VALUES("modify_time", "#{comparisonTask.modifyTime}");
        VALUES("owner", "#{comparisonTask.owner}");
      }
    }.toString();
  }

  public String totalCount(Map<String, Object> parameter) {
    String keyWord = (String) parameter.get("keyWord");
    ExecStatus status = (ExecStatus) parameter.get("status");
    Date startDate = (Date) parameter.get("startDate");
    Date endDate = (Date) parameter.get("endDate");
    String sql = new SQL() {
      {
        SELECT("count(*)");
        FROM(tableName + " as t LEFT JOIN  "
            + "(SELECT * from ( SELECT "
            + "*, "
            + "RANK () OVER ( "
            + "PARTITION BY task_id "
            + "ORDER BY "
            + "exec_time DESC "
            + ") tmp  "
            + "FROM "
            + "comp_result "
            + ") t "
            + "where tmp = 1 ) r "
            + "ON t.id=r.task_id");
        WHERE("owner = #{userId} ");
        if (keyWord != null){
          WHERE("\"name\" ~ #{keyWord}");
        }
        if (status != null){
          if (status.equals(ExecStatus.NOT_RUN)){
            WHERE("status is null");
          }else {
            WHERE("status =" + EnumFieldUtil.genFieldStr("status", ExecStatus.class, "status_type"));
          }
        }
        if (startDate != null){
          WHERE("create_time> #{startDate}");
        }
        if (endDate != null){
          WHERE("create_time< #{endDate}");
        }
      }
    }.toString();
    return sql;
  }

  public String update(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(tableName);
        SET("name = #{comparisonTask.name}");
        SET("modify_time = #{comparisonTask.modifyTime}");
        WHERE("id = #{comparisonTask.id}");
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
        FROM(tableName);
        WHERE("id = #{id}");
      }
    }.toString();
    return sql;
  }

  public String findAll(Map<String, Object> parameter) {
    String keyWord = (String) parameter.get("keyWord");
    ExecStatus status = (ExecStatus) parameter.get("status");
    String timeOrder = (String) parameter.get("timeOrder");
    Date startDate = (Date) parameter.get("startDate");
    Date endDate = (Date) parameter.get("endDate");
    String sql = new SQL() {
      {
        SELECT("*");
        FROM(tableName + " as t LEFT JOIN  "
            + "(SELECT * from ( SELECT "
            + "*, "
            + "RANK () OVER ( "
            + "PARTITION BY task_id "
            + "ORDER BY "
            + "exec_time DESC "
            + ") tmp  "
            + "FROM "
            + "comp_result "
            + ") t "
            + "where tmp = 1 ) r "
            + "ON t.id=r.task_id");
        WHERE("owner = #{userId} ");
        if (keyWord != null){
          WHERE("\"name\" ~ '" + keyWord + "'");
        }
        if (status != null){
          if (status.equals(ExecStatus.NOT_RUN)){
            WHERE("status is null");
          }else {
            WHERE("status =" + EnumFieldUtil.genFieldStr("status", ExecStatus.class, "status_type"));
          }
        }
        if (startDate != null){
          WHERE("create_time> #{startDate}");
        }
        if (endDate != null){
          WHERE("create_time< #{endDate}");
        }

        if (timeOrder != null && timeOrder.equals("ASC")){
          ORDER_BY("create_time ASC");
        }else {
          ORDER_BY("create_time DESC");
        }

      }
    }.toString();
    return sql + " limit #{pageSize} OFFSET #{start}";
  }

  public String findByTableId(Map<String, Object> parameter) {
    String sql = new SQL() {
      {
        SELECT("DISTINCT t.*");
        FROM(tableName + " AS t");
        JOIN("com_node n ON t.id=n.task_id");
        WHERE("n.table_id = #{tableId}");
        ORDER_BY("t.create_time DESC");
      }
    }.toString();
    return sql;
  }

}
