package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.bi.engine.enums.FieldType;
import com.baifendian.bi.engine.util.FieldCastUtil;
import com.baifendian.bi.engine.util.SqlUtil;
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
public class SelectedField {

  /**
   * 字段id
   */
  private String id;

  /**
   * 字段别名
   */
  private String alias;

  /**
   * 工作表字段名
   */
  private String name;

  /**
   * 字段原始名称
   */
  private String originName;

  private FieldType type;

  private String orgType;

  public String getSelectName(){
//    return FieldCastUtil.castValue(id, type, orgType);
    return id;
  }

  public String getResultName(){
    if (alias == null){
      return name;
    }
    return alias;
  }
}
