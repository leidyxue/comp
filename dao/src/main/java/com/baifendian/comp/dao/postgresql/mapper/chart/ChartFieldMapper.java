package com.baifendian.comp.dao.postgresql.mapper.chart;

import com.baifendian.comp.dao.postgresql.model.chart.ChartField;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface ChartFieldMapper {
  /**
   * @see ChartFieldMapperProvider#deleteByDash
   */
  @DeleteProvider(type = ChartFieldMapperProvider.class, method = "deleteByDash")
  int deleteByDash(@Param("dashId") String dashId);

  /**
   * @see ChartFieldMapperProvider#batchInsert(Map)
   */
  @InsertProvider(type = ChartFieldMapperProvider.class, method = "batchInsert")
  int batchInsert(@Param("list") List<ChartField> chartFields);

//  /**
//   * @see ChartFieldMapperProvider#insert(Map)
//   */
//  @InsertProvider(type = ChartFieldMapperProvider.class, method = "insert")
//  int insert(@Param("chartField") ChartField chartField);

  /**
   * @see ChartFieldMapperProvider#findByChartId(Map)
   */
  @Results(value = {
      @Result(property = "uniqId", column = "uniq_Id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "fieldId", column = "field_key", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "alias", column = "alias", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "operator", column = "operator", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "sort", column = "sort", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "chartId", column = "chart_Id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "level", column = "level", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
      @Result(property = "order", column = "order", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "fieldType", column = "field_Type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = ChartFieldMapperProvider.class, method = "findByChartId")
  List<ChartField> findByChartId(@Param("chartId") String chartId);

  /**
   * @see ChartFieldMapperProvider#findByUser(Map)
   */
  @Results(value = {
      @Result(property = "uniqId", column = "uniq_Id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "fieldId", column = "field_key", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "alias", column = "alias", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "operator", column = "operator", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "sort", column = "sort", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "chartId", column = "chart_Id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "level", column = "level", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
      @Result(property = "order", column = "order", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "fieldType", column = "field_Type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = ChartFieldMapperProvider.class, method = "findByUser")
  List<ChartField> findByUser(@Param("userId") String userId);

  /**
   * @see ChartFieldMapperProvider#deleteByChart
   */
  @DeleteProvider(type = ChartFieldMapperProvider.class, method = "deleteByChart")
  int deleteByChart(@Param("chartId") String chartId);
}
