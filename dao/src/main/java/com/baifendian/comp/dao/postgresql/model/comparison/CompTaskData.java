package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.comp.common.enums.ExecStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by xuelei on 2017/11/14 0014.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompTaskData {
  /**
   * 比对任务id
   */
  private String id;

  /**
   * 任务名称
   */
  private String name;

  /**
   * 所有者ID
   */
  private String owner;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date modifyTime;

  private ExecStatus status;

  private Date execTime;
  private Date finishedTime;

  private String spendTime;
}
