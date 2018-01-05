package com.baifendian.comp.api.dto.dashboard;

import com.baifendian.comp.common.annotation.ParamRequest;
import com.baifendian.comp.common.structs.dash.DashElementParam;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ParamRequest
public class PutDashConfigReqDTO {
  private List<DashElementParam> elements;
  private JsonNode styleConf;
}
