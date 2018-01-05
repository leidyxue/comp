package com.baifendian.comp.api.dto.dashboard;

import com.baifendian.comp.common.annotation.ParamRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ParamRequest
public class PutDashFiltersReqDTO {
  List<DashFilterParam> filters;
}
