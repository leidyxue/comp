package com.baifendian.comp.common.structs.chart;

import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.enums.FuncType;
import com.baifendian.bi.engine.enums.SortType;
import com.baifendian.comp.common.structs.field.FieldOld;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ChartFieldParam {

  /**
   * 字段id
   */
  private String id;
  /**
   * 序号id
   */
  private String uniqId;

  /**
   * 别名
   */
  private String aliasName;

  /**
   * 字段计算方式
   */
  private FuncType operator;
  /**
   * 字段排序
   */
  private SortType sort;

  @JsonIgnore
  private int order;

  /**
   * 对应的table 字段
   */
  private FieldOld field;

  /**
   * 字段类型
   */
  private FieldType type;
}
