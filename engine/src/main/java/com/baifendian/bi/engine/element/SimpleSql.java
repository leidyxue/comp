package com.baifendian.bi.engine.element;

import com.baifendian.bi.engine.element.column.NullColumn;
import com.baifendian.bi.engine.element.condition.CombineCondition;
import com.baifendian.bi.engine.element.condition.Condition;
import com.baifendian.bi.engine.enums.CombineLogic;
import com.baifendian.bi.engine.meta.Column;
import com.baifendian.bi.engine.meta.GroupBy;
import com.baifendian.bi.engine.meta.OrderBy;
import com.baifendian.bi.engine.meta.SQL;
import com.baifendian.bi.engine.meta.SQLTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimpleSql implements SQL {

  private boolean distinct = false;

  private SQLTable table;
  private Condition where;
  private GroupBy groupBy;
  private OrderBy orderBy;
  private Integer limitNum;

  private List<Column> columns = new ArrayList<>();

  @Override
  public List<String> getTitle() {
    return columns.stream().map(Column::getId).collect(Collectors.toList());
  }

  @Override
  public List<Column> getColumns() {
    return columns;
  }

  @Override
  public SQL setDistinct() {
    distinct = true;
    return this;
  }

  @Override
  public SQL addColumn(Column column) {
    if (column == null){
      columns.add(new NullColumn("1", null));
    }else {
      columns.add(column);
    }
    return this;
  }

  @Override
  public SQL addColumn(List<Column> column) {
    columns.addAll(column.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    return this;
  }

  @Override
  public SQL table(SQLTable table) {
    this.table = table;
    return this;
  }

  @Override
  public SQL setCondition(Condition condition) {
    return setCondition(CombineLogic.AND, condition);
  }

  @Override
  public SQL setCondition(CombineLogic logic, Condition condition) {
    if (where == null){
      where = condition;
    }else{
      where = new CombineCondition(where, condition, logic);
    }
    return this;
  }

  @Override
  public SQL setGroupBy(GroupBy groupBy) {
    this.groupBy = groupBy;
    return this;
  }

  @Override
  public SQL setOrderBy(OrderBy orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  @Override
  public SQL limit(int num) {
    this.limitNum = num;
    return this;
  }

  @Override
  public String build() {
    String result = "SELECT " + (distinct ? " DISTINCT " : "");
    result += columns.stream().map(Column::toSelect).collect(Collectors.joining(", "))
        + " FROM " + table.toString();
    if (where != null){
      result += " WHERE " + where.toString();
    }
    if (groupBy != null){
      result += groupBy;
    }
    if (orderBy != null){
      result += orderBy.toString();
    }
    if (limitNum != null){
      result += " limit " + limitNum;
    }
    return result;
  }
}
