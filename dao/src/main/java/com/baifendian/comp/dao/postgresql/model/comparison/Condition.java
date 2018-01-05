package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.bi.engine.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Condition {

  /**
   * 左节点字段编号
   */
  private String leftFieldId;

  private String leftName;

  private String leftOriginName;

  private FieldType leftFieldType;

  private String leftOrgType;

  /**
   * 右节点字段编号
   */
  private String rightFieldId;

  private String rightName;

  private String rightOriginName;

  private FieldType rightFieldType;

  private String rightOrgType;

  private String unionStr;
}
