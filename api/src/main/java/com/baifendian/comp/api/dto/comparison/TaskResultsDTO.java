package com.baifendian.comp.api.dto.comparison;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by xuelei on 2017/11/21 0021.
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResultsDTO {

  private String taskId;
  private String taskName;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date modifyTime;

  private String owner;

  private List<ResultInfo> results;
}
