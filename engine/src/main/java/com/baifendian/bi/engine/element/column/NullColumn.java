package com.baifendian.bi.engine.element.column;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.ColumnType;

public class NullColumn implements Column{
  private String id;
  private CustomHandle customHandle;

  public NullColumn(String id, CustomHandle constsColumn) {
    this.id = id;
    this.customHandle = constsColumn;
  }

  @Override
  public String cast(FieldType type) {
    return "null";
  }

  @Override
  public CustomHandle getCustomHandle() {
    return customHandle;
  }

  @Override
  public ColumnType getType() {
    return new ColumnType(FieldType.NULL, FieldType.NULL);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Column modifyId(String newId) {
    return new NullColumn(newId, getCustomHandle());
  }

  @Override
  public String getName() {
    return "null";
  }
}
