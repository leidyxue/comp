package com.baifendian.comp.dao.postgresql.mapper.chart;

import com.baifendian.comp.dao.postgresql.model.chart.InnerFilter;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

public interface InnerFilterMapper {
  /**
   * @see InnerFilterMapperProvider#deleteByDash
   */
  @DeleteProvider(type = InnerFilterMapperProvider.class, method = "deleteByDash")
  int deleteByDash(@Param("dashId") String dashId);

  /**
   * @see InnerFilterMapperProvider#deleteByChart
   */
  @DeleteProvider(type = InnerFilterMapperProvider.class, method = "deleteByChart")
  int deleteByChart(@Param("chartId") String chartId);

  /**
   * @see InnerFilterMapperProvider#batchInsert(Map)
   */
  @InsertProvider(type = InnerFilterMapperProvider.class, method = "batchInsert")
  int batchInsert(@Param("list") List<InnerFilter> innerFilters);

  /**
   * @see InnerFilterMapperProvider#findByChartId(Map)
   */
  @Results(value = {
      @Result(property = "fieldId", column = "field_key", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "chartId", column = "chart_Id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = InnerFilterMapperProvider.class, method = "findByChartId")
  List<InnerFilter> findByChartId(@Param("chartId") String chartId);
}
