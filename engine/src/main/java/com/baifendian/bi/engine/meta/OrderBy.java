package com.baifendian.bi.engine.meta;

import com.baifendian.bi.engine.enums.SortType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

@NoArgsConstructor
public class OrderBy {

  private List<Pair<Column, SortType>> columns = new ArrayList<>();

  public OrderBy addColumn(Column column, SortType sortType) {
    columns.add(Pair.of(column, sortType));

    return this;
  }

  @Override
  public String toString() {
    if (columns.isEmpty()){
      return " ";
    }
    return " ORDER BY " + columns.stream()
        .map(p -> p.getKey().getAlisa() + sortType2String(p.getValue()))
        .collect(Collectors.joining(", "));
  }

  private String sortType2String(SortType st) {
    switch (st) {
      case ASC:
        return " ASC ";

      case DESC:
        return " DESC ";
    }

    return " ";
  }
}
