package com.baifendian.comp.api.dto.table;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableField {
  private String id;

  private String dsId;

  private String name;

  @Singular("schema")
  private List<SchemaData> schema;

  private Date createTime;

  private Date modifyTime;
}
