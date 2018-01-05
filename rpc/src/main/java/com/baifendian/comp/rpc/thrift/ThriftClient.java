package com.baifendian.comp.rpc.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThriftClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThriftClient.class);

  public static void main(String[] args) {
    try {
      TTransport transport;

      transport = new TSocket("localhost", 8090);
      transport.open();

      TProtocol protocol = new TBinaryProtocol(transport);
      QuartzService.Client client = new QuartzService.Client(protocol);

//      String result2 = client.cronTrigger("task_172539fddb52e26111c3373648548629",
//          "{\"crontab\":{\"startDate\":\"1511575200\",\"endDate\":\"1514167200\",\"type\":\"DAY\",\"dayTime\":\"16:54\"}}",
//          "");
//      LOGGER.info("Result is: [{}]", result2);
      String result3 = client.execSQL("task_b82dc99ce8df198e67052114378d90ef");
      LOGGER.info("Result is: [{}]", result3);
      transport.close();
    } catch (TException x) {
      x.printStackTrace();
    }

  }
}
