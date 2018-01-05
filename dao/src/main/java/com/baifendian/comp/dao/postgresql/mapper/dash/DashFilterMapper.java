package com.baifendian.comp.dao.postgresql.mapper.dash;

import com.baifendian.comp.dao.postgresql.model.dash.DashFilter;
import java.util.List;
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
public interface DashFilterMapper {
  /**
   * @see DashFilterMapperProvider#deleteByDash
   */
  @DeleteProvider(type = DashFilterMapperProvider.class, method = "deleteByDash")
  int deleteByDash(@Param("dashId") String dashId);

  /**
   * @see DashFilterMapperProvider#deleteByChart
   */
  @DeleteProvider(type = DashFilterMapperProvider.class, method = "deleteByChart")
  int deleteByChart(@Param("chartId") String chartId);

  /**
   * @see DashFilterMapperProvider#insert
   */
  @InsertProvider(type = DashFilterMapperProvider.class, method = "insert")
  int insert(@Param("dashFilter") DashFilter dashFilter);

  /**
   * @see DashFilterMapperProvider#queryByDashId
   * */
  @Results(value = {
      @Result(property = "dashId", column = "dash_id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "id", column = "id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "fieldType", column = "field_Type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableId", column = "table_Id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "fieldId", column = "field_key", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "chartId", column = "chart_Id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DashFilterMapperProvider.class, method = "queryByDashId")
  List<DashFilter> queryByDashId(@Param("dashId") String dashId);
}
