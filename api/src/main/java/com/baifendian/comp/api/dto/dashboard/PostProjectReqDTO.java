package com.baifendian.comp.api.dto.dashboard;

import com.baifendian.comp.common.annotation.ParamProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostProjectReqDTO {
  @ParamProperty
  private String name;
  private String desc;
  private String parentId;
}
