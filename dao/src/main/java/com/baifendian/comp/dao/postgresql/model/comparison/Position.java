package com.baifendian.comp.dao.postgresql.model.comparison;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by xuelei on 2017/11/13 0013.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Position {

  /**
   * 横坐标
   */
  private Double x;

  /**
   * 纵坐标
   */
  private Double y;
}
