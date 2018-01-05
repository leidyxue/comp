package com.baifendian.comp.api.dto.datasource;

import com.baifendian.comp.common.enums.ds.DsMetaType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DsPubMeta {

  private JsonNode name;
  @Default
  private DsMetaType metaType = DsMetaType.PUBLIC_DATA;
  @JsonProperty("isUsed")
  private boolean isUsed;
}
