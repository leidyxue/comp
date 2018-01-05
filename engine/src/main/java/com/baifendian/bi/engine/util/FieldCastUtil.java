package com.baifendian.bi.engine.util;

import com.baifendian.bi.engine.enums.FieldType;

/**
 * Created by Administrator on 2017/11/25 0025.
 */
public class FieldCastUtil {
  public static String castValue(String field, FieldType fieldType, String orgType){
    if (SqlUtil.createValueType(orgType).equals(fieldType)){
      return field;
    }
    if (SqlUtil.createValueType(orgType).equals(FieldType.TEXT)){
      switch (fieldType){
        case DATE:
          return "bi_date_func(" + field + ")";

        case NUM:
          return "bi_num_func(" + field + ")";

        case TEXT:
          return "cast(" + field + " as text)";
      }
    }

    return field;
  }

  public static String unionField(String leftCol, String rightCol){
    return "(case  when (" + leftCol +
        " is null ) then " + rightCol +
        " else "+ leftCol + " end)";
  }

  public String castText(String field) {
    return "cast(" + field + " as text)";
  }

  public String castNum(String field) {
    return "bi_num_func(" + field + ")";
  }

  public String castDate(String field) {
    return "bi_date_func(" + field + ")";
  }
}
