package com.baifendian.comp.dao.postgresql.mapper.comparison;

import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonSchedule;
import com.baifendian.comp.dao.utils.JsonTypeHandler;
import java.util.Date;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
@MapperScan
public interface ScheduleMapper {
  @InsertProvider(type = ScheduleMapperProvider.class, method = "insert")
  int insert(@Param("comparisonSchedule") ComparisonSchedule comparisonSchedule);

  @UpdateProvider(type = ScheduleMapperProvider.class, method = "update")
  int update(@Param("comparisonSchedule") ComparisonSchedule comparisonSchedule);

  @Results(value = {
      @Result(property = "taskId", column = "task_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "status", column = "status", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "warnEmail", column = "warn_email", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "crontab", column = "crontab", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = ScheduleMapperProvider.class, method = "findById")
  ComparisonSchedule findById(@Param("taskId") String taskId);
}
