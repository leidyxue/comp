package com.baifendian.bi.engine.element.condition;

import com.baifendian.bi.engine.enums.MatchType;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.SQLValue;
import java.util.Date;

public class MatchCondition implements Condition{
  private Column fieldName;
  private SQLValue value;
  private MatchType operator;

  public MatchCondition(Column fieldName, SQLValue value, MatchType type){
    this.fieldName = fieldName;
    this.value = value;
    this.operator = type;
  }

  @Override
  public String toString(){
    return fieldName.cast(value.getType()) + operator + value;
  }
}
