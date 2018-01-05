package com.baifendian.comp.dao.utils;

import com.baifendian.comp.common.utils.json.JsonUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class JsonListHandler <E extends Object> extends BaseTypeHandler<List<E>> {

  private final Class<E> type;

  public JsonListHandler(Class<E> type) {
    if (type == null) {
      throw new IllegalArgumentException("Type argument cannot be null");
    } else {
      this.type = type;
    }
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, List<E> parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setObject(i, JsonUtil.toJsonString(parameter));
  }

  @Override
  public List<E> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String s = rs.getString(columnName);

    try {
      return JsonUtil.parseList(s, this.type);
    } catch (Exception e) {
      throw new SQLException(e.getCause());
    }
  }

  @Override
  public List<E> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String s = rs.getString(columnIndex);

    try {
      return JsonUtil.parseList(s, this.type);
    } catch (Exception e) {
      throw new SQLException(e.getCause());
    }
  }

  @Override
  public List<E> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String s = cs.getString(columnIndex);

    try {
      return JsonUtil.parseList(s, this.type);
    } catch (Exception e) {
      throw new SQLException(e.getCause());
    }
  }
}
