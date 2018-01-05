package com.baifendian.comp.dao.postgresql.mapper.comparison;

import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonNode;
import com.baifendian.comp.dao.utils.JsonTypeHandler;
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
public interface NodeMapper {
  @InsertProvider(type = NodeMapperProvider.class, method = "insert")
  int insert(@Param("comparisonNode") ComparisonNode comparisonNode);

  @UpdateProvider(type = NodeMapperProvider.class, method = "update")
  int update(@Param("comparisonNode") ComparisonNode comparisonNode);

  @DeleteProvider(type = NodeMapperProvider.class, method = "deleteById")
  int deleteById(@Param("id") String id);

  @DeleteProvider(type = NodeMapperProvider.class, method = "deleteByTaskId")
  int deleteByTaskId(@Param("taskId") String taskId);

  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "taskId", column = "task_id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "position", column = "position", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = NodeMapperProvider.class, method = "findById")
  ComparisonNode findById(@Param("id") String id);

  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "taskId", column = "task_id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableId", column = "table_id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "position", column = "position", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = NodeMapperProvider.class, method = "findAll")
  List<ComparisonNode> findAll(@Param("taskId") String taskId);
}
