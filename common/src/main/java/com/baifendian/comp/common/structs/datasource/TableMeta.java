package com.baifendian.comp.common.structs.datasource;

import com.baifendian.comp.common.consts.Constants;
import com.baifendian.bi.engine.enums.FieldType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class TableMeta {

  private String tableName;

  private String desc;

  private int number;

  @Singular("field")
  private List<FieldMeta> schema;

  @Singular("data")
  @JsonFormat(pattern = Constants.BASE_DATETIME_FORMAT)
  private List<List<Object>> data;

  private List<String> titles;

  @Setter
  @Getter
  public static class FieldMeta{

    private String name;
    private String desc;
    private FieldType type;
    @JsonIgnore
    private String orgType;
  }
}
