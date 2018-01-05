package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.structs.datasource.JDBCParam;
import java.sql.Connection;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCPool {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 最大活跃连接数
   */
  private int maxActive = 512;

  /**
   * 链接池中最大空闲的连接数
   */
  private int maxIdle = 128;

  /**
   * 连接池中最少空闲的连接数
   */
  private int minIdle = 0;

  /**
   * 当连接池资源耗尽时，调用者最大阻塞的时间
   */
  private int maxWait = 2000;

  /**
   * 空闲链接检测线程，检测的周期，毫秒数，-1 表示关闭空闲检测
   */
  private int timeBetweenEvictionRunsMillis = 180000;

  /**
   * 空闲时是否进行连接有效性验证，如果验证失败则移除，默认为 true
   */
  private boolean testWhileIdle = true;

  private GenericKeyedObjectPool pool;

  /**
   * jdbc链接池
   */
  private static JDBCPool jdbcPool;

  private JDBCPool() {
    try {
      pool = bulidJDBCPool();
    } catch (Exception e) {
      logger.error("build client pool exception", e);
    }
  }

  /**
   * 构建单例, 初始化 hive 连接的客户端
   *
   * @return
   */
  public static JDBCPool getInstance() {
    if (jdbcPool == null) {
      synchronized (JDBCPool.class) {
        if (jdbcPool == null) {
          jdbcPool = new JDBCPool();
        }
      }
    }

    return jdbcPool;
  }

  private GenericKeyedObjectPool bulidJDBCPool() {
    GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();

    poolConfig.setMaxIdlePerKey(maxIdle);
    poolConfig.setMinIdlePerKey(minIdle);
    poolConfig.setMaxWaitMillis(maxWait);
    poolConfig.setMaxTotal(maxActive);
    poolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    poolConfig.setTestWhileIdle(testWhileIdle);
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);

//    poolConfig.maxActive = maxActive;
//    poolConfig.maxIdle = maxIdle;
//    poolConfig.minIdle = minIdle;
//    poolConfig.maxWait = maxWait;
//    poolConfig.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
//    poolConfig.testWhileIdle = testWhileIdle;
//    poolConfig.testOnBorrow = true;
//    poolConfig.testOnReturn = true;

    JDBCObjectFactory clientFactory = new JDBCObjectFactory();

    return new GenericKeyedObjectPool(clientFactory, poolConfig);
  }

  /**
   * 获取一个连接
   * @param jdbcParam
   * @return
   * @throws Exception
   */
  public Connection borrowConn(JDBCParam jdbcParam) throws Exception {
    return (Connection) pool.borrowObject(jdbcParam);
  }

  /**
   * 返还一个连接
   * @param jdbcParam
   * @param connection
   */
  public void returnConn(JDBCParam jdbcParam, Connection connection){
    if (connection != null) {
      try {
        pool.returnObject(jdbcParam, connection);
      } catch (Exception e) {
        logger.warn("HiveService2Client returnClient exception", e);
      }
    }
  }


}
