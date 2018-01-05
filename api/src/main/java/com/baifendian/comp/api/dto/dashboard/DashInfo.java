package com.baifendian.comp.api.dto.dashboard;

import com.baifendian.comp.dao.postgresql.model.dash.Dashboard;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashInfo {
  private String id;
  private String name;
  private String desc;
  private Date createTime;
  private Date modifyTime;
  private String owner;
  //private ChartType type;
  private String projectId;

  public DashInfo(Dashboard dashboard){
    this.id = dashboard.getId();
    this.name = dashboard.getName();
    this.desc = dashboard.getDesc();
    this.createTime = dashboard.getCreateTime();
    this.modifyTime = dashboard.getModifyTime();
    this.owner = dashboard.getOwner();
    this.projectId = dashboard.getProjectId();
  }
}
