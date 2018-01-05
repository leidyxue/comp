package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public class JDBCExecTest {
  public static void copyFromFile(Connection connection, String filePath, String tableName)
      throws SQLException, IOException {

    FileInputStream fileInputStream = null;

    try {
      CopyManager copyManager = new CopyManager((BaseConnection) connection);
      fileInputStream = new FileInputStream(filePath);
      copyManager.copyIn("COPY " + tableName + " FROM STDIN with DELIMITER ',' CSV HEADER", fileInputStream);
    } finally {
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Test
  public void JDBCPoolTest() {
    JDBCPool pool = JDBCPool.getInstance();
    JDBCParam jdbcParam = new JDBCParam();
    jdbcParam.setAddress("172.24.8.98:5432");
    jdbcParam.setDatabase("bi-storage");
    jdbcParam.setType(DSType.POSTGRESQL);
    jdbcParam.setUser("postgres");
    jdbcParam.setPassword("postgres-2017");
//    String sql = "COPY tac FROM '/opt/udp/tt.txt' with DELIMITER '  ' CSV HEADER";
//    String sql = "COPY tac FROM '/opt/udp/tt.txt' WITH DELIMITER '\t' CSV";
    try {
      Connection connection = JDBCPool.getInstance().borrowConn(jdbcParam);
      copyFromFile(connection, "F:\\file\\tac.csv", "tac");
      JDBCPool.getInstance().returnConn(jdbcParam, connection);
//      JDBCExec.dsExec(jdbcParam, sql);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
