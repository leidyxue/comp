package com.baifendian.bi.engine.meta;

import com.baifendian.bi.engine.element.condition.Condition;
import com.baifendian.bi.engine.enums.CombineLogic;
import java.util.List;

public interface SQL {

  List<String> getTitle();
  List<Column> getColumns();
  SQL setDistinct();

  SQL addColumn(Column column);
  SQL addColumn(List<Column> column);
  SQL table(SQLTable table);

  SQL setCondition(Condition condition);
  SQL setCondition(CombineLogic logic, Condition condition);

  SQL setGroupBy(GroupBy groupBy);
  SQL setOrderBy(OrderBy orderBy);

  SQL limit(int num);

  String build();
}
