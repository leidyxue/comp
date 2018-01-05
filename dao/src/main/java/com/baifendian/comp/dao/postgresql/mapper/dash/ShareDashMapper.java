package com.baifendian.comp.dao.postgresql.mapper.dash;

import com.baifendian.comp.dao.postgresql.model.share.ShareDash;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface ShareDashMapper {

  /**
   * @see DashShareMapperProvider#insert
   */
  @InsertProvider(type = DashShareMapperProvider.class, method = "insert")
  int insert(@Param("shareDash") ShareDash shareDash);
}
