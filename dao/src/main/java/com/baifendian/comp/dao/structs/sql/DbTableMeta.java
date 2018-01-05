package com.baifendian.comp.dao.structs.sql;

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
public class DbTableMeta {
  private String tableName;

  private String desc;

  @Singular("field")
  private List<DbFieldMeta> schema;

}
