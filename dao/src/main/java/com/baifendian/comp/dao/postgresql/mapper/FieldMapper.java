package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.common.structs.field.FieldOld;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

public interface FieldMapper {

  /**
   * @see FieldMapperProvider#insert(Map)
   */
  @InsertProvider(type = FieldMapperProvider.class, method = "insert")
  int insert(@Param("field") FieldOld field);

  /**
   * @see FieldMapperProvider#updateById(Map)
   */
  @UpdateProvider(type = FieldMapperProvider.class, method = "updateById")
  int updateById(@Param("field") FieldOld field);

  /**
   * @see FieldMapperProvider#deleteByDsId
   */
  @DeleteProvider(type = FieldMapperProvider.class, method = "deleteByDsId")
  int deleteByDsId(@Param("dsId") String dsId);

  /**
   * @see FieldMapperProvider#deleteById
   */
  @DeleteProvider(type = FieldMapperProvider.class, method = "deleteById")
  int deleteById(@Param("id") String id);



  /**
   * @see FieldMapperProvider#findByTId
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originName", column = "origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "fileOriginName", column = "f_origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originType", column = "origin_type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "genType", column = "gen_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableId", column = "table_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableName", column = "table_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "findByTId")
  List<FieldOld> findByTId(@Param("tId") String tId);

  /**
   * @see FieldMapperProvider#findByTId
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originName", column = "origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "fileOriginName", column = "f_origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originType", column = "origin_type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "genType", column = "gen_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableId", column = "table_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableName", column = "table_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "findByTIds")
  List<FieldOld> findByTIds(@Param("tIds") Set<String> tIds);

  /**
   * @see FieldMapperProvider#selectById(Map)
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originName", column = "origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originType", column = "origin_type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "genType", column = "gen_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableId", column = "table_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableName", column = "table_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "selectById")
  FieldOld selectById(@Param("id") String id);

  /**
   * @see FieldMapperProvider#selectByIds
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originName", column = "origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originType", column = "origin_type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableId", column = "table_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tableName", column = "table_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "selectByIds")
  List<FieldOld> selectByIds(@Param("ids") Set<String> ids);
}
