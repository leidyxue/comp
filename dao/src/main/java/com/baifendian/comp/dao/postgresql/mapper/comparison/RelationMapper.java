package com.baifendian.comp.dao.postgresql.mapper.comparison;

import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonsRelation;
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
public interface RelationMapper {
  @InsertProvider(type = RelationMapperProvider.class, method = "insert")
  int insert(@Param("comparisonsRelation") ComparisonsRelation comparisonsRelation);

  @UpdateProvider(type = RelationMapperProvider.class, method = "update")
  int update(@Param("comparisonsRelation") ComparisonsRelation comparisonsRelation);

  @DeleteProvider(type = RelationMapperProvider.class, method = "deleteByResultId")
  int deleteByResultId(@Param("resultId") String resultId);

  @DeleteProvider(type = RelationMapperProvider.class, method = "deleteByTaskId")
  int deleteByTaskId(@Param("taskId") String taskId);

  @Results(value = {
      @Result(property = "resultId", column = "result_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "taskId", column = "task_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "leftId", column = "left_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "rightId", column = "right_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "compType", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "conditions", column = "conditions", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "filter", column = "filter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "selectedLeftFields", column = "left_cols", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "selectedRightFields", column = "right_cols", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = RelationMapperProvider.class, method = "findByResultId")
  ComparisonsRelation findByResultId(@Param("resultId") String resultId);

  @Results(value = {
      @Result(property = "resultId", column = "result_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "taskId", column = "task_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "leftId", column = "left_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "rightId", column = "right_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "compType", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "conditions", column = "conditions", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "filter", column = "filter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "selectedLeftFields", column = "left_cols", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "selectedRightFields", column = "right_cols", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = RelationMapperProvider.class, method = "findAll")
  List<ComparisonsRelation> findAll(@Param("taskId") String taskId);
}
