package com.baifendian.bi.engine.element.column;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.enums.FuncDateType;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.ColumnType;

public class DateColumn implements Column {

  private String id;
  private FuncDateType dateType;
  private Column param;

  public DateColumn(String id, FuncDateType dateType, Column param) {
    this.dateType = dateType;
    this.param = param;
    this.id = id;
  }

  public FuncDateType getDateType(){
    return dateType;
  }

  @Override
  public CustomHandle getCustomHandle() {
    return param.getCustomHandle();
  }

  @Override
  public ColumnType getType() {
    switch (dateType) {
      case MINUTE:
      case HOUR:
      case QUARTERLY:
      case MONTH:
      case YEAR:
      case DAY:
      case SECOND:
        return new ColumnType(FieldType.NUM, FieldType.NUM);

      default:
        return new ColumnType(FieldType.DATE, FieldType.DATE);
    }
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Column modifyId(String newId) {
    return new DateColumn(newId, dateType, param);
  }

  @Override
  public String getName() {
    return param.getCustomHandle().dateFormat(param, dateType);
  }
}
