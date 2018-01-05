package com.baifendian.comp.common.structs.datasource;

import com.baifendian.comp.common.enums.DSType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Summary {
  /**
   * 数据源类型
   */
  private DSType type;

  /**
   * 出现次数
   */
  private int count;

}
