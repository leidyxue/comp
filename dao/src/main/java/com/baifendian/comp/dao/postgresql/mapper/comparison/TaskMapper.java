package com.baifendian.comp.dao.postgresql.mapper.comparison;

import com.baifendian.comp.common.enums.ExecStatus;
import com.baifendian.comp.dao.postgresql.model.comparison.CompTaskData;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonTask;
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
 * Created by xuelei on 2017/11/13 0013.
 */
@MapperScan
public interface TaskMapper {

  @InsertProvider(type = TaskMapperProvider.class, method = "insert")
  int insert(@Param("comparisonTask") ComparisonTask comparisonTask);

  @SelectProvider(type = TaskMapperProvider.class, method = "totalCount")
  int totalCount(@Param("userId") String userId, @Param("keyWord") String keyWord, @Param("status") ExecStatus status,
      @Param("startDate") Date startDate, @Param("endDate") Date endDate);

  @UpdateProvider(type = TaskMapperProvider.class, method = "update")
  int update(@Param("comparisonTask") ComparisonTask comparisonTask);

  @DeleteProvider(type = TaskMapperProvider.class, method = "deleteById")
  int deleteById(@Param("id") String id);

  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
  })
  @SelectProvider(type = TaskMapperProvider.class, method = "findById")
  ComparisonTask findById(@Param("id") String id);

  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "execTime", column = "exec_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "finishedTime", column = "finished_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "status", column = "status", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = TaskMapperProvider.class, method = "findAll")
  List<CompTaskData> findAll(@Param("userId") String userId, @Param("start") int start,
      @Param("pageSize") int pageSize, @Param("keyWord") String keyWord, @Param("status") ExecStatus status,
      @Param("timeOrder") String timeOrder, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
  })
  @SelectProvider(type = TaskMapperProvider.class, method = "findByTableId")
  List<ComparisonTask> findByTableId(@Param("tableId") String tableId);
}
