package com.baifendian.comp.dao.postgresql.model.dash;

import com.baifendian.bi.engine.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashFilter {
  private String id;
  private String name;
  private FieldType fieldType;
  private String tableId;
  private String fieldId;
  private String chartId;
  private String dashId;
}
