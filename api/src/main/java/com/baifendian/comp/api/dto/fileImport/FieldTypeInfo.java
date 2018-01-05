package com.baifendian.comp.api.dto.fileImport;

import com.baifendian.bi.engine.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by xuelei on 2017/10/26 0026.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldTypeInfo {
  private FieldType type;
  private String name;
  private String fieldId;
}
