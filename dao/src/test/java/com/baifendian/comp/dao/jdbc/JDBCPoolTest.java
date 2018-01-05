package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import java.sql.Connection;
import org.junit.Test;

public class JDBCPoolTest {

  @Test
  public void JDBCPoolTest() {
    JDBCPool pool = JDBCPool.getInstance();
    JDBCParam jdbcParam = new JDBCParam();
    jdbcParam.setAddress("119.23.22.38:3306");
    jdbcParam.setDatabase("bi");
    jdbcParam.setType(DSType.MYSQL);
    jdbcParam.setUser("admin");
    jdbcParam.setPassword("123456");

    JDBCParam jdbcParam2 = new JDBCParam();
    jdbcParam2.setAddress("119.23.22.38:3306");
    jdbcParam2.setDatabase("bi");
    jdbcParam2.setType(DSType.MYSQL);
    jdbcParam2.setUser("admin");
    jdbcParam2.setPassword("123456");

    System.out.println(jdbcParam.equals(jdbcParam2));
    System.out.println(jdbcParam.hashCode());
    System.out.println(jdbcParam2.hashCode());
    try {
      while (true) {
        Connection connection = pool.borrowConn(jdbcParam);
        System.out.println(connection.isClosed());
        pool.returnConn(jdbcParam, connection);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
