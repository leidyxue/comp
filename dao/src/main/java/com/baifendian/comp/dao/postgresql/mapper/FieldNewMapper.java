package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.dao.postgresql.model.table.Field;
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

public interface FieldNewMapper {

  /**
   * @see FieldMapperProvider#batchInsert(Map)
   */
  @InsertProvider(type = FieldMapperProvider.class, method = "batchInsert")
  int batchInsert(@Param("list") List<Field> field);

  /**
   * @see FieldMapperProvider#insert(Map)
   */
  @InsertProvider(type = FieldMapperProvider.class, method = "insert")
  int insert(@Param("field") Field field);

  /**
   * @see FieldMapperProvider#deleteById
   */
  @DeleteProvider(type = FieldMapperProvider.class, method = "deleteById")
  int deleteById(@Param("id") String id);

  /**
   * @see FieldMapperProvider#updateById(Map)
   */
  @UpdateProvider(type = FieldMapperProvider.class, method = "updateById")
  int updateById(@Param("field") Field field);

  /**
   * @see FieldMapperProvider#updateDeleteById(Map)
   */
  @UpdateProvider(type = FieldMapperProvider.class, method = "updateDeleteById")
  int updateDeleteById(@Param("fieldId") String fieldId);

  /**
   * @see FieldMapperProvider#findDelByTId
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
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "findDelByTId")
  List<Field> findDelByTId(@Param("tId") String tId);

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
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "findByTId")
  List<Field> findByTId(@Param("tId") String tId);

  /**
   * @see FieldMapperProvider#findByDsId(Map)
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
      @Result(property = "tableOrgName", column = "table_org_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "findByDsId")
  List<Field> findByDsId(@Param("id") String id);

  /**
   * @see FieldMapperProvider#findByTIds
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
      @Result(property = "tableOrgName", column = "table_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "findByTIds")
  List<Field> findByTIds(@Param("tIds") Set<String> tIds);

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
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "selectByIds")
  List<Field> selectByIds(@Param("ids") Set<String> ids);

  /**
   * @see FieldMapperProvider#selectChartFieldByUser
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
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "selectChartFieldByUser")
  List<Field> selectChartFieldByUser(@Param("userId") String userId);

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
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = FieldMapperProvider.class, method = "selectById")
  Field selectById(@Param("id") String id);

  /**
   * @see FieldMapperProvider#findUsedById(Map)
   */
  @SelectProvider(type = FieldMapperProvider.class, method = "findUsedById")
  int findUsedById(@Param("id") String id);
}
