package com.baifendian.comp.rpc.job;

import static com.baifendian.comp.rpc.common.Params.END_DATE;
import static com.baifendian.comp.rpc.common.Params.REAL_START_DATE;
import static com.baifendian.comp.rpc.common.Params.START_DATE;

import com.baifendian.comp.api.dto.comparison.ExecData;
import com.baifendian.comp.api.service.ComparisonService;
import com.baifendian.comp.rpc.SpringUtil;
import com.baifendian.comp.rpc.email.MailSendUtil;
import java.util.Arrays;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CronJob implements Job {

  private static final Logger LOGGER = LoggerFactory.getLogger(CronJob.class);

  private ComparisonService comparisonService;

  public CronJob() {
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
    String[] emails = jobDataMap.getString("emails").split(",");
    String startDate = jobDataMap.getString(START_DATE);
    String realStartDate = jobDataMap.getString(REAL_START_DATE);
    String endDate = jobDataMap.getString(END_DATE);
    LOGGER.info("TaskId is: {}", taskId);
    LOGGER.info("Emails are: {}", Arrays.toString(emails));

    ExecData execData = comparisonService.execTask(taskId);
    String content = "您的比对任务执行结束，任务编号：" + taskId +
        " \n任务名称：" + execData.getTaskName() +
        " \n执行结束时间：" +execData.getEndTime()
        + "\n任务状态：" + execData.getStatus();

    MailSendUtil.sendMails(
        Arrays.asList(emails),
        "您的比对任务执行结束, 任务名称:"+ execData.getTaskName(),
        content
    );
  }
}
