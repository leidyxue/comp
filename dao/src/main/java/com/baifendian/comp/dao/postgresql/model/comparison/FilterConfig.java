package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.util.FieldCastUtil;
import com.baifendian.comp.common.enums.Operator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
@Builder
@Setter
@Getter
public class FilterConfig {

  /**
   * 节点id
   */
  private String nodeId;

  /**
   * 字段id
   */
  private String fieldId;

  private String fieldName;

  private String fieldOriginName;

  /**
   * 运算符
   */
  private Operator operator;

  /**
   * 字段类型
   */
  private FieldType fieldType;

  private String fieldTOrgType;

  /**
   * 字段取值
   */
  private String value;

  private String startDate;

  private String endDate;

  @JsonIgnore
  public String getFilterSQL() {
//    String field = FieldCastUtil.castValue(nodeId + "." + fieldId, fieldType, fieldTOrgType);
    String field = nodeId + "." + fieldId;
    if (operator.equals(Operator.NULL)) {
      return " " + field + " is null";
    } else if (operator.equals(Operator.NOT_NULL)) {
      return " " + field + " is not null";
    } else if (operator.equals(Operator.BETWEEN)) {
      return " "+ field + " between '" + startDate + "' and '" + endDate +"'";
    } else if (operator.equals(Operator.CON)) {
      return " position('" + value +"' in " + field + ")>0";
    }else if (operator.equals(Operator.NCON)) {
      return " position('" + value +"' in " + field + ")=0";
    }else {
      String v = value;
      if (fieldType.equals(FieldType.TEXT) || fieldType.equals(FieldType.DATE)) {
        v = "'" + value + "'";
      }
      return " " + field + operator.toString() + v;
    }

  }

}
