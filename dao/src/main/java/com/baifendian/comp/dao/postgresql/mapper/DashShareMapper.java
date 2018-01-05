package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.dao.postgresql.model.share.ShareDash;
import com.baifendian.comp.dao.utils.JsonTypeHandler;
import java.util.Date;
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
public interface DashShareMapper {

  /**
   * @see DashShareMapperProvider#insert
   */
  @InsertProvider(type = DashShareMapperProvider.class, method = "insert")
  int insert(@Param("shareDash") ShareDash shareDash);

  /**
   * @see DashShareMapperProvider#deleteShare()
   */
  @DeleteProvider(type = DashShareMapperProvider.class, method = "deleteShare")
  int delete(@Param("shareId") String shareId);

  /**
   * @see DashShareMapperProvider#queryByDashId
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tag", column = "tag", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "meta", column = "meta", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dashId", column = "dash_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
  })
  @SelectProvider(type = DashShareMapperProvider.class, method = "queryByDashId")
  List<ShareDash> queryByDashId(@Param("dashId") String dashId);

  /**
   * @see DashShareMapperProvider#queryById
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "tag", column = "tag", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "meta", column = "meta", typeHandler = JsonTypeHandler.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "desc", column = "desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "dashId", column = "dash_id", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
  })
  @SelectProvider(type = DashShareMapperProvider.class, method = "queryById")
  ShareDash queryById(@Param("shareId") String shareId);

}
