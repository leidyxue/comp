package com.baifendian.comp.dao.postgresql.mapper.chart;

import com.baifendian.comp.dao.postgresql.model.chart.Chart;
import com.baifendian.comp.dao.utils.JsonTypeHandler;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

@MapperScan
public interface ChartNewMapper {

  /**
   * @see ChartMapperProvider#insert(Map)
   */
  @InsertProvider(type = ChartMapperProvider.class, method = "insert")
  int insert(@Param("chart") Chart chart);

  /**
   * @see ChartMapperProvider#update(Map)
   */
  @UpdateProvider(type = ChartMapperProvider.class, method = "update")
  int update(@Param("chart") Chart chart);

  /**
   * @see ChartMapperProvider#deleteById(Map)
   */
  @DeleteProvider(type = ChartMapperProvider.class, method = "deleteById")
  int deleteById(@Param("id") String id);

  /**
   * @see ChartMapperProvider#findByDId(Map)
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableId", column = "table_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableName", column = "table_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dashId", column = "dash_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dashName", column = "dash_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "projectId", column = "project_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "projectName", column = "project_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "webData", column = "web_data", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_Time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "filterExp", column = "filter_exp", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "chartType", column = "chart_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = ChartMapperProvider.class, method = "findByDId")
  List<Chart> findByDId(@Param("dashId") String dashId);

  /**
   * @see ChartMapperProvider#findByTId(Map)
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableId", column = "table_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableName", column = "table_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dashId", column = "dash_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dashName", column = "dash_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "projectId", column = "project_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "projectName", column = "project_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "webData", column = "web_data", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "createTime", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "filterExp", column = "filter_exp", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "chartType", column = "chart_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = ChartMapperProvider.class, method = "findByTId")
  List<Chart> findByTId(@Param("tableId") String tableId);

  /**
   * @see ChartMapperProvider#findById(Map)
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableId", column = "table_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableName", column = "table_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dashId", column = "dash_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dashName", column = "dash_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "projectId", column = "project_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "projectName", column = "project_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "webData", column = "web_data", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "createTime", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "filterExp", column = "filter_exp", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "chartType", column = "chart_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = ChartMapperProvider.class, method = "findById")
  Chart findById(@Param("id") String id);
}
