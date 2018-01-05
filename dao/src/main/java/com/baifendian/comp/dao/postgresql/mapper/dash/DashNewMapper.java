package com.baifendian.comp.dao.postgresql.mapper.dash;

import com.baifendian.comp.dao.postgresql.model.dash.Dashboard;
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
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface DashNewMapper {

  /**
   * @see DashMapperProvider#insert(Map)
   */
  @InsertProvider(type = DashMapperProvider.class, method = "insert")
  int insert(@Param("dashboard") Dashboard dashboard);

  /**
   * @see DashMapperProvider#findById(Map)
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "webData", column = "web_data", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "projectId", column = "project_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DashMapperProvider.class, method = "findById")
  Dashboard findById(@Param("id") String id);

  /**
   * @see DashMapperProvider#findByOwner(Map)
   * */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "meta", column = "meta", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "projectId", column = "project_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DashMapperProvider.class, method = "findByOwner")
  List<Dashboard> findByOwner(@Param("owner") String owner);

  /**
   * @see DashMapperProvider#update(Map)
   */
  @UpdateProvider(type = DashMapperProvider.class, method = "update")
  int update(@Param("dashboard") Dashboard dashboard);

  /**
   * @see DashMapperProvider#deleteById(Map)
   */
  @DeleteProvider(type = DashMapperProvider.class, method = "deleteById")
  int deleteById(@Param("id") String id);

  /**
   * @see DashMapperProvider#findByProjectId(Map)
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "meta", column = "meta", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_Time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "projectId", column = "project_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DashMapperProvider.class, method = "findByProjectId")
  List<Dashboard> findByProjectId(@Param("projectId") String id);
}
