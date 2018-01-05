package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.comp.common.enums.ScheduleStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by xuelei on 2017/11/19 0019.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ComparisonSchedule {
  private String taskId;
  private Date createTime;
  private ScheduleStatus status;
  private String warnEmail;
  private Crontab crontab;
}
