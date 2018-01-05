package com.baifendian.comp.api.dto.chart;

import com.baifendian.comp.common.annotation.ParamRequest;
import com.baifendian.comp.common.enums.ChartType;
import com.baifendian.comp.common.structs.chart.ChartMeta;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ParamRequest
public class PutChartReqDTO {
  private String name;
  private ChartType chartType;
  private String desc;
  private ChartMeta meta;
}
