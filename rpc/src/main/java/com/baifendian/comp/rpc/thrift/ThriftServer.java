package com.baifendian.comp.rpc.thrift;

import com.baifendian.comp.rpc.service.QuartzServiceHandler;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThriftServer implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThriftServer.class);

  @Value("${thrift.server.port}")
  public int SERVER_PORT;

  private void startThrift() {
    TProcessor processor = new QuartzService.Processor<>(
        new QuartzServiceHandler());

    TServerSocket transport = null;
    try {
      transport = new TServerSocket(SERVER_PORT);
    } catch (TTransportException e) {
      e.printStackTrace();
    }
    TServer server = new TThreadPoolServer(
        new TThreadPoolServer.Args(transport).processor(processor));
    LOGGER.info("Start server on port " + SERVER_PORT + "...");
    server.serve();

    //    TThreadPoolServer.Args ttpsArgs = new TThreadPoolServer.Args(transport);
    //    ttpsArgs.processor(processor);
    //    ttpsArgs.protocolFactory(new TBinaryProtocol.Factory());
    // 线程池服务模型，使用标准的阻塞式IO，预先创建一组线程处理请求。
    //    TThreadPoolServer server = new TThreadPoolServer(ttpsArgs);
  }

  @Override
  public void run(String... strings) throws Exception {
    startThrift();
  }
}
