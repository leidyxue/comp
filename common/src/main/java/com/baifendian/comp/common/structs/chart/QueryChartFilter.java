package com.baifendian.comp.common.structs.chart;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryChartFilter {

  private String id;
  private List<String> range;
}
