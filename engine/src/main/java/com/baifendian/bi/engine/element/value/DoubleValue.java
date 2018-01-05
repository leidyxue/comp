package com.baifendian.bi.engine.element.value;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.meta.SQLValue;

public class DoubleValue implements SQLValue{

  private String val;
  private CustomHandle handle;

  public DoubleValue(String val, CustomHandle handle){
    this.val = val;
    this.handle = handle;
  }

  @Override
  public String toString(){
    return getValue();
  }

  @Override
  public String getValue() {
    try {
      Double.valueOf(val);
      return val;
    }catch (NumberFormatException num){
      // 常量不是一个合法的数值，直接用0替换
      return "0";
    }
  }

  @Override
  public FieldType getType() {
    return FieldType.NUM;
  }

  @Override
  public CustomHandle getCustomHandle() {
    return handle;
  }
}
