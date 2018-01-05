package com.baifendian.comp.rpc;

import com.baifendian.comp.rpc.thrift.ThriftServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
@ComponentScan(basePackages = "com.baifendian.comp")
@Import(value = {SpringUtil.class})
public class RpcApplication {

  /**
   * Start thrift server before SpringApplication.run()
   */
  @Bean
  public ThriftServer thriftServer() {
    return new ThriftServer();
  }

  public static void main(String[] args) {
    SpringApplication.run(RpcApplication.class);
  }
}
