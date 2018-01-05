package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.dao.postgresql.model.ds.DsDirRef;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface DsDirRefMapper {
  /**
   * @see DsDirRefMapperProvider#insert
   */
  @InsertProvider(type = DsDirRefMapperProvider.class, method = "insert")
  int insert(@Param("dsDirRef") DsDirRef dsDirRef);

  /**
   * @see DsDirRefMapperProvider#selectByUser(Map)
   */
  @Results(value = {
      @Result(property = "dsId", column = "ds_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dirId", column = "dir_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DsDirRefMapperProvider.class, method = "selectByUser")
  List<DsDirRef> selectByUser(@Param("userId") String userId);

  /**
   * @see DsDirRefMapperProvider#selectByDsId(Map)
   */
  @Results(value = {
      @Result(property = "dsId", column = "ds_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dirId", column = "dir_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "owner", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
  })
  @SelectProvider(type = DsDirRefMapperProvider.class, method = "selectByDsId")
  DsDirRef selectByDsId(@Param("userId") String userId, @Param("dsId") String dsId);

  /**
   * @see DsDirRefMapperProvider#deleteByDsId
   */
  @InsertProvider(type = DsDirRefMapperProvider.class, method = "deleteByDsId")
  int deleteByDsId(@Param("userId")String userId, @Param("dsId") String dsId);
}
