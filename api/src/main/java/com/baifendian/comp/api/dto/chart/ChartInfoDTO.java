package com.baifendian.comp.api.dto.chart;

import com.baifendian.comp.common.consts.Constants;
import com.baifendian.comp.common.enums.ChartType;
import com.baifendian.comp.common.structs.chart.ChartMeta;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChartInfoDTO {

  private String id;
  private String name;
  private String tableId;
  private String tableName;
  private String desc;
  private String dashId;
  private String dashName;
  private ChartType chartType;
  private String owner;
  private Date createTime;
  private Date modifyTime;
  private ChartMeta meta;

  @Default
  private int dataStatus = 0;
  private String dataMessage;

  private List<String> titles;
  @JsonFormat(pattern = Constants.BASE_DATETIME_FORMAT)
  private List<List<Object>> data;
}
