package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.structs.datasource.Datasource;
import com.baifendian.comp.common.structs.datasource.Summary;
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
public interface DatasourceMapper {

  /**
   * @see DatasourceMapperProvider#insert(Map)
   */
  @InsertProvider(type = DatasourceMapperProvider.class, method = "insert")
  int insert(@Param("datasource") Datasource datasource);

  /**
   * @see DatasourceMapperProvider#update(Map)
   */
  @UpdateProvider(type = DatasourceMapperProvider.class, method = "update")
  int update(@Param("datasource") Datasource datasource);

  /**
   * @see DatasourceMapperProvider#updateInner
   */
  @InsertProvider(type = DatasourceMapperProvider.class, method = "updateInner")
  int updateInner(@Param("datasource") Datasource datasource);

  /**
   * @see DatasourceMapperProvider#deleteById(Map)
   */
  @DeleteProvider(type = DatasourceMapperProvider.class, method = "deleteById")
  int deleteById(@Param("id") String id);

  /**
   * @see DatasourceMapperProvider#findById(Map)
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parameter", column = "parameter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "startTime", column = "start_time", javaType = Date.class, jdbcType = JdbcType.DATE),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
     // @Result(property = "dirId", column = "dir_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DatasourceMapperProvider.class, method = "findById")
  Datasource findById(@Param("id") String id);

  /**
   * @see DatasourceMapperProvider#findAll
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parameter", column = "parameter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "startTime", column = "start_time", javaType = Date.class, jdbcType = JdbcType.DATE),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DatasourceMapperProvider.class, method = "findAll")
  List<Datasource> findAll(@Param("userId") String userId, @Param("type") DSType type);

  /**
   * @see DatasourceMapperProvider#findSummary
   */
  @Results(value = {
      @Result(property = "count", column = "count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DatasourceMapperProvider.class, method = "findSummary")
  List<Summary> findSummary(@Param("userId")String userId);
}
