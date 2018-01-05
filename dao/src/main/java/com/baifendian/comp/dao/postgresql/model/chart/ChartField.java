package com.baifendian.comp.dao.postgresql.model.chart;

import com.baifendian.comp.common.enums.ChartFieldType;
import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.enums.FuncType;
import com.baifendian.bi.engine.enums.SortType;
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
public class ChartField {

  private String uniqId;
  private String fieldId;
  private String alias;
  private FuncType operator;
  private SortType sort;
  private String chartId;
  private int level;
  private int order;
  private ChartFieldType type;
  private FieldType fieldType;
}
