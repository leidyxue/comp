package com.baifendian.comp.dao.config;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by xuelei on 2017/10/19 0019.
 */
@Configuration
@PropertySource({"classpath:dao/storage.properties"})
public class StorageConfig {

  @Bean(name = "StorageConfig")
  public StorageConfig storageConfig(){
    return new StorageConfig();
  }

  private static String jdbcType;

  private static String dbAddress;

  private static String dbName;

  private static String schema;
  private static String shareSchema;
  private static boolean shareEnable;
  private static String pubDataSchema;
  private static String compResultSchema;

  private static String dbUser;

  private static String dbPassword;

  public static DSType getJdbcType(){
    return DSType.valueOf(jdbcType);
  }


  @Value("${jdbc.type}")
  public void setJdbcType(String jdbcTypeStr){
    jdbcType = jdbcTypeStr.toUpperCase();
  }

  @Value("${jdbc.address}")
  public void setDbAddress( String dbAddress) {
    StorageConfig.dbAddress = dbAddress;
  }

  @Value("${jdbc.dbName}")
  public void setDbName(String dbName) {
    StorageConfig.dbName = dbName;
  }

  public static String getSchema() {
    return schema;
  }

  public static String getShareSchema() {
    return shareSchema;
  }

  public static boolean isShareOpen(){
    return shareEnable;
  }

  public static String getCompResultSchema() {
    return compResultSchema;
  }

  public static String getPublicSchema() {
    return pubDataSchema;
  }

  @Value("${jdbc.schema}")
  public void setSchema(String schema) {
    StorageConfig.schema = schema;
  }

  @Value("${bi.share.schema}")
  public void setShareSchema(String schema) {
    StorageConfig.shareSchema = schema;
  }

  @Value("${bi.share.enable}")
  public void setShareEnable(boolean open) {
    StorageConfig.shareEnable = open;
  }

  @Value("${bi.public_data.schema}")
  public void setPubDataSchema(String schema) {
    StorageConfig.pubDataSchema = schema;
  }

  @Value("${comp.result.schema}")
  public void setCompResultSchema(String schema) {
    StorageConfig.compResultSchema = schema;
  }

  @Value("${jdbc.user}")
  public void setDbUser(String dbUser) {
    StorageConfig.dbUser = dbUser;
  }

  @Value("${jdbc.pass}")
  public void setDbPassword(String dbPassword) {
    StorageConfig.dbPassword = dbPassword;
  }

  public static JDBCParam getFileJDBCParam(){
    return getJDBCParam();
  }

  public static JDBCParam getJDBCParam(){
    JDBCParam jdbcParam = new JDBCParam();
    jdbcParam.setAddress(dbAddress);
    jdbcParam.setDatabase(dbName);
    jdbcParam.setType(DSType.valueOf(jdbcType));
    jdbcParam.setUser(dbUser);
    jdbcParam.setPassword(dbPassword);

    return jdbcParam;
  }
}
