package com.baifendian.comp.api.dto.chart;

import com.baifendian.comp.common.annotation.ParamProperty;
import com.baifendian.comp.common.annotation.ParamRequest;
import com.baifendian.comp.common.enums.ChartType;
import com.baifendian.comp.common.structs.dash.DashElementParam;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ParamRequest
public class CreateChartReqDTO {
  @ParamProperty
  private String name;
  private String desc;
  private ChartType chartType;
  private String dashId;
  private String tableId;
  private List<DashElementParam> elements;
}
