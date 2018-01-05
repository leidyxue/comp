package com.baifendian.bi.engine.meta;

import com.baifendian.bi.engine.custom.Customable;
import com.baifendian.bi.engine.element.column.ConstsColumn;
import com.baifendian.bi.engine.element.column.DateColumn;
import com.baifendian.bi.engine.element.column.AggregationColumn;
import com.baifendian.bi.engine.element.column.NullColumn;
import com.baifendian.bi.engine.element.value.DateValue;
import com.baifendian.bi.engine.element.value.DoubleValue;
import com.baifendian.bi.engine.element.value.TextValue;
import com.baifendian.bi.engine.enums.AggregationType;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.enums.FuncDateType;
import com.baifendian.bi.engine.enums.FuncType;
import java.util.List;
import java.util.stream.Collectors;

public interface SQLTable extends Customable {

  default Column createNullColumn(String id) {
    return new NullColumn(id, getCustomHandle());
  }

  String getAlisa();

  Column createColumn(String name, String id, ColumnType columnType);
  default Column createColumn(String name, String id, FieldType fieldType){
    return createColumn(name, id, new ColumnType(fieldType, fieldType));
  }

  default Column createConstsColumn(String id, String value, FieldType fieldType){
    switch (fieldType){
      case DATE:
        return new ConstsColumn(id, createDateValue(value));

      case NUM:
        return new ConstsColumn(id, createNumValue(value));
    }

    return new ConstsColumn(id, createTextValue(value));
  }
  default SQLValue createDateValue(String val){
    return new DateValue(val, getCustomHandle());
  }
  default SQLValue createTextValue(String val){
    return new TextValue( val, getCustomHandle());
  }
  default List<SQLValue> createTextValue(List<String> val){
    return val.stream().map(s ->  new TextValue(s, getCustomHandle())).collect(Collectors.toList());
  }
  default SQLValue createNumValue(String val){
    return new DoubleValue(val, getCustomHandle());
  }

  default Column createFuncColumn(FuncType type, Column param, String id ) {
    switch (type){
      case SUM:
      case DISTINCT_COUNT:
      case COUNT:
      case AVERAGE:
      case MAX:
      case MIN:
        return new AggregationColumn(id, AggregationType.valueOf(type.name()), param);

      default:
        return new DateColumn(id, FuncDateType.valueOf(type.name()), param);
    }
  }
}
