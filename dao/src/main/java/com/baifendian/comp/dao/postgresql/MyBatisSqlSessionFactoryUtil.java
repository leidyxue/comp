/*
 * Create Author  : dsfan
 * Create Date    : 2016年10月14日
 * File Name      : MyBatisSqlSessionFactoryUtil.java
 */

package com.baifendian.comp.dao.postgresql;

import com.alibaba.druid.pool.DruidDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * MyBatis SqlSessionFactory 工具类
 * <p>
 *
 * @author : dsfan
 * @date : 2016年10月14日
 */
public class MyBatisSqlSessionFactoryUtil {

  /**
   * LOGGER
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisSqlSessionFactoryUtil.class);

  /**
   * {@link SqlSessionFactory}
   */
  private static SqlSessionFactory sqlSessionFactory;

  private static SqlSessionFactory swfSqlSessionFactory;

  /**
   * {@link Properties}
   */
  private static final Properties PROPERTIES = new Properties();

  static {
    try {
      File dataSourceFile = ResourceUtils.getFile("classpath:dao/postgresql.properties");
      PROPERTIES.load(new FileInputStream(dataSourceFile));

//      File swfDataSourceFile = ResourceUtils.getFile("classpath:dao/storage.properties");
//      PROPERTIES.load(new FileInputStream(swfDataSourceFile));

    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  /**
   * 获取 SqlSessionFactory
   * <p>
   *
   * @return {@link SqlSessionFactory}
   */
  public static SqlSessionFactory getSqlSessionFactory() {
    if (sqlSessionFactory == null) {
      synchronized (MyBatisSqlSessionFactoryUtil.class) {
        if (sqlSessionFactory == null) {
          DataSource dataSource = getDataSource();
          TransactionFactory transactionFactory = new JdbcTransactionFactory();

          Environment environment = new Environment("development", transactionFactory, dataSource);

          Configuration configuration = new Configuration(environment);
          configuration.setLazyLoadingEnabled(true);
          configuration.addMappers("com.baifendian.comp.dao.postgresql.mapper");

          SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
          sqlSessionFactory = builder.build(configuration);
        }
      }
    }
    return sqlSessionFactory;
  }

  /**
   * 获取 SqlSession
   * <p>
   *
   * @return {@link SqlSession}
   */
  public static SqlSession getSqlSession() {
    return new SqlSessionTemplate(getSqlSessionFactory());
  }

  /**
   * 获取 DataSource
   * <p>
   */
  private static DataSource getDataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl(PROPERTIES.getProperty("jdbc.url"));
    dataSource.setUsername(PROPERTIES.getProperty("jdbc.user"));
    dataSource.setPassword(PROPERTIES.getProperty("jdbc.pass"));
    dataSource.setInitialSize(Integer.valueOf(PROPERTIES.getProperty("jdbc.initialSize")));
    dataSource.setMinIdle(Integer.valueOf(PROPERTIES.getProperty("jdbc.minIdle")));
    dataSource.setMaxActive(Integer.valueOf(PROPERTIES.getProperty("jdbc.maxActive")));
    dataSource.setMaxWait(Integer.valueOf(PROPERTIES.getProperty("jdbc.maxWait")));
    dataSource.setTimeBetweenEvictionRunsMillis(
        Long.valueOf(PROPERTIES.getProperty("jdbc.timeBetweenEvictionRunsMillis")));
    dataSource.setMinEvictableIdleTimeMillis(
        Long.valueOf((PROPERTIES.getProperty("jdbc.minEvictableIdleTimeMillis"))));
    dataSource.setTestWhileIdle(Boolean.valueOf(PROPERTIES.getProperty("jdbc.testWhileIdle")));
    dataSource.setTestOnBorrow(Boolean.valueOf(PROPERTIES.getProperty("jdbc.testOnBorrow")));
    dataSource.setTestOnReturn(Boolean.valueOf(PROPERTIES.getProperty("jdbc.testOnReturn")));
    dataSource.setRemoveAbandoned(true);
    dataSource.setRemoveAbandonedTimeout(1800);
    return dataSource;
  }

  public static List<String> getDataSourceMysql() {
    String dirverClass = PROPERTIES.getProperty("jdbc.driverClassName");
    String url = PROPERTIES.getProperty("dw.jdbc.url");
    String user = PROPERTIES.getProperty("dw.jdbc.user");
    String password = PROPERTIES.getProperty("dw.jdbc.pass");
    List<String> list = new ArrayList<String>();
    list.add(dirverClass);
    list.add(url);
    list.add(user);
    list.add(password);
    return list;
  }

  public static List<String> getDataSourceHive() {
    String dirverClass = PROPERTIES.getProperty("hive.driverClass");
    String url = PROPERTIES.getProperty("hive.thrift.uris");
    String user = PROPERTIES.getProperty("hive.root.user");
    String password = PROPERTIES.getProperty("hive.root.password");
    List<String> list = new ArrayList<String>();
    list.add(dirverClass);
    list.add(url);
    list.add(user);
    list.add(password);
    return list;
  }

  /**
   * 获取 DataSource
   * <p>
   */
  private static DataSource getSwfDataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl(PROPERTIES.getProperty("swf.jdbc.url"));
    dataSource.setUsername(PROPERTIES.getProperty("swf.jdbc.user"));
    dataSource.setPassword(PROPERTIES.getProperty("swf.jdbc.pass"));
    dataSource.setInitialSize(Integer.valueOf(PROPERTIES.getProperty("swf.jdbc.initialSize")));
    dataSource.setMinIdle(Integer.valueOf(PROPERTIES.getProperty("swf.jdbc.minIdle")));
    dataSource.setMaxActive(Integer.valueOf(PROPERTIES.getProperty("swf.jdbc.maxActive")));
    dataSource.setMaxWait(Integer.valueOf(PROPERTIES.getProperty("swf.jdbc.maxWait")));
    dataSource.setTimeBetweenEvictionRunsMillis(
        Long.valueOf(PROPERTIES.getProperty("swf.jdbc.timeBetweenEvictionRunsMillis")));
    dataSource.setMinEvictableIdleTimeMillis(
        Long.valueOf((PROPERTIES.getProperty("swf.jdbc.minEvictableIdleTimeMillis"))));
    dataSource.setTestWhileIdle(Boolean.valueOf(PROPERTIES.getProperty("swf.jdbc.testWhileIdle")));
    dataSource.setTestOnBorrow(Boolean.valueOf(PROPERTIES.getProperty("swf.jdbc.testOnBorrow")));
    dataSource.setTestOnReturn(Boolean.valueOf(PROPERTIES.getProperty("swf.jdbc.testOnReturn")));
    dataSource.setRemoveAbandoned(true);
    dataSource.setRemoveAbandonedTimeout(1800);
    return dataSource;
  }

  /**
   * 获取 swordfish SqlSessionFactory
   * <p>
   *
   * @return {@link SqlSessionFactory}
   */
  public static SqlSessionFactory getSwordfishSqlSessionFactory() {
    if (swfSqlSessionFactory == null) {
      synchronized (MyBatisSqlSessionFactoryUtil.class) {
        if (swfSqlSessionFactory == null) {
          DataSource dataSource = getSwfDataSource();
          TransactionFactory transactionFactory = new JdbcTransactionFactory();

          Environment environment = new Environment("development", transactionFactory, dataSource);

          Configuration configuration = new Configuration(environment);
          configuration.setLazyLoadingEnabled(true);
          configuration.addMappers("com.bfd.dw.dao.mysql.swordfish.mapper");

          SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
          swfSqlSessionFactory = builder.build(configuration);
        }
      }
    }

    return swfSqlSessionFactory;
  }

  /**
   * 获取 swordfish SqlSession
   * <p>
   *
   * @return {@link SqlSession}
   */
  public static SqlSession getSwordfishSqlSession() {
    return new SqlSessionTemplate(getSwordfishSqlSessionFactory());
  }
}
