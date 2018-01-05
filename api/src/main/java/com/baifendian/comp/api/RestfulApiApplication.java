package com.baifendian.comp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Restful Api 应用
 * <p>
 *
 * @author : shuanghu
 * @date : 17-6-28
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
@ComponentScan(value = {"com.baifendian.comp", "com.bfd.systemmanager"})
@EnableScheduling
public class RestfulApiApplication {

  /**
   * spring rest 服务的入口main函数
   */
  public static void main(String[] args) {
    SpringApplication.run(RestfulApiApplication.class);
  }
}
