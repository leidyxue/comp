package com.baifendian.bi.engine.custom;

import com.baifendian.bi.engine.enums.FuncDateType;
import com.baifendian.bi.engine.meta.Column;

public interface CustomHandle {

  String escape(String orgValue);

  String dateFormat(Column field, FuncDateType dateType);
  /**
   * 转换成数字
   */
  String castNum(String field);

  /**
   * 转换成文本
   */
  default String castText(String field){
    return field;
  }

  /**
   * 转换成时间
   */
  String castDate(String field);

  /**
   * 获取SQL关键字引号
   */
  String getQuote();
}
