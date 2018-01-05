package com.baifendian.comp.dao.jdbc;


import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCObjectFactory extends BaseKeyedPooledObjectFactory {

  private static final String MYSQL_JDBC_TIMEOUT = "connectTimeout=1000";
  private static final String POSTGRESQL_JDBC_TIMEOUT = "connectTimeout=1000";
  private final Logger logger = LoggerFactory.getLogger(getClass());

  static String getHead(DSType type) {
    switch (type) {
      case MYSQL:
        return "jdbc:mysql";

      case POSTGRESQL:
        return "jdbc:postgresql";

      case KYLIN:
        return "jdbc:kylin";
    }

    throw new RuntimeException("error type.");
  }

  static private String getSqlDriver(DSType type) {
    switch (type) {
      case MYSQL:
        return "com.mysql.jdbc.Driver";

      case POSTGRESQL:
        return "org.postgresql.Driver";

      case KYLIN:
        return "org.apache.kylin.jdbc.Driver";
    }

    throw new RuntimeException("error type.");
  }

  static public String getUrl(JDBCParam jdbcParam) {
    if (jdbcParam.getType() == DSType.KYLIN) {
      return MessageFormat
          .format("{0}://{1}/{2}", getHead(jdbcParam.getType()), jdbcParam.getAddress(),
              jdbcParam.getDatabase());
    }
    String jdbcExtend = jdbcParam.getExtendParams();
    String timeout = DSType.POSTGRESQL == jdbcParam.getType()? POSTGRESQL_JDBC_TIMEOUT :MYSQL_JDBC_TIMEOUT;
    if (StringUtils.isEmpty(jdbcExtend)){

      jdbcExtend = timeout;
    }else{
      jdbcExtend += "&" + timeout;
    }
    return MessageFormat
        .format("{0}://{1}/{2}?{3}", getHead(jdbcParam.getType()), jdbcParam.getAddress(),
            jdbcParam.getDatabase(), jdbcExtend);
  }

  private final static String FUNCTION = "CREATE or REPLACE function bi_date_func(text) returns timestamp language plpgsql immutable as $$ begin return  $1::timestamp ; exception when others then return null; end;$$";
  private final static String FUNCTION_2 = "CREATE or REPLACE function bi_num_func(text) returns numeric language plpgsql immutable as $$ begin return  $1::numeric ; exception when others then return null; end;$$";

  /**
   * 构建底层对象
   */
  @Override
  public Object create(Object o) throws Exception {
    if (o != null) {
      JDBCParam jdbcParam = (JDBCParam) o;
      Driver driver = (Driver) Class.forName(getSqlDriver(jdbcParam.getType())).newInstance();
      Properties info = new Properties();
      info.put("user", jdbcParam.getUser());
      info.put("password", jdbcParam.getPassword());
      try {
        Connection connection = driver.connect(getUrl(jdbcParam), info);
        if (jdbcParam.getType() == DSType.POSTGRESQL) {
          connection.createStatement().execute(FUNCTION);
          connection.createStatement().execute(FUNCTION_2);
        }

        return connection;
      } catch (SQLException e) {
        throw JdbcUtil.exceptionParse(e, jdbcParam.getType());
      }

    }
    return null;
  }

  /**
   * 池化底层对象
   */
  @Override
  public PooledObject wrap(Object o) {
    return new DefaultPooledObject<>((Connection) o);
  }

  /**
   * 销毁一个对象
   */
  @Override
  public void destroyObject(Object key, PooledObject p) throws Exception {
    DefaultPooledObject defaultPooledObject = (DefaultPooledObject) p;
    Connection connection = (Connection) defaultPooledObject.getObject();
    connection.close();
  }

  /**
   * 验证对象活性
   */
  @Override
  public boolean validateObject(Object key, PooledObject p) {
    DefaultPooledObject defaultPooledObject = (DefaultPooledObject) p;
    Connection connection = (Connection) defaultPooledObject.getObject();
    try {
      boolean isClose = connection.isClosed();
      return !isClose;
    } catch (SQLException e) {
      logger.error("Connection exception", e);
    }

    return false;
  }
}
