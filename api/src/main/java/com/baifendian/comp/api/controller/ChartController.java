package com.baifendian.comp.api.controller;

import com.baifendian.comp.api.dto.chart.ChartInfo;
import com.baifendian.comp.api.dto.chart.ChartInfoDTO;
import com.baifendian.comp.api.dto.chart.ChartLinkParam;
import com.baifendian.comp.api.dto.chart.CreateChartReqDTO;
import com.baifendian.comp.api.dto.chart.PutChartReqDTO;
import com.baifendian.comp.api.service.ChartNewService;
import com.baifendian.comp.api.service.util.FileExportUtil;
import com.baifendian.comp.common.annotation.ParamProperty;
import com.baifendian.comp.common.annotation.ParamRequest;
import com.baifendian.comp.common.exception.BiException;
import com.baifendian.comp.common.structs.chart.FilterInner;
import com.baifendian.comp.common.structs.chart.QueryChart;
import com.baifendian.comp.common.structs.chart.QueryChartFilter;
import com.baifendian.comp.common.structs.dash.DashElementParam;
import com.baifendian.comp.common.utils.HttpUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChartController {

  @Autowired
  private ChartNewService chartNewService;

  @Autowired
  private FileExportUtil fileExportUtil;

  /**
   * 创建一个图表
   */
  @PostMapping("/charts")
  public ChartInfo postChart(@RequestAttribute("userId") String userId,
      @RequestBody CreateChartReqDTO postChartReqDTO) {

    return chartNewService
        .createCharts(postChartReqDTO.getDashId(), userId, postChartReqDTO.getTableId()
            , postChartReqDTO.getName(), postChartReqDTO.getDesc(), postChartReqDTO.getElements(),
            postChartReqDTO.getChartType());
  }

  /**
   * 修改一个图表
   */
  @PutMapping("/charts/{chartId}")
  public ChartInfoDTO putChart(@RequestAttribute("userId") String userId,
      @RequestBody PutChartReqDTO putChartReqDTO,
      @PathVariable("chartId") String chartId) {

    return chartNewService
        .modifyChart(userId, chartId, putChartReqDTO.getName(),
            putChartReqDTO.getDesc(), putChartReqDTO.getChartType(), putChartReqDTO.getMeta());
  }

  /**
   * 移动一个图表
   */
  @PostMapping("/charts-move/{chartId}")
  public ChartInfo moveChart(@RequestAttribute("userId") String userId,
      @PathVariable("chartId") String chartId, @RequestBody MoveChartParam moveChartParam) {
    return chartNewService
        .moveChart(userId, moveChartParam.getDashId(), chartId, moveChartParam.getCharts());
  }

  /**
   * 复制一个图表
   */
  @PostMapping("/charts-copy/{chartId}")
  public ChartInfo copyChart(@RequestAttribute("userId") String userId,
      @PathVariable("chartId") String chartId, @RequestBody CopyChartParam copyChartParam) {
    return chartNewService
        .copyChart(userId, copyChartParam.getDashId(), chartId, copyChartParam.getName(),
            copyChartParam.getCharts());
  }

  /**
   * 删除一个图表
   */
  @DeleteMapping("/charts/{chartId}")
  public void deleteChart(@RequestAttribute("userId") String userId,
      @PathVariable("chartId") String chartId) {

    chartNewService.deleteChart(userId, chartId);
  }

  /**
   * 查询一个图表数据
   */
  @GetMapping("/charts/{chartId}")
  public ChartInfoDTO getDataChart(@RequestAttribute("userId") String userId,
      @PathVariable("chartId") String chartId, HttpServletRequest request,
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
    return chartNewService.getChartData(userId, queryChart,
        HttpUtil.listParam(request, "dashFilter", QueryChartFilter.class), chartLinkParam, false);
  }


  /**
   * download一个图表数据
   */
  @ResponseBody
  @GetMapping("/download/charts/{chartId}")
  public ResponseEntity<Resource> downloadDataChart(@RequestAttribute("userId") String userId,
      @PathVariable("chartId") String chartId, HttpServletRequest request,
      @RequestParam(value = "drillDownFieldId", required = false) String drillDownFieldId,
      @RequestParam(value = "drillLevel", required = false) Integer drillLevel,
      @RequestParam(value = "fileType", required = false) String fileType) {
    QueryChart queryChart = new QueryChart();
    queryChart.setChartId(chartId);
    queryChart.setDrillLevel(drillLevel);
    queryChart.setDrillDownFieldId(drillDownFieldId);
    queryChart.setDrillDownValues(HttpUtil.listParam(request, "drillDownValues", String.class));
    queryChart.setFilterInner(HttpUtil.listParam(request, "filterInner", FilterInner.class));
    List<ChartLinkParam> chartLinkParam = HttpUtil
        .listParam(request, "linkValue", ChartLinkParam.class);
    ChartInfoDTO data = chartNewService.getChartData(userId, queryChart,
        HttpUtil.listParam(request, "dashFilter", QueryChartFilter.class), chartLinkParam, true);

    switch (fileType) {
      case "CSV": {
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=chart_" + data.getDashName() + "_" + data.getName() + ".csv")
            .body(new ByteArrayResource(
                fileExportUtil.createCsv(data.getTitles(), data.getData()).getBytes()));
      }

      case "EXCEL": {
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename="+ data.getId() + ".xls")
            .body(new ByteArrayResource(
                fileExportUtil.createExcel(data.getTitles(), data.getData())));
      }
    }

    throw new BiException("暂未支持的文件类型");
  }

}

@Setter
@Getter
@ParamRequest
class MoveChartParam {

  @ParamProperty
  private String dashId;

  private List<DashElementParam> charts;
}

@Setter
@Getter
@ParamRequest
class CopyChartParam {

  @ParamProperty
  private String dashId;

  @ParamProperty
  private String name;

  private List<DashElementParam> charts;
}
