package com.baifendian.comp.dao.postgresql.model.dash;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashMetaData {
  private JsonNode styleConf;
}
