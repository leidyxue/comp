package com.baifendian.comp.api.controller;

import com.baifendian.comp.api.dto.comparison.CompTaskInfo;
import com.baifendian.comp.api.dto.comparison.ScheduleDTO;
import com.baifendian.comp.api.dto.comparison.TableCompRelationDTO;
import com.baifendian.comp.api.dto.comparison.TaskDTO;
import com.baifendian.comp.api.dto.comparison.TaskResultData;
import com.baifendian.comp.api.dto.comparison.TaskResultsDTO;
import com.baifendian.comp.api.service.ComparisonService;
import com.baifendian.comp.common.enums.ExecStatus;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonSchedule;
import com.bfd.systemmanager.client.holder.ShiroUserHolder;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xuelei on 2017/11/14 0014.
 */
@RestController
public class ComparisonController {
  @Autowired
  ComparisonService comparisonService;

//  @GetMapping("/comparisonTasks")
//  public CompTaskInfo getTasks(@RequestAttribute(value = "user") ShiroUser shiroUser){
//    return comparisonService.getTasks(shiroUser.getId(), shiroUser.getName());
//  }

  @GetMapping("/comparisonTasks/{currentPage}/{pageSize}")
  public CompTaskInfo getTasks(
      @PathVariable("currentPage") String currentPage,
      @PathVariable("pageSize") String pageSize,
      @RequestParam(value = "keyWord", required = false) String keyWord,
      @RequestParam(value = "status", required = false) ExecStatus status,
      @RequestParam(value = "timeOrder", required = false) String timeOrder,
      @RequestParam(value = "startDate", required = false) Long startDate,
      @RequestParam(value = "endDate", required = false) Long endDate){

//    return comparisonService.getTasks(ShiroUserHolder.getUser().getId(),
//        ShiroUserHolder.getUser().getName(), Integer.parseInt(currentPage),
//        Integer.parseInt(pageSize), keyWord, status, timeOrder, startDate, endDate);
    Date start = startDate == null? null : new Date(startDate);
    Date end = endDate == null? null : new Date(endDate);
    return comparisonService.getTasks(ShiroUserHolder.getUser().getId(),
        ShiroUserHolder.getUser().getName(), Integer.parseInt(currentPage),
        Integer.parseInt(pageSize), keyWord, status, timeOrder, start, end);
  }

  @PostMapping("/comparisonTasks")
  public TaskDTO createTask(@RequestAttribute(value = "userId") String userId,
      @RequestBody TaskDTO taskDTO){
    return comparisonService.createTask(userId, taskDTO.getName(), taskDTO.getNodes(), taskDTO.getRelations());
  }

  @GetMapping("/comparisonTasks/{taskId}")
  public TaskDTO getTaskInfo(@RequestAttribute(value = "userId") String userId,
      @PathVariable("taskId") String taskId){
    return comparisonService.getTask(taskId);
  }

  @PutMapping("/comparisonTasks/{taskId}")
  public void modifyTask(@RequestAttribute(value = "userId") String userId,
      @PathVariable("taskId") String taskId,
      @RequestBody TaskDTO taskDTO){
    comparisonService.modifyTask(userId, taskId, taskDTO.getName(), taskDTO.getNodes(), taskDTO.getRelations());
  }

  @PutMapping("/comparisonTasks/rename/{taskId}/{name}")
  public void modifyTaskName(@RequestAttribute(value = "userId") String userId,
      @PathVariable("taskId") String taskId,
      @PathVariable("name") String name){
    comparisonService.modifyName(userId, taskId, name);
  }

  @DeleteMapping("/comparisonTasks/{taskId}")
  public void delete(@RequestAttribute(value = "userId") String userId,
      @PathVariable("taskId") String taskId){
    comparisonService.deleteTask(userId, taskId);
  }

  @PostMapping("/comparisonTasks/{taskId}/exec")
  public String execTask(@RequestAttribute(value = "userId") String userId,
      @PathVariable("taskId") String taskId){
    return comparisonService.exec(userId, taskId);
  }

  @GetMapping("/comparisonTasks/{taskId}/schedule")
  public ComparisonSchedule querySchedule(@RequestAttribute(value = "userId") String userId,
      @PathVariable("taskId") String taskId){
    return comparisonService.querySchedule(userId, taskId);
  }

  @PostMapping("/comparisonTasks/{taskId}/schedule")
  public ComparisonSchedule saveSchedule(@RequestAttribute(value = "userId") String userId,
      @PathVariable("taskId") String taskId,
      @RequestBody ScheduleDTO scheduleDTO){
    return comparisonService.saveSchedule(userId, taskId, scheduleDTO.getWarnEmail(),scheduleDTO.getStatus(), scheduleDTO.getCrontab());
  }

  //比对结果列表
  @GetMapping("/comparisonTasks/{taskId}/results")
  public TaskResultsDTO queryResults(
      @PathVariable("taskId") String taskId){
    return comparisonService.queryResults(ShiroUserHolder.getUser().getName(), taskId);
  }

  //预览数据
  @GetMapping("/comparisonResults/{resultId}/preview")
  public TaskResultData previewResult(@RequestAttribute(value = "userId") String userId,
      @PathVariable("resultId") String resultId){
    return comparisonService.previewResult(userId, resultId);
  }


  @GetMapping("/tables/{tableId}/compRelations")
  public TableCompRelationDTO saveSchedule(@RequestAttribute(value = "userId") String userId,
      @PathVariable("tableId") String tableId){
    return comparisonService.queryTableCompInfo(userId, tableId);
  }

  @DeleteMapping("/comparisonResults/{resultId}")
  public void deleteResult(@RequestAttribute(value = "userId") String userId,
      @PathVariable("resultId") String resultId){
    comparisonService.deleteResult(userId, resultId);
  }
}
