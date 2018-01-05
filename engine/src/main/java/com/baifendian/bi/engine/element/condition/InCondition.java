package com.baifendian.bi.engine.element.condition;

import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.SQLValue;
import java.util.List;
import java.util.stream.Collectors;

public class InCondition implements Condition {

  private Column column;
  private List<SQLValue> value;

  public InCondition(Column column, List<SQLValue> value) {
    this.column = column;
    this.value = value;
  }

  @Override
  public String toString() {
    return column.cast(value.get(0).getType()) + " IN (" + value.stream().map(SQLValue::toString)
        .collect(Collectors.joining(", ")) + ") ";
  }
}
