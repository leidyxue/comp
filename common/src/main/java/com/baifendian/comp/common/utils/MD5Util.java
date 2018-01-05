package com.baifendian.comp.common.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Util {

  private static MessageDigest md = null;

  static {
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * 计算指定字符串的16位md5值
   *
   * @param str 指定字符串
   * @return 计算后的md5值
   */
  public static String getMD5(String str) {

    md.update(str.getBytes());
    return new BigInteger(1, md.digest()).toString(16);
  }

  public static void main(String[] args) {
    System.out.println(getMD5("admin123"));
  }
}
