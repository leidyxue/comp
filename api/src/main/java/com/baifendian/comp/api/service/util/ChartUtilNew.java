package com.baifendian.comp.api.service.util;

import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.dao.postgresql.mapper.chart.ChartFieldMapper;
import com.baifendian.comp.dao.postgresql.mapper.chart.ChartNewMapper;
import com.baifendian.comp.dao.postgresql.mapper.chart.InnerFilterMapper;
import com.baifendian.comp.dao.postgresql.model.ChartAllData;
import com.baifendian.comp.dao.postgresql.model.chart.Chart;
import com.baifendian.comp.dao.postgresql.model.chart.InnerFilter;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChartUtilNew {

  @Autowired
  private ChartNewMapper chartMapper;

  @Autowired
  private ChartFieldMapper chartFieldMapper;

  @Autowired
  private InnerFilterMapper innerFilterMapper;

  @Transactional
  public void updateChartDetail(ChartAllData chartAllData) {
    chartMapper.update(chartAllData.getChart());
    chartFieldMapper.deleteByChart(chartAllData.chartId());
    innerFilterMapper.deleteByChart(chartAllData.chartId());
    if (CollectionUtils.isNotEmpty(chartAllData.getChartFields())) {
      chartFieldMapper.batchInsert(chartAllData.getChartFields());
    }
    if(CollectionUtils.isNotEmpty(chartAllData.getInnerFilterFields())) {
      innerFilterMapper.batchInsert(chartAllData.getInnerFilterFields().stream()
          .map(id -> new InnerFilter(chartAllData.chartId(), id)).collect(Collectors.toList()));
    }
  }

  @Transactional
  public void insertChartDetail(ChartAllData chartAllData) {
    chartMapper.insert(chartAllData.getChart());
    if (CollectionUtils.isNotEmpty(chartAllData.getChartFields())) {
      chartFieldMapper.batchInsert(chartAllData.getChartFields());
    }
    if(CollectionUtils.isNotEmpty(chartAllData.getInnerFilterFields())) {
      innerFilterMapper.batchInsert(chartAllData.getInnerFilterFields().stream()
          .map(id -> new InnerFilter(chartAllData.chartId(), id)).collect(Collectors.toList()));
    }
  }

  public List<Chart> createDashChart(String userId, String dashID) {
    return chartMapper.findByDId(dashID);
  }

  public List<ChartAllData> createDashChartDetail(String userId, String dashID) {
    // TODO
    return chartMapper.findByDId(dashID)
        .stream()
        .map(chart -> {
          ChartAllData chartDetail = new ChartAllData();
          chartDetail.setChart(createChart(userId, chart.getId()));
          chartDetail.setChartFields(chartFieldMapper.findByChartId(chart.getId()));
          chartDetail.setInnerFilterFields(
              innerFilterMapper.findByChartId(chart.getId()).stream().map(InnerFilter::getFieldId)
                  .collect(
                      Collectors.toList()));

          return chartDetail;
        }).collect(Collectors.toList());
  }

  public ChartAllData createChartDetail(String userId, String chartId) {
    ChartAllData chartDetail = new ChartAllData();
    chartDetail.setChart(createChart(userId, chartId));
    chartDetail.setChartFields(chartFieldMapper.findByChartId(chartId));
    chartDetail.setInnerFilterFields(
        innerFilterMapper.findByChartId(chartId).stream().map(InnerFilter::getFieldId).collect(
            Collectors.toList()));

    return chartDetail;
  }

  public ChartAllData createChartDetail(String chartId) {
    ChartAllData chartDetail = new ChartAllData();
    chartDetail.setChart(createChart(chartId));
    chartDetail.setChartFields(chartFieldMapper.findByChartId(chartId));
    chartDetail.setInnerFilterFields(
        innerFilterMapper.findByChartId(chartId).stream().map(InnerFilter::getFieldId).collect(
            Collectors.toList()));

    return chartDetail;
  }

  public Chart createChart(String userId, String chartId) {
    Chart chart = createChart(chartId);

    if (!StringUtils.equals(chart.getOwner(), userId)) {
      throw new PreFailedException("com.baifendian.bi.api.common.notOwner", I18nUtil.chartName(),
          chartId);
    }
    return chart;
  }

  public Chart createChart(String chartId) {
    Chart chart = chartMapper.findById(chartId);
    if (chart == null) {
      throw new PreFailedException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.chartName());
    }
    return chart;
  }

  public void checkChart(String userId, String chartId) {
    createChart(userId, chartId);
  }
}
