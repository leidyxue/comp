package com.baifendian.comp.common.structs.chart;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class QueryChart {
  private Integer drillLevel;
  private String drillDownFieldId;
  private List<String> drillDownValues;
  private List<FilterInner> filterInner;
  private String chartId;
  private List<LinkValue> linkValues;

  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class LinkValue{

    private String fieldId;
    private String value;
  }
}
