package com.baifendian.comp.common.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class JsonUtil {
  /**
   * json mapper(这里的 ObjectMapper 是线程安全的)
   */
  private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

  static {
    // 未匹配的属性不解析
    JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // 使用系统默认时区
    JSON_MAPPER.setTimeZone(TimeZone.getDefault());
  }

  /**
   * private constructor
   */
  private JsonUtil() {
  }

  /**
   * 对象装json字符串
   * <p>
   *
   * @return json string
   */
  public static String toJsonString(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return JSON_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Json序列化异常", e);
    }
  }

  public static JsonNode createJsonNode(){
    return JSON_MAPPER.createObjectNode();
  }

  public static String toPrettyJsonString(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Json序列化异常", e);
    }
  }

  /**
   * 将string 转化为 map @param <V> @param <T> @param <T> @param
   * object @return @throws
   */
  public static <T, V> Map<T, V> stringToMap(String json, Class<T> classT, Class<V> classV) {
    try {
      return JSON_MAPPER.readValue(json, new TypeReference<Map<T, V>>() {
      });
    } catch (Exception e) {
      throw new RuntimeException("Json反序列化异常", e);
    }
  }

  /**
   * 反序列化一个json字符串
   * <p>
   *
   * @return instance of T
   */
  public static <T> T parseObject(String json, Class<T> clazz) {
    if (StringUtils.isEmpty(json)) {
      return null;
    }
    try {
      return JSON_MAPPER.readValue(json, clazz);
    } catch (IOException e) {
      LOGGER.error("Json反序列化异常,json:" + json+". class:"+clazz, e);
      throw new RuntimeException("Json反序列化异常", e);
    }
  }

  public static <T> T parseObj(String json, Class<T> clazz) throws IOException {
    if (StringUtils.isEmpty(json)) {
      return null;
    }
    return JSON_MAPPER.readValue(json, clazz);
  }

  public static <T> List<T> parseList(String json, Class<T> clazz) throws IOException {
    if (StringUtils.isEmpty(json)){
      return null;
    }

    try {
      JavaType javaType = JSON_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
      return JSON_MAPPER.readValue(json, javaType);
    }catch (IOException e){
      LOGGER.error("Json反序列化异常,json:" + json+". class:"+clazz, e);
      throw e;
    }
  }

  /**
   * 反序列化一个json字符串
   * <p>
   *
   * @return instance of List<T>
   */
  public static <T> List<T> parseObjectList(String json, Class<T> clazz) {
    if (StringUtils.isEmpty(json)) {
      return null;
      //throw new NullPointerException("json string is null");
    }
    try {
      JavaType javaType = JSON_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
      return JSON_MAPPER.readValue(json, javaType);
    } catch (IOException e) {
      LOGGER.error("Json反序列化异常,json:" + json+". class:"+clazz, e);
      throw new RuntimeException("Json反序列化异常", e);
    }
  }

  public static <T> List<T> parseObjList(String json, Class<T> clazz) throws IOException {
    if (StringUtils.isEmpty(json)) {
      throw new NullPointerException("json string is null");
    }

    JavaType javaType = JSON_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
    return JSON_MAPPER.readValue(json, javaType);

  }

  public static <T> List<T> parseObjectListIgnoreCase(String json, Class<T> clazz) {
    if (StringUtils.isEmpty(json)) {
      return null;
    }

    try {
      JavaType javaType = JSON_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
      return JSON_MAPPER.readValue(json.toUpperCase(), javaType);
    } catch (IOException e) {
      throw new RuntimeException("Json反序列化异常", e);
    }
  }

  /**
   * 获取 Json 树
   * <p>
   *
   * @return {@link JsonNode}
   */
  public static JsonNode readTree(String json) {
    if (StringUtils.isEmpty(json)) {
      return null;
    }
    try {
      return JSON_MAPPER.readTree(json);
    } catch (IOException e) {
      throw new RuntimeException("Json解析异常", e);
    }
  }

  /**
   * 获取字段的值（支持深度搜索）<br/>
   * 多个相同字段的情况下，获取节点顺序的第一个
   * <p>
   *
   * @param jsonNode json tree
   * @param field 字段名
   * @return {@link JsonNode}
   */
  public static String findNodeByField(JsonNode jsonNode, String field) {
    JsonNode node = jsonNode.findValue(field);
    if (node == null) {
      return null;
    }
    return node.toString();
  }

  public static class TestArray {

    List<String> test;

    public List<String> getTest() {
      return test;
    }

    public void setTest(List<String> test) {
      this.test = test;
    }
  }

  public static void main(String[] args) throws IOException {
    System.out.println(toJsonString(new TestArray()));
    TestArray testArray = parseObj("{\"test\":null}", TestArray.class);
    System.out.println(testArray.getTest());
  }
}
