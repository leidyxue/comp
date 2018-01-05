package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.comp.common.annotation.AuditProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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
public class ComparisonTask {

  /**
   * 比对任务id
   */
  private String id;

  /**
   * 任务名称
   */
  @AuditProperty
  private String name;

  /**
   * 所有者ID
   */
  private String owner;

  /**
   * 创建时间
   */
  @Default
  private Date createTime = new Date();
  /**
   * 修改时间
   */
  @Default
  private Date modifyTime = new Date();

  private String canvas;
}
