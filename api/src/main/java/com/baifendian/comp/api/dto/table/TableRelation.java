package com.baifendian.comp.api.dto.table;

import com.baifendian.comp.api.dto.datasource.SimpleDatasource;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableRelation {

  private SimpleDatasource datasource;
  @Singular("table")
  private List<TableSimpleData> tables;
  @Singular("chart")
  private List<ChartDash> charts;
}
