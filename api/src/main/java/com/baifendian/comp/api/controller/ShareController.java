package com.baifendian.comp.api.controller;

import com.baifendian.comp.api.dto.chart.ChartInfoDTO;
import com.baifendian.comp.api.dto.chart.ChartLinkParam;
import com.baifendian.comp.api.dto.dashboard.DashDetail;
import com.baifendian.comp.api.dto.dashboard.DashUsedTable;
import com.baifendian.comp.api.dto.share.DashShareId;
import com.baifendian.comp.api.dto.share.ShareInfo;
import com.baifendian.comp.api.dto.table.RangeData;
import com.baifendian.comp.api.service.ChartNewService;
import com.baifendian.comp.api.service.ShareService;
import com.baifendian.comp.api.service.TableService;
import com.baifendian.comp.common.structs.chart.FilterInner;
import com.baifendian.comp.common.structs.chart.QueryChart;
import com.baifendian.comp.common.structs.chart.QueryChartFilter;
import com.baifendian.comp.common.utils.HttpUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShareController {

  @Autowired
  private ShareService shareService;

  @Autowired
  private ChartNewService chartNewService;

  @Autowired
  private TableService tableService;

  /**
   * 发布一个仪表盘
   */
  @PostMapping("/dashboards/{dashId}/share")
  public DashShareId shareDashboard(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId) {
    return shareService.dashShare(userId, dashId);
  }

  /**
   * 取消发布一个仪表盘
   */
  @DeleteMapping("/dashboards/{dashId}/share/{shareId}")
  public void delShareDashboard(@RequestAttribute(value = "userId") String userId,
      @PathVariable("shareId") String shareId) {
    shareService.delShare(userId, shareId);
  }

  /**
   * 查询一个仪表盘的发布状态
   */
  @GetMapping("/dashboards/{dashId}/share")
  public ShareInfo getShareInfo(@RequestAttribute(value = "userId") String userId,
      @PathVariable("dashId") String dashId){
    return shareService.getDashShare(userId, dashId);
  }

  /**
   * 查询一个发布的仪表盘结果
   */
  @GetMapping("/dashboards-share")
  public DashDetail getShareDash(@RequestParam("shareId")String shareId){
    return shareService.getShareDashDetail(shareId);
  }

  /**
   * 查询一个共享图表的数据
   */
  @GetMapping("/charts-share")
  public ChartInfoDTO getShareChart(@RequestParam("chartId") String chartId,
      @RequestParam("shareId") String shareId, HttpServletRequest request,
      @RequestParam(value = "drillDownFieldId", required = false) String drillDownFieldId,
      @RequestParam(value = "drillLevel", required = false) Integer drillLevel) {
    QueryChart queryChart = new QueryChart();
    queryChart.setChartId(chartId);
    queryChart.setDrillLevel(drillLevel);
    queryChart.setDrillDownFieldId(drillDownFieldId);
    queryChart.setDrillDownValues(HttpUtil.listParam(request, "drillDownValues", String.class));
    queryChart.setFilterInner(HttpUtil.listParam(request, "filterInner", FilterInner.class));
    List<ChartLinkParam> chartLinkParam = HttpUtil
        .listParam(request, "linkValue", ChartLinkParam.class);

    return chartNewService
        .getShareChartData(shareId, queryChart, HttpUtil.listParam(request, "dashFilter", QueryChartFilter.class),
            chartLinkParam);
  }

  /**
   * 分享仪表盘的全局过滤范围查询
   */
  @GetMapping("/share-range")
  public RangeData getShareDash(@RequestParam("shareId")String shareId, @RequestParam(value = "filterId")String filterId){
    return tableService.shareFilterData(shareId, filterId);
  }

  /**
   * 分享仪表盘的全局过滤范围查询
   */
//  @GetMapping("/share-inner-range")
//  public RangeData getShareDashInner(@RequestParam("shareId")String shareId, @RequestParam(value = "filterId")String filterId){
//    return tableService.shareFilterData(shareId, filterId);
//  }

  /**
   * 查询发布仪表使用的 table 的信息
   */
  @GetMapping("/dashboards-share/{shareId}/tables")
  public DashUsedTable queryDsTables(@PathVariable("shareId")String shareId){
    return shareService.getDsTables(shareId);
  }
}
