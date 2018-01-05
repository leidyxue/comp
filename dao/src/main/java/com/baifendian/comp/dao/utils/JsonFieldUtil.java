package com.baifendian.comp.dao.utils;

public class JsonFieldUtil {
  /**
   * 生成enum字段的字符串 <p>
   *
   * @return 字段字符串
   */
  public static String genFieldStr(String field) {
    return "to_json(#{" + field + ",typeHandler="
        + JsonTypeHandler.class.getName() + "}::jsonb)";
  }

}
