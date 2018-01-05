package com.baifendian.bi.engine.element.column;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.ColumnType;
import com.baifendian.bi.engine.meta.SQLValue;

public class ConstsColumn implements Column{
  private SQLValue value;
  private String id;

  public ConstsColumn(String id, SQLValue value){
    this.value = value;
    this.id = id;
  }

  @Override
  public CustomHandle getCustomHandle() {
    return value.getCustomHandle();
  }

  @Override
  public ColumnType getType() {
    return new ColumnType(value.getType(), value.getType());
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Column modifyId(String newId) {
    return new ConstsColumn(newId, value);
  }

  @Override
  public String getName() {
    return value.toString();
  }
}
