package com.baifendian.comp.dao.structs.sql;

import com.baifendian.bi.engine.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DbFieldMeta {
  private String name;
  private String desc;
  private FieldType type;
  private String orgType;
  private int sortId;
}
