package com.baifendian.comp.dao.postgresql.mapper.dash;

import com.baifendian.comp.dao.postgresql.model.dash.DashElement;
import com.baifendian.comp.dao.utils.JsonTypeHandler;
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
public interface DashElementMapper {

  /**
   * @see DashElementMapperProvider#deleteByDash
   */
  @DeleteProvider(type = DashElementMapperProvider.class, method = "deleteByDash")
  int deleteByDash(@Param("dashId") String dashId);

  /**
   * @see DashElementMapperProvider#deleteByChart
   */
  // TODO add dashid
  @DeleteProvider(type = DashElementMapperProvider.class, method = "deleteByChart")
  int deleteByChart(@Param("chartId") String chartId);

  /**
   * @see DashElementMapperProvider#insert
   */
  @InsertProvider(type = DashElementMapperProvider.class, method = "insert")
  int insert(@Param("dashElement") DashElement dashElement);

  /**
   * @see DashElementMapperProvider#queryByDashId
   * */
  @Results(value = {
      @Result(property = "dashId", column = "dash_id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "id", column = "id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "x", column = "x", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
      @Result(property = "y", column = "y", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
      @Result(property = "w", column = "w", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
      @Result(property = "h", column = "h", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
      @Result(property = "tableId", column = "table_Id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "settings", column = "settings", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DashElementMapperProvider.class, method = "queryByDashId")
  List<DashElement> queryByDashId(@Param("dashId") String dashId);
}
