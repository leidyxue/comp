package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.common.structs.folder.Folder;
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
public interface FolderMapper {

  /**
   * @see FolderMapperProvider#insert(Map)
   */
  @InsertProvider(type = FolderMapperProvider.class, method = "insert")
  int insert(@Param("folder") Folder folder);

  /**
   * @see FolderMapperProvider#updateById(Map)
   */
  @UpdateProvider(type = FolderMapperProvider.class, method = "updateById")
  int updateById(@Param("folder") Folder folder);

  /**
   * @see FolderMapperProvider#deleteById(Map)
   */
  @DeleteProvider(type = FolderMapperProvider.class, method = "deleteById")
  int deleteById(@Param("id") String id);
  /**
   * @see FolderMapperProvider#deleteByName(Map)
   */
  @DeleteProvider(type = FolderMapperProvider.class, method = "deleteByName")
  int deleteByName(@Param("name") String name);


  /**
   * @see FolderMapperProvider#selectById(Map)
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parentId", column = "parent_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
  })
  @SelectProvider(type = FolderMapperProvider.class, method = "selectById")
  Folder selectById(@Param("id") String id);

  /**
   * @see FolderMapperProvider#selectByName(Map)
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parentId", column = "parent_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
  })
  @SelectProvider(type = FolderMapperProvider.class, method = "selectByName")
  Folder selectByName(@Param("user") String user, @Param("name") String name);

  /**
   * @see FolderMapperProvider#selectByUser(Map)
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "parentId", column = "parent_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
      @Result(property = "modifyTime", column = "modify_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
  })
  @SelectProvider(type = FolderMapperProvider.class, method = "selectByUser")
  List<Folder> selectByUser(@Param("userId") String userId);
}
