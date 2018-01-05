package com.baifendian.comp.common.ds;

import com.baifendian.bi.engine.enums.FieldType;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.i18n.LocaleContextHolder;

@Setter
@Getter
public class PublicDataConf {

  @Getter
  @Setter
  public static class PublicFieldMeta {

    private String name;
    private String orgName;
    private FieldType type;
  }

  @Setter
  @Getter
  public static class PublicTableMeta {

    private String table;
    private String name;
    private List<PublicFieldMeta> fields;
  }
  private String id;
  private JsonNode name;
  private List<PublicTableMeta> meta;


  public String i18nName(){
    return name.get(LocaleContextHolder.getLocale().toString()).asText();
  }
}
