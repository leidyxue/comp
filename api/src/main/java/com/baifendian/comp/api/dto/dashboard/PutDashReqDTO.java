package com.baifendian.comp.api.dto.dashboard;

import com.baifendian.comp.common.annotation.ParamRequest;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ParamRequest
public class PutDashReqDTO {
  private String name;
  private String tag;
  private String desc;
}
