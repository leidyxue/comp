package com.baifendian.bi.engine.meta;

import com.baifendian.bi.engine.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ColumnType {

  private FieldType type;
  private FieldType orgType;
}
