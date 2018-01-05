package com.baifendian.bi.engine.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GroupBy {

  private List<Column> columns = new ArrayList<>();

  public GroupBy addGroupBy(Column column) {
    columns.add(column);

    return this;
  }

  @Override
  public String toString() {
    if (columns.isEmpty()){
      return " ";
    }
    return " GROUP by " + columns.stream().map(Column::getAlisa).collect(Collectors.joining(", "));
  }
}
