package com.baifendian.comp.dao.postgresql.model;

import com.baifendian.comp.common.utils.GenIdUtil;
import com.baifendian.comp.dao.postgresql.model.chart.Chart;
import com.baifendian.comp.dao.postgresql.model.chart.ChartField;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChartAllData {

  public String dashId(){
    return chart.getDashId();
  }
  public String tableId(){
    return chart.getTableId();
  }
  public String chartId(){
    return chart.getId();
  }

  private Chart chart;
  private List<ChartField> chartFields = new ArrayList<>();
  private List<String> innerFilterFields = new ArrayList<>();

  public ChartAllData copy(String newDashId, String newName) {
    ChartAllData newObj = new ChartAllData();

    String newId = GenIdUtil.genChartId();

    newObj.setChart(Chart.builder()
        .id(newId)
        .dashId(newDashId)
        .chartType(chart.getChartType())
        .owner(chart.getOwner())
        .desc(chart.getDesc())
        .name(newName)
        .filterExp(chart.getFilterExp())
        .webData(chart.getWebData())
        .tableId(chart.getTableId())
        .build());

    newObj.setChartFields(chartFields.stream()
        .map(cf -> ChartField.builder()
            .uniqId(cf.getUniqId())
            .alias(cf.getAlias())
            .chartId(newId)
            .fieldId(cf.getFieldId())
            .level(cf.getLevel())
            .operator(cf.getOperator())
            .sort(cf.getSort())
            .order(cf.getOrder())
            .type(cf.getType())
            .build())
        .collect(Collectors.toList()));

    List<String> newInner = new ArrayList<>();
    newInner.addAll(innerFilterFields);

    newObj.setInnerFilterFields(newInner);

    return newObj;
  }
}
