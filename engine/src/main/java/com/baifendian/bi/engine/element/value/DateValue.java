package com.baifendian.bi.engine.element.value;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.meta.SQLValue;

public class DateValue implements SQLValue{
  private String date;
  private CustomHandle handle;

  public DateValue(String date, CustomHandle handle){
    this.date = date;
    this.handle = handle;
  }

  @Override
  public String toString(){
    return getValue();
  }

  @Override
  public FieldType getType() {
    return FieldType.DATE;
  }

  @Override
  public String getValue() {
    return handle.castDate("'"+date+"'");
  }

  @Override
  public CustomHandle getCustomHandle() {
    return handle;
  }
}
