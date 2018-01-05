package com.baifendian.comp.api.dto.comparison;

import com.baifendian.comp.common.enums.ExecStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/29 0029.
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecData {
  private String taskName;
  private Date endTime;
  private ExecStatus status;
  private int num;
}
