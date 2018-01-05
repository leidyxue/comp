package com.baifendian.comp.rpc.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CronExpress {

  private Date startDate;
  private Date endDate;
  private Date realStartDate;
  private String cron;
}
