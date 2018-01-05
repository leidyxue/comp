package com.baifendian.comp.common.ds;

import com.baifendian.comp.common.enums.DSType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DsMetaConf {

  @JsonProperty("DATABASE")
  private List<DbMeta> database;
  @JsonProperty("PUBLIC_DATA")
  private List<PubDataMeta> pubData;

  @Setter
  @Getter
  public static class DbMeta{

    private DSType dsType;
    private JsonNode name;
    @JsonProperty("extends")
    private JsonNode extendsData;
  }

  @Setter
  @Getter
  public static class PubDataMeta{

    private String id;
    private JsonNode name;
    private String conf;
  }
}
