package com.baifendian.comp.dao.postgresql.mapper.ds;

import com.baifendian.comp.dao.postgresql.model.ds.UserPubData;
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
public interface UserPubDataMapper {
  /**
   * @see UserPubDataMapperProvider#insert
   */
  @InsertProvider(type = UserPubDataMapperProvider.class, method = "insert")
  int insert(@Param("pubData") UserPubData pubData);

  /**
   * @see UserPubDataMapperProvider#deleteById()
   */
  @DeleteProvider(type = UserPubDataMapperProvider.class, method = "deleteById")
  int deleteById(@Param("userId") String userId, @Param("pubId")String pubId);

  /**
   * @see UserPubDataMapperProvider#findByUserId
   */
  @Results(value = {
      @Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "userId", column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
      @Result(property = "createTime", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
  })
  @SelectProvider(type = UserPubDataMapperProvider.class, method = "findByUserId")
  List<UserPubData> findByUserId(@Param("userId") String userId);

}
