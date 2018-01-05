package com.baifendian.comp.common.structs.datasource;

import com.baifendian.comp.common.annotation.ParamProperty;
import com.baifendian.comp.common.enums.DSType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Setter
@Getter
public class JDBCParam  {

  /**
   * jdbc的连接类型
   */
  private DSType type;
  /**
   * 数据库地址
   */
  private String address;
  /**
   * 数据库名称
   */
  @ParamProperty(maxLen = 128, name = {"com.bfd.bi.api.common.db"})
  private String database;
  /**
   * 数据库用户
   */
  @ParamProperty(maxLen = 128, name = {"com.bfd.bi.api.common.userName"})
  private String user;
  /**
   * 数据库密码
   */
  @ParamProperty(maxLen = 128, name = {"com.bfd.bi.api.common.pwd"})
  private String password;
  /**
   * 额外参数
   */
  private String extendParams = "";

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JDBCParam jdbcParam = (JDBCParam) o;

    return new org.apache.commons.lang3.builder.EqualsBuilder()
        .append(type, jdbcParam.type)
        .append(address, jdbcParam.address)
        .append(database, jdbcParam.database)
        .append(user, jdbcParam.user)
        .append(password, jdbcParam.password)
        .append(extendParams, jdbcParam.extendParams)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(type)
        .append(address)
        .append(database)
        .append(user)
        .append(password)
        .append(extendParams)
        .toHashCode();
  }
}
