package com.baifendian.comp.common.structs.chart;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilterInner {
  public FilterInner(String id){
    fieldId = id;
  }

  private String fieldId;
  private List<String> range;
}
