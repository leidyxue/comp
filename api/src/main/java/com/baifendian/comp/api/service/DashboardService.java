package com.baifendian.comp.api.service;

import com.baifendian.comp.api.dto.DtoUtil;
import com.baifendian.comp.api.dto.dashboard.DashDetail;
import com.baifendian.comp.api.dto.dashboard.DashFilterParam;
import com.baifendian.comp.api.dto.dashboard.DashFilterParam.DashFilterTable;
import com.baifendian.comp.api.dto.dashboard.DashInfo;
import com.baifendian.comp.api.dto.dashboard.DashLinkDetail;
import com.baifendian.comp.api.dto.dashboard.DashLinkDto;
import com.baifendian.comp.api.dto.dashboard.DashUsedTable;
import com.baifendian.comp.api.dto.dashboard.PutDashConfigReqDTO;
import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.api.dto.error.UnAuthorizedException;
import com.baifendian.comp.api.service.audit.AuditUtil;
import com.baifendian.comp.api.service.util.ChartUtilNew;
import com.baifendian.comp.api.service.util.DashboardUtil;
import com.baifendian.comp.api.service.util.I18nUtil;
import com.baifendian.comp.api.service.util.TableUtilNew;
import com.baifendian.comp.common.enums.ElementType;
import com.baifendian.comp.common.structs.dash.DashElementParam;
import com.baifendian.comp.common.utils.GenIdUtil;
import com.baifendian.comp.dao.enums.AuditPageName;
import com.baifendian.comp.dao.postgresql.mapper.chart.ChartFieldMapper;
import com.baifendian.comp.dao.postgresql.mapper.chart.ChartNewMapper;
import com.baifendian.comp.dao.postgresql.mapper.chart.InnerFilterMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashElementMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashFilterMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashLinkMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashNewMapper;
import com.baifendian.comp.dao.postgresql.model.ChartAllData;
import com.baifendian.comp.dao.postgresql.model.DashAllData;
import com.baifendian.comp.dao.postgresql.model.chart.Chart;
import com.baifendian.comp.dao.postgresql.model.dash.DashElement;
import com.baifendian.comp.dao.postgresql.model.dash.DashFilter;
import com.baifendian.comp.dao.postgresql.model.dash.DashLink;
import com.baifendian.comp.dao.postgresql.model.dash.DashMetaData;
import com.baifendian.comp.dao.postgresql.model.dash.Dashboard;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

  private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

  @Autowired
  private DashboardUtil dashUtil;
  @Autowired
  private ChartUtilNew chartUtil;
  @Autowired
  private TableUtilNew tableUtil;
  @Autowired
  private DashNewMapper dashMapper;

  @Autowired
  private ChartNewMapper chartMapper;

  @Autowired
  private DashElementMapper dashElementMapper;
  @Autowired
  private DashFilterMapper dashFilterMapper;
  @Autowired
  private DashLinkMapper dashLinkMapper;

  public DashInfo createDash(String owner, String name, String desc, String projectId) {
    dashUtil.checkProject(owner, projectId);

    Dashboard dashboard = Dashboard.builder()
        .id(GenIdUtil.genDashId())
        .desc(desc)
        .name(name)
        .owner(owner)
        .webData(new DashMetaData())
        .projectId(projectId)
        .build();

    // 名称重复，利用外键
    try {
      dashMapper.insert(dashboard);
    } catch (DuplicateKeyException e) {
      throw new PreFailedException(e, "com.baifendian.bi.api.common.duplicate",
          new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.dash"));
    }

    AuditUtil.pushInsert(AuditPageName.Thematic, AuditUtil.getAuditData(dashboard));

    return new DashInfo(dashboard);
  }

  /**
   * 修改仪表盘的基本信息
   */
  public DashInfo modifyDash(String userId, String dashId, String name, String desc) {
    Dashboard dashboard = dashUtil.createDashboard(userId, dashId);
    String oldData = AuditUtil.getAuditData(dashboard);

    dashboard.setName(name);
    dashboard.setDesc(desc);
    dashboard.setModifyTime(new Date());

    try {
      dashMapper.update(dashboard);
    } catch (DuplicateKeyException e) {
      throw new PreFailedException(e, "com.baifendian.bi.api.common.duplicate", I18nUtil.dashName());
    }
    AuditUtil.pushUpdate(AuditPageName.Thematic, oldData, AuditUtil.getAuditData(dashboard));
    return new DashInfo(dashboard);
  }

  /**
   * 删除一个仪表盘
   */
  public void deleteDash(String userId, String dashId) {
    Dashboard dashboard = dashUtil.createDashboard(userId, dashId);

    dashMapper.deleteById(dashId);
    AuditUtil.pushDel(AuditPageName.Thematic, AuditUtil.getAuditData(dashboard));
    //dashUtil.deleteDashboard(dashId);
  }


  @Autowired
  private ChartFieldMapper chartFieldMapper;

  @Autowired
  private InnerFilterMapper innerFilterMapper;

  /**
   * 复制一个仪表盘
   */
  @Transactional
  public DashInfo copyDash(String userId, String dashId, String name, String desc,
      String projectId) {
    dashUtil.checkProject(userId, projectId);

    DashAllData oldDashAllData = dashUtil.createDashAll(userId, dashId);
    //.copy(name, desc, projectId);

    String newDashID = GenIdUtil.genDashId();

    DashAllData dashAllData = new DashAllData();
    dashAllData.setDash(Dashboard.builder()
        .id(newDashID)
        .desc(desc)
        .webData(oldDashAllData.getDash().getWebData())
        .name(name)
        .owner(oldDashAllData.getDash().getOwner())
        .projectId(projectId)
        .build());

    dashMapper.insert(dashAllData.getDash());

    List<ChartAllData> newCharts = new ArrayList<>();
    Map<String, String> chartIdMap = new HashMap<>();
    for (ChartAllData chart : chartUtil.createDashChartDetail(userId, dashId)) {
      ChartAllData newChart = chart.copy(newDashID, chart.getChart().getName());
      newCharts.add(newChart);
      chartIdMap.put(chart.chartId(), newChart.chartId());
      chartUtil.insertChartDetail(newChart);

//      chartMapper.insert(newChart.getChart());
//
//      newChart.getChartFields().forEach(cf -> chartFieldMapper.insert(cf));
//      newChart.getInnerFilterFields().stream()
//          .map(id -> new InnerFilter(newChart.chartId(), id))
//          .forEach(innerFilter -> innerFilterMapper.insert(innerFilter));
    }

    //
    dashAllData.setElements(oldDashAllData.getElements().stream()
        .map(ele -> {
          if (ele.getType() == ElementType.CHART) {
            ele.setId(chartIdMap.get(ele.getId()));
          }
          ele.setDashId(newDashID);
          return ele;
        }).collect(Collectors.toList()));
    dashAllData.getElements().forEach(ele -> dashElementMapper.insert(ele));

    dashAllData.setLinks(oldDashAllData.getLinks().stream()
        .map(link -> {
          link.setDashId(newDashID);
          link.setChartId(chartIdMap.get(link.getChartId()));
          link.setLinkChartId(chartIdMap.get(link.getLinkChartId()));
          return link;
        }).collect(Collectors.toList()));
    dashAllData.getLinks().forEach(link -> dashLinkMapper.insert(link));

    dashAllData.setFilters(oldDashAllData.getFilters().stream()
        .map(df -> {
          df.setDashId(newDashID);
          df.setChartId(chartIdMap.get(df.getChartId()));
          return df;
        }).collect(Collectors.toList()));
    dashAllData.getFilters().forEach(filter -> dashFilterMapper.insert(filter));

    AuditUtil.pushDel(AuditPageName.Thematic, AuditUtil.getAuditData(dashAllData.getDash()));
    return new DashInfo(dashAllData.getDash());
  }

  /**
   * 修改一个仪表盘布局信息
   */
  @Transactional
  public DashDetail modifyDashConf(String userId, String dashId,
      PutDashConfigReqDTO putDashConfigReqDTO) {
    DashAllData dashAllData = dashUtil.createDashAll(userId, dashId);

    String oldData = AuditUtil.getAuditData(dashAllData.getDash());

    Dashboard dashboard = dashAllData.getDash();
    dashboard.getWebData().setStyleConf(putDashConfigReqDTO.getStyleConf());
    dashboard.setModifyTime(new Date());

    //TODO 这里需要检查一下新，旧chart元素是否相同，不相同的不与更新
    //TODO  check element
    dashElementMapper.deleteByDash(dashId);

    dashAllData.setElements(
        putDashConfigReqDTO.getElements().stream().map(ele -> elementChange(dashId, ele)).collect(
            Collectors.toList()));
    dashAllData.getElements().forEach(ele -> dashElementMapper.insert(ele));

    dashMapper.update(dashboard);
    //TODO 配置可信度检查暂时不做。

    AuditUtil.pushUpdate(AuditPageName.Thematic, oldData, AuditUtil.getAuditData(dashAllData.getDash()));

    return createDashDetail(dashAllData);
  }

  /**
   * 查询一个仪表盘
   */
  public DashDetail queryDash(String userId, String dashId) {
    return createDashDetail(dashUtil.createDashAll(userId, dashId));
  }

  /**
   * 设置全局筛选
   */
  public DashDetail updateFilter(String userId, String dashId,
      List<DashFilterParam> dashFilterParams) {
    DashAllData dashAllData = dashUtil.createDashAll(userId, dashId);
    List<Chart> dashChartList = chartUtil.createDashChart(userId, dashId);
    Map<String, String> tableChartIdMap = dashChartList.stream()
        .collect(Collectors.toMap(Chart::getId, Chart::getTableId));

    List<DashFilter> dashFilters = new ArrayList<>();
    //TODO 过滤条件有效性检查暂时不做。

    for (DashFilterParam param : dashFilterParams) {
      for (String chartId : param.getCharts()) {
        Optional<DashFilterTable> ft = param.getTables().stream()
            .filter(t -> t.getTableId().equals(tableChartIdMap.get(chartId)))
            .findFirst();
        if (!ft.isPresent()) {
          continue;
        }

        DashFilter dashFilter = DashFilter.builder()
            .dashId(dashId)
            .chartId(chartId)
            .id(param.getId())
            .name(param.getName())
            .tableId(tableChartIdMap.get(chartId))
            .fieldId(ft.get().getFieldId())
            .fieldType(param.getType())
            .build();
        dashFilters.add(dashFilter);
      }
    }

    dashAllData.setFilters(dashFilters);

    dashFilterMapper.deleteByDash(dashId);

    dashFilters.forEach(t -> dashFilterMapper.insert(t));

    return createDashDetail(dashUtil.createDashAll(userId, dashId));
  }

  /**
   * 获取一个仪表盘下内所有的工作表
   */
  public DashUsedTable findDashTables(String userId, String dashId){
    List<Chart> chartList = chartMapper.findByDId(dashId);

    if (CollectionUtils.isEmpty(chartList)){
      DashUsedTable usedTable = new DashUsedTable();
      usedTable.setDashId(dashId);
      return usedTable;
    }

    if (chartList.stream().anyMatch(chart -> !StringUtils.equals(chart.getOwner(), userId))){
      throw new UnAuthorizedException("com.baifendian.bi.api.common.notOwner", I18nUtil.dashName(), dashId);
    }

    Set<String> tableIds = chartList.stream()
        .map(Chart::getTableId)
        .collect(Collectors.toSet());

    return DtoUtil.createDashUsedTable(dashId, tableUtil.createTables(tableIds));
  }

  /**
   * 图表联动设置
   */
  @Transactional
  public DashDetail addLinks(String userId, String dashId, String chartId,
      List<DashLinkDto> links) {
    DashAllData dashAllData = dashUtil.createDashAll(userId, dashId);
    //chartUtil.checkChart(userId, dashId, chartId);

    // TODO check invalid

    dashLinkMapper.deleteByChart(chartId);

    List<DashLink> dashLinks = new ArrayList<>();
    for (DashLinkDto dashLinkParam : links) {
      for (DashLinkDto.LinkField linkField : dashLinkParam.getLinkFields()) {
        DashLink tmp = DashLink.builder()
            .chartId(chartId)
            .dashId(dashId)
            .fieldId(linkField.getFieldId())
            .linkChartId(dashLinkParam.getLinkChartId())
            .linkFieldId(linkField.getLinkFieldId())
            .build();
        dashLinkMapper.insert(tmp);
        dashLinks.add(tmp);
      }
    }

    dashAllData.setLinks(dashLinks);

    return createDashDetail(dashAllData);
  }

  /**
   * 删除图表联动设置
   */
  @Transactional
  public DashDetail removeLinks(String userId, String dashId, String chartId) {
    dashLinkMapper.deleteByChart(chartId);

    DashAllData dashAllData = dashUtil.createDashAll(userId, dashId);
    return createDashDetail(dashAllData);
  }

  public static DashDetail createDashDetail(DashAllData dashAllData) {
    Dashboard dashboard = dashAllData.getDash();

    DashDetail.DashDetailBuilder builder = DashDetail.builder()
        .id(dashboard.getId())
        .projectId(dashboard.getProjectId())
        .projectName(dashboard.getProjectName())
        .modifyTime(dashboard.getModifyTime())
        .createTime(dashboard.getCreateTime())
        .desc(dashboard.getDesc())
        .name(dashboard.getName());

    Map<String, DashLinkDetail> linkDetailMap = new HashMap<>();

    for (DashLink link : dashAllData.getLinks()) {
      DashLinkDetail dashLinkDetail = linkDetailMap.get(link.getChartId());
      if (dashLinkDetail == null) {
        dashLinkDetail = new DashLinkDetail();
        dashLinkDetail.setChartId(link.getChartId());
        dashLinkDetail.setTableId(link.getTableId());
      }
      dashLinkDetail.addLink(link.getLinkChartId(), link.getLinkTableId(), link.getFieldId(),
          link.getLinkFieldId());
      linkDetailMap.put(link.getChartId(), dashLinkDetail);
    }

    DashDetail.DashMeta.DashMetaBuilder metaBuilder = DashDetail.DashMeta.builder()
        .styleConf(dashboard.getWebData().getStyleConf())
        .elements(dashAllData.getElements().stream().map(DashboardService::elementChange).collect(
            Collectors.toList()))
        .filters(
            filterTranform(dashAllData.getFilters()))
        .linkInfos(
            linkDetailMap.entrySet().stream().map(Entry::getValue).collect(Collectors.toList()));

    builder.meta(metaBuilder.build());

    return builder.build();
  }

  public static DashElement elementChange(String dashId,
      DashElementParam dashElement) {
    return DashElement.builder().h(dashElement.getH())
        .dashId(dashId)
        .id(dashElement.getId())
        .settings(dashElement.getSettings())
        .type(dashElement.getType())
        .w(dashElement.getW())
        .x(dashElement.getX())
        .y(dashElement.getY())
        .h(dashElement.getH())
        .build();
  }

  private static DashElementParam elementChange(DashElement dashElement) {
    return DashElementParam.builder().h(dashElement.getH())
        .id(dashElement.getId())
        .settings(dashElement.getSettings())
        .type(dashElement.getType())
        .w(dashElement.getW())
        .x(dashElement.getX())
        .y(dashElement.getY())
        .h(dashElement.getH())
        .tableId(dashElement.getTableId())
        .build();
  }

  private static List<DashFilter> filterParamTranfrom(List<DashFilterParam> filterParams) {
    List<DashFilter> dashFilters = new ArrayList<>();

    for (DashFilterParam param : filterParams) {
      for (DashFilterTable table : param.getTables()) {
//        dashFilters.add(DashFilter.builder()
//        .chartId(param.))
      }
    }
    return dashFilters;
  }

  private static List<DashFilterParam> filterTranform(List<DashFilter> filters) {
    Map<String, DashFilterParam.DashFilterParamBuilder> builderMap = new HashMap<>();

    for (DashFilter filter : filters) {
      DashFilterParam.DashFilterParamBuilder builder = null;
      if (builderMap.containsKey(filter.getId())) {
        builder = builderMap.get(filter.getId());
      } else {
        builder = DashFilterParam.builder()
            .name(filter.getName())
            .type(filter.getFieldType())
            .id(filter.getId());
      }
      builder.table(
          new DashFilterTable(
              filter.getTableId(),
              filter.getFieldId()))
          .chart(filter.getChartId());
      builderMap.put(filter.getId(), builder);
    }

    // TODO fieldId去重
    return builderMap.entrySet().stream().map(entry -> entry.getValue().build())
        .collect(Collectors.toList());
  }
}
