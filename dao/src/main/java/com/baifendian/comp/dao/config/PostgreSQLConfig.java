package com.baifendian.comp.dao.config;

import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@PropertySource({"classpath:dao/postgresql.properties"})
@MapperScan(basePackages = "com.baifendian.comp.dao.postgresql.mapper", sqlSessionFactoryRef = "PostgreSQLSessionFactory")
public class PostgreSQLConfig {

  @Value("${jdbc.driverClassName}")
  private String driverClassName;

  @Value("${jdbc.url}")
  private String dbUrl;

  @Value("${jdbc.user}")
  private String dbUser;

  @Value("${jdbc.pass}")
  private String dbPassword;

  //配置初始化大小、最小、最大
  @Value("${jdbc.initialSize}")
  private Integer initialSize;

  @Value("${jdbc.minIdle}")
  private Integer minIdle;

  @Value("${jdbc.maxActive}")
  private Integer maxActive;
  // 配置获取连接等待超时的时间

  @Value("${jdbc.maxWait}")
  private long maxWait;

  // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
  @Value("${jdbc.timeBetweenEvictionRunsMillis}")
  private long timeBetweenEvictionRunsMillis;

  // 配置一个连接在池中最小生存的时间，单位是毫秒

  @Value("${jdbc.minEvictableIdleTimeMillis}")
  private long minEvictableIdleTimeMillis;
  // 配置测试属性

  @Value("${jdbc.testWhileIdle}")
  private boolean testWhileIdle;
  @Value("${jdbc.testOnBorrow}")
  private boolean testOnBorrow;
  @Value("${jdbc.testOnReturn}")
  private boolean testOnReturn;

  @Primary
  @Bean(name = "PostgreSQLDataSource")
  public DataSource PostgreSQLDataSource() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setUrl(dbUrl);
    dataSource.setPassword(dbPassword);
    dataSource.setUsername(dbUser);
    //配置初始化大小、最小、最大
    dataSource.setInitialSize(initialSize);
    dataSource.setMinIdle(minIdle);
    dataSource.setMaxActive(maxActive);
    // 配置获取连接等待超时的时间
    dataSource.setMaxWait(maxWait);

    // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    // 配置一个连接在池中最小生存的时间，单位是毫秒
    dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    // 配置测试属性
    dataSource.setTestWhileIdle(testWhileIdle);
    dataSource.setTestOnBorrow(testOnBorrow);
    dataSource.setTestOnReturn(testOnReturn);

    return dataSource;
  }

  @Primary
  @Bean(name = "PostgreSQLTransactionManager")
  public DataSourceTransactionManager PostgreSQLTransactionManager(
      @Qualifier("PostgreSQLDataSource") DataSource dwDataSource) {
    return new DataSourceTransactionManager(dwDataSource);
  }

  @Primary
  @Bean(name = "PostgreSQLSessionFactory")
  public SqlSessionFactory PostgreSQLSessionFactory(@Qualifier("PostgreSQLDataSource") DataSource dwDataSource)
      throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dwDataSource);
    return sessionFactory.getObject();
  }
}
