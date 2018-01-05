package com.baifendian.bi.engine.factory;

import com.baifendian.bi.engine.custom.Customable;
import com.baifendian.bi.engine.element.column.DateColumn;
import com.baifendian.bi.engine.element.column.AggregationColumn;
import com.baifendian.bi.engine.element.column.NullColumn;
import com.baifendian.bi.engine.element.condition.Condition;
import com.baifendian.bi.engine.element.condition.NullCondition;
import com.baifendian.bi.engine.element.SimpleSql;
import com.baifendian.bi.engine.element.table.SqlJoinTable;
import com.baifendian.bi.engine.element.value.DateValue;
import com.baifendian.bi.engine.element.value.DoubleValue;
import com.baifendian.bi.engine.element.value.TextValue;
import com.baifendian.bi.engine.enums.AggregationType;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.enums.FuncDateType;
import com.baifendian.bi.engine.enums.FuncType;
import com.baifendian.bi.engine.enums.JoinType;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.GroupBy;
import com.baifendian.bi.engine.meta.OrderBy;
import com.baifendian.bi.engine.meta.SQL;
import com.baifendian.bi.engine.meta.SQLTable;
import com.baifendian.bi.engine.meta.SQLValue;
import java.util.List;
import java.util.stream.Collectors;

public interface SqlBuilder extends Customable {
  default SQL sql(){
    return new SimpleSql();
  }

  default Column createFuncColumn(String id, FuncType type, Column param) {
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

  SQLTable table(String name);

  default GroupBy createGroupBy(){
    return new GroupBy();
  }

  default OrderBy createOrderBy(){
    return new OrderBy();
  }

  default Condition nullCondition(Column column, boolean isNull){
    return new NullCondition(column, isNull);
  }

  default SQLTable join(SQLTable left, SQLTable right, JoinType joinType, Condition on){
    return new SqlJoinTable(left, right, joinType, on);
  }

  default SQLValue createSQLValue(String value, FieldType type){
    switch (type){
      case NUM:
        return createNumValue(value);
      case DATE:
        return createDateValue(value);

        default:
    }
    return createTextValue(value);
  }

  default SQLValue createDateValue(String dateStr){
    return new DateValue(dateStr, getCustomHandle());
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
}
