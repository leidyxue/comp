package com.baifendian.bi.engine.element.value;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.meta.SQLValue;
import com.baifendian.bi.engine.util.SqlUtil;

public class TextValue implements SQLValue{

  private String value;
  private CustomHandle handle;

  public TextValue(String value, CustomHandle handle){
    this.value = value;
    this.handle = handle;
  }

  @Override
  public String toString(){
    return getValue();
  }

  @Override
  public String getValue() {
    return SqlUtil.sqlAddQuote(handle.escape(value), "'");
  }

  @Override
  public FieldType getType() {
    return FieldType.TEXT;
  }

  @Override
  public CustomHandle getCustomHandle() {
    return handle;
  }
}
