package com.baifendian.comp.dao.postgresql.mapper.dash;

import com.baifendian.comp.dao.postgresql.model.dash.DashLink;
import java.util.List;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface DashLinkMapper {
  /**
   * @see DashLinkMapperProvider#insert
   */
  @InsertProvider(type = DashLinkMapperProvider.class, method = "insert")
  int insert(@Param("dashLink") DashLink dashLink);

  /**
   * @see DashLinkMapperProvider#deleteByChart
   */
  @InsertProvider(type = DashLinkMapperProvider.class, method = "deleteByChart")
  int deleteByChart(@Param("chartId") String chartId);

  /**
   * @see DashLinkMapperProvider#deleteByDash
   */
  @DeleteProvider(type = DashLinkMapperProvider.class, method = "deleteByDash")
  int deleteByDash(@Param("dashId") String dashId);

  /**
   * @see DashLinkMapperProvider#queryByDashId
   * */
  @Results(value = {
      @Result(property = "dashId", column = "dash_id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "chartId", column = "chart_Id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableId", column = "table_Id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "fieldId", column = "field_key", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "linkChartId", column = "link_chart_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "linkTableId", column = "link_table_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "linkFieldId", column = "link_field_key", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DashLinkMapperProvider.class, method = "queryByDashId")
  List<DashLink> queryByDashId(@Param("dashId") String dashId);
}
