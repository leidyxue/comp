package com.baifendian.comp.dao.postgresql.model.dash;

import com.baifendian.comp.common.enums.ElementType;
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
@NoArgsConstructor
@AllArgsConstructor
public class DashElement {
  private String id;
  private ElementType type;
  private String tableId;
  private JsonNode settings;
  private int x;
  private int y;
  private int w;
  private int h;
  private String dashId;
}
