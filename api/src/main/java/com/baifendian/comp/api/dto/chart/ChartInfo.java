package com.baifendian.comp.api.dto.chart;

import com.baifendian.comp.dao.postgresql.model.chart.Chart;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChartInfo {

  private String id;
  private String name;
  private String desc;
  private String dashId;
  private String dashName;
  private String tableId;
  private String tableName;

  public ChartInfo(Chart chart){
    this.id = chart.getId();
    this.name = chart.getName();
    this.desc = chart.getDesc();
    this.dashId = chart.getDashId();
    this.dashName = chart.getDashName();
    this.tableId = chart.getTableId();
    this.tableName = chart.getTableName();
  }
}
