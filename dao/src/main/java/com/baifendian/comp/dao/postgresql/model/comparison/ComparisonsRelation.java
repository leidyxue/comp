package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.comp.common.enums.CompType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/11 0011.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ComparisonsRelation {
  /**
   * 任务id
   */
  private String taskId;

  /**
   * 结果集节点id
   */
  private String resultId;

  /**
   * 左节点id
   */
  private String leftId;

  /**
   * 右节点id
   */
  private String rightId;

  /**
   * 左节点名称
   */
  private String leftName;

  /**
   * 右节点名称
   */
  private String rightName;

  /**
   * 运算符
   */
  private CompType compType;

  /**
   * 比对字段
   */
  private Conditions conditions;

  private Filter filter;

  /**
   * 左节点选择结果字段信息
   */
  private SelectedFields selectedLeftFields;

  /**
   * 右节点选择结果字段信息
   */
  private SelectedFields selectedRightFields;


}
