package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.exception.BiException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

public class JdbcUtil {
  public static BiException exceptionParse(SQLException sqlException, DSType dsType){
    switch (dsType){
      case POSTGRESQL:
        return postgreParse(sqlException);

      case KYLIN:
        return kylinParse(sqlException);

        default:
          return mysqlParse(sqlException);
    }
  }

  private static BiSQLException mysqlParse(SQLException sqlException){
    Throwable throwable = sqlException.getCause();
    if (throwable instanceof SocketTimeoutException || throwable instanceof NoRouteToHostException){
      throw new BiSQLException(sqlException, "com.bfd.bi.api.db.error.timeout");
    }
    if (throwable instanceof ConnectException){
      throw new BiSQLException(sqlException, "com.bfd.bi.api.db.error.timeout");
    }

    return new BiSQLException(sqlException, "com.baifendian.bi.api.datasource.connect.error");
  }

  private static BiSQLException postgreParse(SQLException sqlException){
    Throwable throwable = sqlException.getCause();
    if (throwable instanceof SocketTimeoutException || throwable instanceof NoRouteToHostException){
      throw new BiSQLException(sqlException, "com.bfd.bi.api.db.error.timeout");
    }

    if (throwable instanceof ConnectException){
      throw new BiSQLException(sqlException, "com.bfd.bi.api.db.error.timeout");
    }

    return new BiSQLException(sqlException, "com.baifendian.bi.api.datasource.connect.error");
  }

  private static BiSQLException kylinParse(SQLException sqlException){
    Throwable throwable = sqlException.getCause();
    if (throwable instanceof SocketTimeoutException || throwable instanceof NoRouteToHostException){
      throw new BiSQLException(sqlException, "com.bfd.bi.api.db.error.timeout");
    }

    return new BiSQLException(sqlException, "com.baifendian.bi.api.datasource.connect.error");
  }
}
