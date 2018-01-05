
package com.baifendian.comp.common.config;

import com.baifendian.comp.common.utils.file.FileHelper;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 业务相关配置
 * <p>
 *
 * @author : xuelei
 */

@Configuration
@PropertySource({"classpath:biz.properties"})
public class BizConfig {
  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Value("${tmp.path}")
  private static String tmpPath;

  @Value("${export.path}")
  private static String exportPath;

  @Value("${rpc.host}")
  private static String host;

  @Value("${rpc.port}")
  private static String port;


  @Value("${rpc.host}")
  public void setHost(String host) {
    BizConfig.host = host;
  }

  @Value("${rpc.port}")
  public void setPort(String port) {
    BizConfig.port = port;
  }

  private static int fileMaxSize;

  private static String fileImportCoding;


  @Value("${export.path}")
  public void setExportPath(String exportPath) {
    BizConfig.exportPath = exportPath;
  }

  @Value("${file.max.size}")
  public void setFileMaxSize(int size) {
    fileMaxSize = size;
  }
  @Value("${tmp.path}")
  public void setTmpPath(String path) {
    Map<String, String> map = System.getenv();

    tmpPath = path + "/" + map.get("USER")+"/bi";
//    tmpPath = path;
  }
  @Value("${file.import.coding}")
  public void setFileImportCoding(String code) {
    fileImportCoding = code;
  }

  @PostConstruct
  public void init() throws IOException {
    File directory = new File(tmpPath);
    if (!directory.exists()) {
      if (!directory.mkdirs()) {
        throw new RuntimeException("本地缓存目录创建失败, " + tmpPath);
      }
    } else if (directory.isFile()) {
      throw new RuntimeException("本地缓存目录，必须是一个目录，不能是文件, " + tmpPath);
    }
  }

  @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
  public void tmpPathClean() {
    LOGGER.warn("Begin clean tmp file.");
    FileHelper.tmpPathClean(tmpPath);
    LOGGER.warn("End clean tmp file.");
  }

  /**
   *
   * @return size, unit:B
   */
  public static int getFileMaxSize(){
    return fileMaxSize* 1024*1024;
  }

  public static int getFileMaxM(){
    return fileMaxSize;
  }

  /**
   *
   * @return size, unit:MB
   */
  public static int getFileMaxMSize(){
    return fileMaxSize;
  }

  public static String getTmpPath() {
    return tmpPath;
  }

  public static String getFileImportCoding(){
    return fileImportCoding;
  }

  public static String getExportPath() {
    return exportPath;
  }

  public static String getHost() {
    return host;
  }

  public static String getPort() {
    return port;
  }

}
