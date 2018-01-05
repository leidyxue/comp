package com.baifendian.comp.common.structs.dash;

import com.baifendian.comp.common.enums.ElementType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashElementParam {
  private String id;
  private ElementType type;
  private String tableId;
  private JsonNode settings;
  private int x;
  private int y;
  private int w;
  private int h;

//  @Override
//  public boolean equals(Object obj) {
//
//
//    return false;
//  }

  @JsonIgnore
  public boolean isChart(){
    return ElementType.CHART == type;
  }
}
