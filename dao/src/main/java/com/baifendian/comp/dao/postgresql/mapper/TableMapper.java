package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.common.enums.TableType;
import com.baifendian.comp.common.structs.table.TableOld;
import com.baifendian.comp.dao.utils.JsonTypeHandler;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

public interface TableMapper {




  /**
   * @see TableMapperProvider#selectById
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originName", column = "origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      //@Result(property = "parameter", column = "jdbc_parameter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "jdbcParameter", column = "jdbc_parameter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsId", column = "ds_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parentId", column = "parent_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsName", column = "ds_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsType", column = "ds_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
//      @Result(property = "schema", column = "id", javaType = List.class, many = @Many(select = "FieldMapper.findByTId"))
  })
  @SelectProvider(type = TableMapperProvider.class, method = "selectById")
  TableOld selectById(@Param("tId") String tId);

  /**
   * @see TableMapperProvider#selectByIds
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originName", column = "origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      //@Result(property = "parameter", column = "parameter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "jdbcParameter", column = "jdbc_parameter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsId", column = "ds_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parentId", column = "parent_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsName", column = "ds_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsType", column = "ds_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      //@Result(property = "schema", column = "id", javaType = List.class, many = @Many(select = "FieldMapper.findByTId"))
  })
  @SelectProvider(type = TableMapperProvider.class, method = "selectByIds")
  List<TableOld> selectByIds(@Param("ids") Set<String> ids);

  /**
   * @see TableMapperProvider#countByFolder
   */
  @SelectProvider(type = TableMapperProvider.class, method = "countByFolder")
  int countByFolder(@Param("parentId") String parentId);

  /**
   * @see TableMapperProvider#selectByUser
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originName", column = "origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "jdbcParameter", column = "jdbc_parameter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parentId", column = "parent_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsId", column = "ds_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsName", column = "ds_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsType", column = "ds_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      //@Result(property = "schema", column = "id", javaType = List.class, many = @Many(select = "FieldMapper.findByTId"))
  })
  @SelectProvider(type = TableMapperProvider.class, method = "selectByUser")
  List<TableOld> selectByUser(@Param("userId") String userId);

  /**
   * @see TableMapperProvider#selectByChartIds
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originName", column = "origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "jdbcParameter", column = "jdbc_parameter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parentId", column = "parent_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsId", column = "ds_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsName", column = "ds_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsType", column = "ds_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "schema", column = "id", javaType = List.class, many = @Many(select = "FieldMapper.findByTId"))
  })
  @SelectProvider(type = TableMapperProvider.class, method = "selectByChartIds")
  List<TableOld> selectByChartIds(@Param("chartIds") Set<String> chartIds);
  /**
   * @see TableMapperProvider#selectNumByTId
   */
  @SelectProvider(type = TableMapperProvider.class, method = "selectNumByTId")
  int selectNumByTId(@Param("tId") String tId);

  /**
   * @see TableMapperProvider#deleteByDsId
   */
  @DeleteProvider(type = TableMapperProvider.class, method = "deleteByDsId")
  int deleteByDsId(@Param("userId") String userId, @Param("dsId") String dsId);

  /**
   * @see TableMapperProvider#deleteByType
   * */
  @DeleteProvider(type = TableMapperProvider.class, method = "deleteByType")
  int deleteByType(@Param("type")TableType type, @Param("userId") String userId);
  /**
   * @see TableMapperProvider#deleteByTableId
   */
  @DeleteProvider(type = TableMapperProvider.class, method = "deleteByTableId")
  int deleteByTableId(@Param("tId") String tId);

  /**
   * @see TableMapperProvider#updateById
   */
  @UpdateProvider(type = TableMapperProvider.class, method = "updateById")
  int updateById(@Param("table") TableOld table);
}
