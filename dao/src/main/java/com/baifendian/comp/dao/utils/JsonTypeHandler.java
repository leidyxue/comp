package com.baifendian.comp.dao.utils;

import com.baifendian.comp.common.utils.json.JsonUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class JsonTypeHandler<E extends Object> extends BaseTypeHandler<E> {

  private final Class<E> type;

  public JsonTypeHandler(Class<E> type) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    } else {
      this.type = type;
    }
  }

  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setObject(i, JsonUtil.toJsonString(parameter));
  }

  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    E res = null;
    try {
      String s = rs.getString(columnName);
      if (StringUtils.isNotEmpty(s)) {
        res = JsonUtil.parseObj(s, this.type);
      }
    } catch (SQLException e) {
      throw e;
    } catch (Exception e) {
      throw new SQLException(e.getCause());
    }
    return res;
  }

  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    E res = null;
    try {
      String s = rs.getString(columnIndex);
      if (StringUtils.isNotEmpty(s)) {
        res = JsonUtil.parseObj(s, this.type);
      }
    } catch (SQLException e) {
      throw e;
    } catch (Exception e) {
      throw new SQLException(e.getCause());
    }
    return res;
  }

  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    E res = null;
    try {
      String s = cs.getString(columnIndex);
      if (StringUtils.isNotEmpty(s)) {
        res = JsonUtil.parseObj(s, this.type);
      }
    } catch (SQLException e) {
      throw e;
    } catch (Exception e) {
      throw new SQLException(e.getCause());
    }
    return res;
  }
}
