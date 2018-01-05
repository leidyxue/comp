package com.baifendian.bi.engine.element.condition;

import com.baifendian.bi.engine.meta.Column;

public class NullCondition implements Condition{
  private Column fieldName;
  private boolean isNull;

  public NullCondition(Column field, boolean isNull){
    this.fieldName = field;
    this.isNull = isNull;
  }

  @Override
  public String toString(){
    return fieldName.getName() + (isNull? " is null": " is not null");
  }

}
