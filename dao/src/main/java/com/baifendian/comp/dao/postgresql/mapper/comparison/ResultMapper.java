package com.baifendian.comp.dao.postgresql.mapper.comparison;

import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonResult;
import com.baifendian.comp.dao.utils.JsonTypeHandler;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
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
 * Created by Administrator on 2017/11/18 0018.
 */
@MapperScan
public interface ResultMapper {
  @InsertProvider(type = ResultMapperProvider.class, method = "insert")
  int insert(@Param("comparisonResult") ComparisonResult comparisonResult);

  @UpdateProvider(type = ResultMapperProvider.class, method = "update")
  int update(@Param("comparisonResult") ComparisonResult comparisonResult);

  @Results(value = {
      @Result(property = "execTime", column = "exec_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "finishedTime", column = "finished_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "taskId", column = "task_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "status", column = "status", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "resultTable", column = "result_table", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = ResultMapperProvider.class, method = "findById")
  ComparisonResult findById(@Param("resultId") String resultId);

  @Results(value = {
      @Result(property = "execTime", column = "exec_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "finishedTime", column = "finished_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "taskId", column = "task_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "status", column = "status", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "resultTable", column = "result_table", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = ResultMapperProvider.class, method = "findAll")
  List<ComparisonResult> findAll(@Param("taskId") String taskId);

  @DeleteProvider(type = ResultMapperProvider.class, method = "deleteById")
  int deleteById(@Param("id") String id);

}
