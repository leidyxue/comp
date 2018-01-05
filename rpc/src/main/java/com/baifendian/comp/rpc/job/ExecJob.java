package com.baifendian.comp.rpc.job;

import com.baifendian.comp.api.service.ComparisonService;
import com.baifendian.comp.rpc.SpringUtil;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecJob implements Job {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExecJob.class);

  private ComparisonService comparisonService;

  public ExecJob() {
    this.comparisonService = SpringUtil.getBean(ComparisonService.class);
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobKey jobDetail = context.getJobDetail().getKey();
    LOGGER.info("JobKey is: {}", jobDetail);

    TriggerKey triggerKey = context.getTrigger().getKey();
    LOGGER.info("TriggerKey is: {}", triggerKey);

    JobDataMap jobDataMap = context.getMergedJobDataMap();
    String taskId = jobDataMap.getString("taskId");
    LOGGER.info("TaskId is: {}", taskId);

    comparisonService.execTask(taskId);
    long time = context.getJobRunTime();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(time), ZoneId.systemDefault());
    LOGGER.info("Runtime is: {}", localDateTime.toString());
  }
}
