package com.baifendian.comp.api.service;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.api.service.audit.AuditUtil;
import com.baifendian.comp.api.service.util.I18nUtil;
import com.baifendian.comp.api.service.util.TableUtilNew;
import com.baifendian.comp.api.dto.chart.ChartInfo;
import com.baifendian.comp.api.dto.chart.ChartInfoDTO;
import com.baifendian.comp.api.dto.chart.ChartLinkParam;
import com.baifendian.comp.api.dto.error.ParameterException;
import com.baifendian.comp.api.service.util.ChartUtilNew;
import com.baifendian.comp.api.service.util.DashboardUtil;
import com.baifendian.comp.api.service.util.FieldId2Name;
import com.baifendian.comp.api.service.util.FieldName2Id;
import com.baifendian.comp.common.enums.ChartFieldType;
import com.baifendian.comp.common.enums.ChartType;
import com.baifendian.comp.common.enums.ElementType;
import com.baifendian.comp.common.structs.chart.ChartFieldParam;
import com.baifendian.comp.common.structs.chart.ChartFilter;
import com.baifendian.comp.common.structs.chart.ChartMeta;
import com.baifendian.comp.common.structs.chart.DrillDownLevel;
import com.baifendian.comp.common.structs.chart.FilterInner;
import com.baifendian.comp.common.structs.chart.QueryChart;
import com.baifendian.comp.common.structs.chart.QueryChartFilter;
import com.baifendian.comp.common.structs.dash.DashElementParam;
import com.baifendian.comp.common.utils.GenIdUtil;
import com.baifendian.comp.dao.enums.AuditPageName;
import com.baifendian.comp.dao.jdbc.DrillDownData;
import com.baifendian.comp.dao.jdbc.SQLContext;
import com.baifendian.comp.dao.jdbc.SqlResultData;
import com.baifendian.comp.dao.postgresql.mapper.chart.ChartNewMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashElementMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashFilterMapper;
import com.baifendian.comp.dao.postgresql.mapper.dash.DashLinkMapper;
import com.baifendian.comp.dao.postgresql.model.ChartAllData;
import com.baifendian.comp.dao.postgresql.model.DashAllData;
import com.baifendian.comp.dao.postgresql.model.chart.Chart;
import com.baifendian.comp.dao.postgresql.model.chart.ChartField;
import com.baifendian.comp.dao.postgresql.model.chart.ChartMetaData;
import com.baifendian.comp.dao.postgresql.model.dash.DashElement;
import com.baifendian.comp.dao.postgresql.model.dash.DashFilter;
import com.baifendian.comp.dao.postgresql.model.dash.Dashboard;
import com.baifendian.comp.dao.postgresql.model.table.Field;
import com.baifendian.comp.dao.postgresql.model.table.TableAllData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChartNewService {

  @Autowired
  private DashboardUtil dashUtil;
  @Autowired
  private ChartUtilNew chartUtil;
  @Autowired
  private TableUtilNew tableUtilNew;
  @Autowired
  private ChartNewMapper chartMapper;
  @Autowired
  private DashLinkMapper dashLinkMapper;
  @Autowired
  private DashFilterMapper dashFilterMapper;
  @Autowired
  private DashElementMapper dashElementMapper;

  /**
   * 创建一个图表
   */
  @Transactional
  public ChartInfo createCharts(String dashId, String user, String tableId, String name,
      String desc, List<DashElementParam> elementList, ChartType type) {
    if (CollectionUtils.isEmpty(elementList)) {
      throw ParameterException.emptyException("charts", ArrayList.class);
    }

    //校验先前的元素是否与现在的一致。注意，这里不检测布局内容。
    Dashboard dashboard = dashUtil.createDashboard(user, dashId);

    String chartId = GenIdUtil.genChartId();

    Map<Boolean, List<DashElementParam>> elementMap = elementList.stream()
        .collect(partitioningBy(c -> c.getId() == null && c.getType() == ElementType.CHART));

    List<DashElementParam> newChart = elementMap.get(true);

    //判断需要新建的chart，如果没有异常退出
    if (CollectionUtils.isEmpty(newChart) || newChart.size() > 2) {
      throw new ParameterException("com.baifendian.bi.api.chart.newChart.notFound");
    } else {
      newChart.get(0).setId(chartId);
    }

    List<DashElementParam> srcElements = elementMap.get(false);

    //校验先前的元素是否与现在的一致。注意，这里不检测布局内容。

    Chart chart = Chart.builder()
        .id(chartId)
        .name(name)
        .desc(desc)
        .dashId(dashId)
        .owner(user)
        .tableId(tableId)
        .chartType(type != null ? type : ChartType.TABLE)
        .dashName(dashboard.getName())
        .projectId(dashboard.getProjectId())
        .projectName(dashboard.getProjectName())
        .build();

    chartMapper.insert(chart);

    dashElementMapper.deleteByDash(dashId);

    List<DashElement> dashElements = elementMap.get(false).stream()
        .map(ele -> DashboardService.elementChange(dashId, ele))
        .collect(toList());
    dashElements.add(DashboardService.elementChange(dashId, newChart.get(0)));
    dashUtil.insertDashElements(dashElements);

    AuditUtil.pushInsert(AuditPageName.CHART, AuditUtil.getAuditData(chart));

    return new ChartInfo(chart);
  }

  /**
   * 更新一个chart
   */
  public ChartInfoDTO modifyChart(String userId, String chartId, String name, String desc,
      ChartType type, ChartMeta meta) {
    // TODO 校验目标的 dashboard 的图表布局信息
    ChartAllData chartDetail = chartUtil.createChartDetail(userId, chartId);

    TableAllData table = tableUtilNew.createTable(userId, chartDetail.tableId());

    //List<FieldOld> fields = fieldMapper.findByTId(table.tableId());

    Chart chart = chartDetail.getChart();
    String oldData = AuditUtil.getAuditData(chart);
    //这里对where表达式需要进行特殊处理
    String expression = meta.getFilter().getExpression();

    if (StringUtils.isNotEmpty(expression)) {
      FieldName2Id fieldName2Id = new FieldName2Id(table.getFieldList());
      chart.setFilterExp(fieldName2Id.apply(expression));
    } else {
      chart.setFilterExp(null);
    }

    chart.setChartType(type);
    chart.setTableId(table.tableId());
    chart.setName(name);
    chart.setDesc(desc);
    chart.setWebData(new ChartMetaData(meta.getLevel()
        .stream().map(DrillDownLevel::getStyleConf).collect(Collectors.toList())
    ));
    chart.setModifyTime(new Date());

    chartDetail.setInnerFilterFields(meta.getFilterInner().stream().map(FilterInner::getFieldId)
        .collect(Collectors.toList()));

    List<ChartField> chartFieldList = new ArrayList<>();

    for (int i = 0; i < meta.getDrillDownFields().size(); ++i) {
      ChartFieldParam fieldParam = meta.getDrillDownFields().get(i);
      chartFieldList.add(createChartField(chartId, i, ChartFieldType.DRILL, fieldParam, 0));
    }

    for (int i = 0; i < meta.getLevel().size(); ++i) {
      DrillDownLevel drillDownLevel = meta.getLevel().get(i);
      final int level = i;
      if (drillDownLevel.hasX()) {
        List<ChartFieldParam> xFields = drillDownLevel.getX();

        chartFieldList.addAll(
            IntStream.range(0, drillDownLevel.getX().size())
                .mapToObj(
                    index -> createChartField(chartId, level, ChartFieldType.X, xFields.get(index),
                        index))
                .collect(Collectors.toList()));
      }
      if (drillDownLevel.hasY()) {
        List<ChartFieldParam> yFields = drillDownLevel.getY();

        chartFieldList.addAll(
            IntStream.range(0, yFields.size())
                .mapToObj(
                    index -> createChartField(chartId, level, ChartFieldType.Y, yFields.get(index),
                        index))
                .collect(Collectors.toList()));
      }
      if (drillDownLevel.hasYOpt()) {
        List<ChartFieldParam> yOptFields = drillDownLevel.getYOptional();

        chartFieldList.addAll(
            IntStream.range(0, yOptFields.size())
                .mapToObj(index -> createChartField(chartId, level, ChartFieldType.Y_OPT,
                    yOptFields.get(index), index))
                .collect(Collectors.toList()));
      }
    }

    chartDetail.setChartFields(chartFieldList);

    chartUtil.updateChartDetail(chartDetail);

    AuditUtil.pushUpdate(AuditPageName.CHART, oldData, AuditUtil.getAuditData(chart));

    return createChartInfoDTO(chartDetail, table.getFieldList());
  }

  private ChartInfoDTO createChartInfoDTO(ChartAllData chartAllData, List<Field> fieldList) {
    Chart chart = chartAllData.getChart();
    ChartInfoDTO.ChartInfoDTOBuilder builder = ChartInfoDTO.builder()
        .dashName(chart.getDashName())
        .dashId(chart.getDashId())
        .name(chart.getName())
        .chartType(chart.getChartType())
        .createTime(chart.getCreateTime())
        .desc(chart.getDesc())
        .id(chart.getId())
        .modifyTime(chart.getModifyTime())
        .owner(chart.getOwner())
        .tableId(chart.getTableId())
        .tableName(chart.getTableName());

    ChartMeta chartMeta = new ChartMeta();

    chartMeta.setDrillDownFields(
        chartAllData.getChartFields().stream().filter(cf -> cf.getType() == ChartFieldType.DRILL)
            .sorted((o1, o2) -> {
              if (o1.getLevel() < o2.getLevel()) {
                return -1;
              }
              return 1;
            }).map(this::createChartFieldParam).collect(Collectors.toList()));

    Map<Integer, DrillDownLevel> levelMap = new HashMap<>();

    if (chart.getWebData() != null && CollectionUtils
        .isNotEmpty(chart.getWebData().getStyleConfs())) {
      for (int i = 0; i < chart.getWebData().getStyleConfs().size(); ++i) {
        DrillDownLevel drillDownLevel = new DrillDownLevel();
        drillDownLevel.setStyleConf(chart.getWebData().getStyleConfs().get(i));

        levelMap.put(i, drillDownLevel);
      }
    }

    for (ChartField chartField : chartAllData.getChartFields()) {
      int level = chartField.getLevel();
      if (!levelMap.containsKey(level)) {
        levelMap.put(level, new DrillDownLevel());
      }
      ChartFieldParam fieldParam = createChartFieldParam(chartField);

      switch (chartField.getType()) {
        case X:
          levelMap.get(level).getX().add(fieldParam);
          break;
        case Y:
          levelMap.get(level).getY().add(fieldParam);
          break;

        case Y_OPT:
          levelMap.get(level).getYOptional().add(fieldParam);
      }
    }

    chartMeta.setFilterInner(chartAllData.getInnerFilterFields().stream()
        .map(FilterInner::new).collect(Collectors.toList()));

    FieldId2Name fieldId2Name = FieldId2Name.create(fieldList);
    chartMeta.setFilter(new ChartFilter(fieldId2Name.apply(chart.getFilterExp())));

    List<DrillDownLevel> levelList = new ArrayList<>();

    for (int i = 0; levelMap.containsKey(i); ++i) {
      levelList.add(levelMap.get(i).sort());
    }

    chartMeta.setLevel(levelList);

    return builder.meta(chartMeta).build();
  }

  private ChartFieldParam createChartFieldParam(ChartField chartField) {
    return ChartFieldParam.builder()
        .aliasName(chartField.getAlias())
        .id(chartField.getFieldId())
        .operator(chartField.getOperator())
        .uniqId(chartField.getUniqId())
        .sort(chartField.getSort())
        .type(chartField.getFieldType())
        .build();
  }

  private ChartField createChartField(String chartId, final int level, ChartFieldType type,
      ChartFieldParam fieldParam, int order) {
    return ChartField.builder()
        .chartId(chartId)
        .alias(fieldParam.getAliasName())
        .fieldId(fieldParam.getId())
        .level(level)
        .order(order)
        .operator(fieldParam.getOperator())
        .sort(fieldParam.getSort())
        .uniqId(fieldParam.getUniqId())
        .type(type)
        .fieldType(fieldParam.getType())
        .build();
  }

  /**
   * 删除一个图表
   */
  @Transactional
  public void deleteChart(String userId, String chartId) {
    Chart chart = chartUtil.createChart(userId, chartId);
    // 图表联动信息
    dashLinkMapper.deleteByChart(chartId);
    //全局筛选
    dashFilterMapper.deleteByChart(chartId);

    dashElementMapper.deleteByChart(chartId);

    chartMapper.deleteById(chartId);

    AuditUtil.pushDel(AuditPageName.CHART, AuditUtil.getAuditData(chart));
  }

  @Transactional
  public ChartInfo moveChart(String userId, String dashId, String chartId,
      List<DashElementParam> elementList) {
    Chart chart = chartUtil.createChart(userId, chartId);
    if (StringUtils.equals(chart.getDashId(), dashId)) {
      throw new ParameterException("com.baifendian.bi.api.chart.sameDash");
    }
    String oldData = AuditUtil.getAuditData(chart);

    Dashboard dashboard = dashUtil.createDashboard(userId, dashId);

    // 图表联动信息
    dashLinkMapper.deleteByChart(chartId);
    //全局筛选
    dashFilterMapper.deleteByChart(chartId);
    // ele
    dashElementMapper.deleteByChart(chartId);

    chart.setDashId(dashId);
    chart.setDashName(dashboard.getName());
    chart.setProjectId(dashboard.getProjectId());

    chartMapper.update(chart);

    for (DashElementParam param : elementList) {
      if (param.getId() == null || param.getId().equals(chartId)) {
        param.setId(chart.getId());
        //param.setId(dashId);
      }
    }

    dashElementMapper.deleteByDash(dashId);

    dashUtil.insertDashElements(
        elementList.stream().map(ele -> DashboardService.elementChange(dashId, ele)).collect(
            Collectors.toList()));

    AuditUtil.pushUpdate(AuditPageName.CHART, oldData, AuditUtil.getAuditData(chart));
    return new ChartInfo(chart);
  }

  @Transactional
  public ChartInfo copyChart(String userId, String dashId, String chartId, String newName,
      List<DashElementParam> elementList) {
    // TODO 校验目标的 dashboard 的图表布局信息
    ChartAllData chartAllData = chartUtil.createChartDetail(userId, chartId);
    if (StringUtils.equals(chartAllData.dashId(), dashId)) {
      throw new ParameterException("com.baifendian.bi.api.chart.sameDash");
    }

    ChartAllData newData = chartAllData.copy(dashId, newName);

    for (DashElementParam param : elementList) {
      if (param.getId() == null || param.getId().equals(chartId)) {
        param.setId(newData.chartId());
      }
    }
    dashElementMapper.deleteByDash(dashId);

    chartUtil.insertChartDetail(newData);

    dashUtil.insertDashElements(
        elementList.stream().map(ele -> DashboardService.elementChange(dashId, ele)).collect(
            Collectors.toList()));

    AuditUtil.pushInsert(AuditPageName.CHART, AuditUtil.getAuditData(newData.getChart()));

    return new ChartInfo(newData.getChart());
  }

  public ChartInfoDTO getShareChartData(String shareId, QueryChart query,
      List<QueryChartFilter> dashFilter, List<ChartLinkParam> chartLinkParams) {
    ChartAllData chart = chartUtil.createChartDetail(query.getChartId());
    TableAllData table = tableUtilNew.createTable(chart.tableId());

    DashAllData dashAllData = dashUtil.createDashAll(chart.dashId());

    return getChartData(chart, table, dashAllData, query, dashFilter, chartLinkParams, false);
  }

  public ChartInfoDTO getChartData(String userId, QueryChart query,
      List<QueryChartFilter> dashFilter, List<ChartLinkParam> chartLinkParams, boolean realTitle) {
    ChartAllData chart = chartUtil.createChartDetail(userId, query.getChartId());
    TableAllData table = tableUtilNew.createTable(userId, chart.tableId());

    DashAllData dashAllData = dashUtil.createDashAll(userId, chart.dashId());

    return getChartData(chart, table, dashAllData, query, dashFilter, chartLinkParams, realTitle);
  }

  public ChartInfoDTO getChartData(ChartAllData chart, TableAllData table
      , DashAllData dashAllData, QueryChart query,
      List<QueryChartFilter> dashFilter, List<ChartLinkParam> chartLinkParams, boolean realTitle) {
    if (table.isDeletedTable()){
      throw new PreFailedException("com.bfd.bi.api.common.deleted", I18nUtil.tableName(), table.tableName());
    }

    if (CollectionUtils.isNotEmpty(chartLinkParams)) {
      for (ChartLinkParam chartLinkParam : chartLinkParams) {
        // TODO 检查联动配置是否一致
//        List<DashLink> links = dashData.getLinks().stream()
//            .filter(dl -> StringUtils.equals(dl.getDashId(), chartLinkParam.getChartId()))
//            .collect(toList());

//        if (links.size() != chartLinkParam.getLinkFields().size()) {
//          throw new BiException("com.baifendian.bi.api.chart.link.error");
//        }
        query.setLinkValues(chartLinkParam.getLinkFields().stream()
            .map(lf -> new QueryChart.LinkValue(lf.getFieldId(), lf.getValue())).collect(toList()));
      }
    }

    // 将全局过滤加入到内部过滤
    for (QueryChartFilter queryChartFilter : dashFilter) {
      Optional<DashFilter> filter = dashAllData
          .findDashFilter(queryChartFilter.getId(), chart.chartId());
      if (!filter.isPresent()) {
        continue;
      }

      query.getFilterInner()
          .add(new FilterInner(filter.get().getFieldId(), queryChartFilter.getRange()));
    }

    ChartInfoDTO chartInfoDTO = createChartInfoDTO(chart, table.getFieldList());

    if (CollectionUtils.isNotEmpty(chart.getChartFields())) {
      Integer level = query.getDrillLevel();
      if (level == null) {
        level = 0;
      }

      final int levelNum = level;

      List<ChartField> chartFieldList = chart.getChartFields()
          .stream()
          .filter(cf -> cf.getLevel() == levelNum && cf.getType() != ChartFieldType.DRILL)
          .collect(toList());

      DrillDownData drillDownLevel = new DrillDownData();
      drillDownLevel.addChartFields(chartFieldList);
      drillDownLevel.addDrills(chart.getChartFields()
          .stream().filter(cf -> cf.getType() == ChartFieldType.DRILL)
          .sorted(Comparator.comparing(ChartField::getLevel)).collect(toList()));

      SqlResultData chartData = SQLContext
          .getChartData(table, query, drillDownLevel, chart.getChart().getFilterExp(), realTitle);

      chartInfoDTO.setData(chartData.getDataList());
        chartInfoDTO.setTitles(chartData.getTitles());
      chartInfoDTO.setDataMessage(chartData.getDataMessage());
      chartInfoDTO.setDataStatus(chartData.getDataStatus().ordinal());
    }

    return chartInfoDTO;
  }
}
