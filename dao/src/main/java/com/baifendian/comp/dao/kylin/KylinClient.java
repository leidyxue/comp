package com.baifendian.comp.dao.kylin;

import com.baifendian.comp.common.exception.BiException;
import com.baifendian.comp.common.utils.json.JsonUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KylinClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(KylinClient.class);
  private static final String EXPECTED_MIME_TYPE = "application/json";

  private Client client = null;
  private String url = null;

  public KylinClient(String url, String userName, String password) {
    this.url = url;

    client = Client.create();
    client.addFilter(new HTTPBasicAuthFilter(userName, password));
  }

  public <T> T getResult(String path, Class<T> tClass) {
    String result = exec(path, "GET");

    return JsonUtil.parseObject(result, tClass);
  }

  public <T> List<T> getResultList(String path, Class<T> tClass) {
    String result = exec(path, "GET");

    return JsonUtil.parseObjectList(result, tClass);
  }

  public String exec(String path, String method) {
    return exec(path, method, null);
  }

  public String exec(String path, String method, String jsonData) {

    WebResource webResource = client.resource(url + path);

    return exec(webResource, method, jsonData);
  }

  public String exec(WebResource webResource, String method, String jsonData) {

    ClientResponse response = null;
    try {
      response = webResource.accept(EXPECTED_MIME_TYPE)
          .entity(jsonData)
          .header("X-Requested-With", "XMLHttpRequest")
          .header("Content-Type", "application/json")
          .header("X-XSRF-HEADER", " ").method(method, ClientResponse.class);
      String result = response.getEntity(String.class);
      LOGGER.debug("Ranger result error.status:{}, result: {}, data:{}", response.getStatus(), result, jsonData);

      if (response.getStatus() >= 300) {
        // TODO error
        LOGGER.warn("Ranger result error.status:{}, result: {}", response.getStatus(), result);
        if (response.getStatus() == 404){
          throw new BiException("404");
        }
        throw new BiException("error.");
      }else{
        return result;
      }
    }catch (UniformInterfaceException e){
      LOGGER.debug("Ranger result error.status:{}, json: {}", response.getStatus(), jsonData);
      return null;
    }    finally {
      if (response != null) {
        response.close();
      }
    }
  }
}
