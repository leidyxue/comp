package com.baifendian.bi.engine.element.condition;

import com.baifendian.bi.engine.meta.Column;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class ExpressionCondition implements Condition{

  private String exp;
  private List<Column> columnList;

  public ExpressionCondition(String exp, List<Column> columns){
    this.exp = exp;
    this.columnList = columns;
  }

  @Override
  public String toString(){
    String result = exp;
    if (CollectionUtils.isNotEmpty(columnList)) {
      for (Column c : columnList) {
        result = result.replace("${" + c.getId() + "}", c.getName());
      }
    }
    return result;
  }
}
