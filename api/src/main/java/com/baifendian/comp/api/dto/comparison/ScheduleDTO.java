package com.baifendian.comp.api.dto.comparison;

import com.baifendian.comp.common.enums.ScheduleStatus;
import com.baifendian.comp.dao.postgresql.model.comparison.Crontab;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {
  private String taskId;
  private ScheduleStatus status;
  private String warnEmail;
  private Crontab crontab;
}
