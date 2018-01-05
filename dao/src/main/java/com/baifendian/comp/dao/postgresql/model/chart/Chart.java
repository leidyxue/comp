package com.baifendian.comp.dao.postgresql.model.chart;

import com.baifendian.comp.common.annotation.AuditProperty;
import com.baifendian.comp.common.enums.ChartType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chart {
  private String id;
  @AuditProperty
  private String name;
  private String tableId;
  @AuditProperty
  private String tableName;
  @AuditProperty
  private String desc;
  private String dashId;
  @AuditProperty
  private String dashName;
  private String projectId;
  @AuditProperty
  private String projectName;
  private ChartType chartType;
  private String owner;
  @Default
  private Date createTime= new Date();
  @Default
  private Date modifyTime = new Date();
  @Default
  private ChartMetaData webData = new ChartMetaData();
  private String filterExp;
}
