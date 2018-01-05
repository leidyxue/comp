package com.baifendian.comp.common.utils;

import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by xuelei on 2017/10/20 0020.
 */
public class DataTypeUtil {

  /**
   * 判断数据能不能转成实数
   */
  public static boolean isNumeric(Object str) {
    if (str == null){
      return false;
    }
    Pattern pattern = Pattern.compile("^[-+]?\\d+(\\.\\d+)?$"); //[0-9]*
    return pattern.matcher((CharSequence) str).matches();
  }

  public static boolean isDate(Object str) {
    if (str == null){
      return false;
    }
    return DateUtils.isValidDate((String) str);
  }

  public static int[] randomNumber(int len, int maxNum){
    if (len > maxNum){
      int[] result = new int[maxNum];
      for (int i=0; i<maxNum; ){
        result[i] = i++;
      }
      return result;
    }

    int []tmp = new int[maxNum];
    for (int i=0;i<maxNum; ++i){
      tmp[i] = i;
    }
    int[] result = new int[len];

    Random random = new Random();
    for (int i=0; i<len; ++i){
      int num = random.nextInt(maxNum);
      int in = tmp[i];
      tmp[i] = tmp[num];
      tmp[num] = in;
    }

    for (int i=0; i<len; ++i){
      result[i] = tmp[i];
    }
    Arrays.sort(result);

    return result;
  }



  public static void main(String[] args) {
    if (DataTypeUtil.isNumeric("0")) {
      System.out.println("是数值");
    } else {
      System.out.println("是字符串");
    }
  }
}
