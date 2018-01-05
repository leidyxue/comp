package com.baifendian.comp.api.controller;

import com.baifendian.comp.api.dto.dashboard.DashDetail;
import com.baifendian.comp.api.dto.dashboard.DashInfo;
import com.baifendian.comp.api.dto.dashboard.DashLinkParam;
import com.baifendian.comp.api.dto.dashboard.DashUsedTable;
import com.baifendian.comp.api.dto.dashboard.PostDashReqDTO;
import com.baifendian.comp.api.dto.dashboard.PutDashConfigReqDTO;
import com.baifendian.comp.api.dto.dashboard.PutDashFiltersReqDTO;
import com.baifendian.comp.api.dto.dashboard.PutDashReqDTO;
import com.baifendian.comp.api.service.DashboardService;
import com.baifendian.comp.common.annotation.ParamProperty;
import com.baifendian.comp.common.annotation.ParamRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashController {

  @Autowired
  private DashboardService dashboardService;

  /**
   * 创建一个仪表盘
   */
  @PostMapping("/dashboards")
  public DashInfo postDashboard(@RequestAttribute(value = "userId") String userId,
      @RequestBody PostDashReqDTO postDashReqDTO) {
    return dashboardService.createDash(userId, postDashReqDTO.getName(), postDashReqDTO.getDesc(),
        postDashReqDTO.getProjectId());
  }

  /**
   * 修改一个仪表盘的基本信息
   */
  @PutMapping("/dashboards/{dashId}")
  public DashInfo putDashboard(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId,
      @RequestBody PutDashReqDTO putDashReqDTO) {
    return dashboardService
        .modifyDash(userId, dashId, putDashReqDTO.getName(), putDashReqDTO.getDesc());
  }

  /**
   * 删除一个仪表盘
   */
  @DeleteMapping("/dashboards/{dashId}")
  public void deleteDashboard(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId) {
    dashboardService.deleteDash(userId, dashId);
    //dashService.deleteDash(userId, dashId);
  }

  @PostMapping("/dashboards-copy/{dashId}")
  public DashInfo cpDashboard(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId, @RequestBody DashboardCopyParam param) {

    return dashboardService.copyDash(userId, dashId, param.getName(), param.getDesc(), param.getProjectId());
  }

  /**
   * 修改一个仪表盘盘的布局信息
   */
  @PutMapping("/dashboards/{dashId}/config")
  public DashDetail putDashboardConfig(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId,
      @RequestBody PutDashConfigReqDTO putDashConfigReqDTO) {
    return dashboardService.modifyDashConf(userId, dashId, putDashConfigReqDTO);
  }

  /**
   * 获取一个仪表盘的详细信息
   */
  @GetMapping("/dashboards/{dashId}")
  public DashDetail getDashboard(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId) {
    return dashboardService.queryDash(userId, dashId);
  }

  /**
   * 修改一个仪表盘的全局过滤条件
   */
  @PostMapping("/dashboards/{dashId}/filters")
  public DashDetail putDashboardFilters(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId,
      @RequestBody PutDashFiltersReqDTO putDashFiltersReqDTO) {
    return dashboardService.updateFilter(userId, dashId, putDashFiltersReqDTO.getFilters());
  }

  /**
   * 获取一个仪表盘下所有的工作表信息
   */
  @GetMapping("/dashboards/{dashId}/tables")
  public DashUsedTable getDashboardTables(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId) {
    return dashboardService.findDashTables(userId, dashId);
  }


  @PatchMapping("/dashboards/{dashId}/coordination/{chartId}")
  public DashDetail dashboardLinkInfo(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId, @PathVariable("chartId") String chartId,
      @RequestBody DashLinkParam param) {
    return dashboardService.addLinks(userId, dashId, chartId, param.getLinks());
  }

  @DeleteMapping("/dashboards/{dashId}/coordination/{chartId}")
  public DashDetail delLinkInfo(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId, @PathVariable("chartId") String chartId) {
    return dashboardService.removeLinks(userId, dashId, chartId);
  }
}

@Setter
@Getter
@ParamRequest
class DashboardCopyParam {

  @ParamProperty
  private String name;
  private String desc;
  private String projectId;
}



