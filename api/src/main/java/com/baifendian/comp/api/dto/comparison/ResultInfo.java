package com.baifendian.comp.api.dto.comparison;

import com.baifendian.comp.common.enums.ExecStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/21 0021.
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultInfo {
  private String id;
  private Date startExecTime;
  private Date endExecTime;
  private ExecStatus status;
}
