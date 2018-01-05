package com.baifendian.comp.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ResultSetFunc {

  List<Object> apply(ResultSet resultSet) throws SQLException;
}
