package com.baifendian.comp.common.structs.chart;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChartMeta {
  private List<ChartFieldParam> drillDownFields = new ArrayList<>();
  private ChartFilter filter = new ChartFilter();
  private List<DrillDownLevel> level = new ArrayList<>();
  private List<FilterInner> filterInner = new ArrayList<>();
}
