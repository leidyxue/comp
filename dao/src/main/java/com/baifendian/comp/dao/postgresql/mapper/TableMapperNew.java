package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.common.enums.TableType;
import com.baifendian.comp.dao.postgresql.model.table.Table;
import com.baifendian.comp.dao.utils.JsonTypeHandler;
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

public interface TableMapperNew {
  /**
   * @see TableMapperProvider#insert
   */
  @InsertProvider(type = TableMapperProvider.class, method = "insert")
  int insert(@Param("table") Table table);
  /**
   * @see TableMapperProvider#insertAll(Map)
   */
  @InsertProvider(type = TableMapperProvider.class, method = "insertAll")
  int batchInsert(@Param("list") List<Table> list);

  /**
   * @see TableMapperProvider#getUserList()
   */
  @SelectProvider(type = TableMapperProvider.class, method = "getUserList")
  List<String> getUserList();

  /**
   * @see TableMapperProvider#updateById
   */
  @UpdateProvider(type = TableMapperProvider.class, method = "updateById")
  int updateById(@Param("table") Table table);

  /**
   * @see TableMapperProvider#updateDeleteById(Map)
   */
  @UpdateProvider(type = FieldMapperProvider.class, method = "updateDeleteById")
  int updateDeleteById(@Param("tableId") String tableId);

  /**
   * @see TableMapperProvider#selectById
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originName", column = "origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "jdbcParameter", column = "jdbc_parameter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_Time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsId", column = "ds_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parentId", column = "parent_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsName", column = "ds_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsType", column = "ds_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = TableMapperProvider.class, method = "selectById")
  Table selectById(@Param("tId") String tId);

  /**
   * @see TableMapperProvider#selectByDs()
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
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = TableMapperProvider.class, method = "selectByDs")
  List<Table> selectByDs(@Param("userId")String userId, @Param("dsId") String dsId);

  /**
   * @see TableMapperProvider#selectByType
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
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = TableMapperProvider.class, method = "selectByType")
  List<Table> selectByType(@Param("userId")String userId, @Param("type") TableType type);

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
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = TableMapperProvider.class, method = "selectByUser")
  List<Table> selectByUser(@Param("userId") String userId);

  /**
   * @see TableMapperProvider#selectByIds
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
      @Result(property = "dsId", column = "ds_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parentId", column = "parent_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsName", column = "ds_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsType", column = "ds_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = TableMapperProvider.class, method = "selectByIds")
  List<Table> selectByIds(@Param("ids") Set<String> ids);

  /**
   * @see TableMapperProvider#findChartTableByUser
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "originName", column = "origin_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "jdbcParameter", column = "jdbc_parameter", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_Time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "type", column = "type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsId", column = "ds_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parentId", column = "parent_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsName", column = "ds_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dsType", column = "ds_type", typeHandler = EnumTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "deleted", column = "deleted", javaType = boolean.class, jdbcType = JdbcType.BOOLEAN),
  })
  @SelectProvider(type = TableMapperProvider.class, method = "findChartTableByUser")
  List<Table> findChartTableByUser(@Param("userId") String userId, @Param("dsId")String dsId);

  /**
   * @see TableMapperProvider#countByFolder
   */
  @SelectProvider(type = TableMapperProvider.class, method = "countByFolder")
  int countByFolder(@Param("parentId") String parentId);

  /**
   * @see TableMapperProvider#selectNumByTId
   */
  @SelectProvider(type = TableMapperProvider.class, method = "selectNumByTId")
  int selectNumByTId(@Param("tId") String tId);

  /**
   * @see TableMapperProvider#deleteByTableId
   */
  @DeleteProvider(type = TableMapperProvider.class, method = "deleteByTableId")
  int deleteByTableId(@Param("tId") String tId);

  /**
   * @see TableMapperProvider#deleteByDsId
   */
  @DeleteProvider(type = TableMapperProvider.class, method = "deleteByDsId")
  int deleteByDsId(@Param("userId") String userId, @Param("dsId") String dsId);
}
