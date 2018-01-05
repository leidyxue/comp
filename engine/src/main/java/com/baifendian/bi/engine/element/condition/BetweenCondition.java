package com.baifendian.bi.engine.element.condition;

import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.SQLValue;

public class BetweenCondition implements Condition{
  private Column column;
  private SQLValue start;
  private SQLValue end;

  public BetweenCondition(Column column, SQLValue start, SQLValue end){
    this.column = column;
    this.start = start;
    this.end = end;
  }

  @Override
  public String toString(){
    return column.cast(start.getType()) + " Between " + start.getValue() + " and " + end.getValue();
  }
}
