package com.baifendian.comp.dao.postgresql.model.dash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashLink {
  private String dashId;
  private String chartId;
  private String tableId;
  private String fieldId;
  private String linkChartId;
  private String linkTableId;
  private String linkFieldId;
}
