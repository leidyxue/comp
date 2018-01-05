package com.baifendian.comp.dao.postgresql.model.chart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InnerFilter {

  private String chartId;
  private String fieldId;
}
