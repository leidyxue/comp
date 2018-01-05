package com.baifendian.comp.dao.utils;

import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.EnumTypeHandler;

public class EnumFieldUtil {

  /**
   * 生成enum字段的字符串 <p>
   *
   * @return 字段字符串
   */
  public static String genOrdinalFieldStr(String field, Class<?> enumClass) {
    return "#{" + field + ",javaType=" + enumClass.getName() + ",typeHandler="
        + EnumOrdinalTypeHandler.class.getName() + "}";
  }

  /**
   * 生成enum字段的字符串 <p>
   *
   * @return 字段字符串
   */
  public static String genFieldStr(String field, Class<?> enumClass, String dbEnum) {
    return "#{" + field + ",javaType=" + enumClass.getName() + ",typeHandler="
        + EnumTypeHandler.class.getName() + "}::" + dbEnum;
  }

  public static String genEnumFieldStr(String field, Class<?> enumClass, String dbEnum) {
    return "#'{'" + field + ",javaType=" + enumClass.getName() + ",typeHandler="
        + EnumTypeHandler.class.getName() + "}::" + dbEnum;
  }

  /**
   * 生成enum字段的字符串(MessageFormat特殊字符) <p>
   *
   * @return 字段字符串
   */
  public static String genOrdinalFieldSpecialStr(String field, Class<?> enumClass) {
    return "#'{'" + field + ",javaType=" + enumClass.getName() + ",typeHandler="
        + EnumOrdinalTypeHandler.class.getName() + "}";
  }

}
