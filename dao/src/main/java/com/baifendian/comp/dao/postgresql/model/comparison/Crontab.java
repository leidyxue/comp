package com.baifendian.comp.dao.postgresql.model.comparison;

import com.baifendian.comp.common.enums.ScheduleType;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Crontab {
  private Date startDate;
  private Date endDate;
  private ScheduleType type;

  @JsonFormat(pattern = "HH:mm:ss")
  private Date dayTime;
}
