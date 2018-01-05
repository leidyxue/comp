package com.baifendian.comp.dao.postgresql.model;

import com.baifendian.comp.common.utils.GenIdUtil;
import com.baifendian.comp.dao.postgresql.model.dash.DashElement;
import com.baifendian.comp.dao.postgresql.model.dash.DashFilter;
import com.baifendian.comp.dao.postgresql.model.dash.DashLink;
import com.baifendian.comp.dao.postgresql.model.dash.Dashboard;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashAllData {

  private Dashboard dash;
  @Default
  private List<DashLink> links = new ArrayList<>();
  @Default
  private List<DashFilter> filters = new ArrayList<>();
  @Default
  private List<DashElement> elements = new ArrayList<>();

  public Optional<DashFilter> findDashFilter(String id, String chartId){
    return filters.stream().filter(df -> StringUtils.equals(df.getId(), id)
    && StringUtils.equals(df.getChartId(), chartId))
        .findFirst();
  }

  public Dashboard copyDash(String name, String desc, String projectId){
    return Dashboard.builder()
        .id(GenIdUtil.genDashId())
        .desc(desc)
        .webData(dash.getWebData())
        .name(name)
        .owner(dash.getOwner())
        .projectId(projectId)
        .build();
  }

  public DashAllData copy(String name, String desc, String projectId){
    String newDashID = GenIdUtil.genDashId();
    DashAllData dashAllData = new DashAllData();
    dashAllData.setDash(Dashboard.builder()
        .id(newDashID)
        .desc(desc)
        .webData(dash.getWebData())
        .name(name)
        .owner(dash.getOwner())
        .projectId(projectId)
        .build());

//    List<ChartAllData> newCharts = new ArrayList<>();
//    Map<String, String> chartIdMap = new HashMap<>();
//    for (ChartAllData chart: charts){
//      ChartAllData newChart = chart.copy(newDashID);
//      newCharts.add(newChart);
//      chartIdMap.put(chart.chartId(), newChart.chartId());
//    }
//
//    dashAllData.setCharts(newCharts);

    return dashAllData;
  }
}
