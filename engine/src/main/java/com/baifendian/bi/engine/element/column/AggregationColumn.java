package com.baifendian.bi.engine.element.column;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.enums.AggregationType;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.ColumnType;

public class AggregationColumn implements Column {

  private String id;
  private AggregationType aggregationType;
  private Column param;

  public AggregationColumn(String id, AggregationType aggregationType, Column param) {
    this.aggregationType = aggregationType;
    this.param = param;
    this.id = id;
  }

  @Override
  public ColumnType getType() {
    return new ColumnType(FieldType.NUM, FieldType.NUM);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Column modifyId(String newId) {
    return new AggregationColumn(newId, aggregationType, param);
  }

  @Override
  public String getName() {
    switch (aggregationType) {
      case SUM:
        return "sum(" + param.cast(FieldType.NUM) + ")";
      case DISTINCT_COUNT:
        return "count(distinct(" + param.getName() + "))";
      case COUNT:
        return "count(" + param.getName() + ")";
      case AVERAGE:
        return "avg(" + param.cast(FieldType.NUM) + ")";
      case MAX:
        return "max(" + param.getName() + ")";
      case MIN:
        return "min(" + param.getName() + ")";
      default:
        return param.getName();
    }
  }

  @Override
  public CustomHandle getCustomHandle() {
    return param.getCustomHandle();
  }
}
