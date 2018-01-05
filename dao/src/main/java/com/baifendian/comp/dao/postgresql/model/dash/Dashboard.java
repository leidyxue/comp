package com.baifendian.comp.dao.postgresql.model.dash;

import com.baifendian.comp.common.annotation.AuditProperty;
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
public class Dashboard {
  private String id;
  @AuditProperty
  private String name;
  @AuditProperty
  private String desc;
  private String owner;
  private String projectId;
  @AuditProperty
  private String projectName;
  @Default
  private Date createTime = new Date();
  @Default
  private Date modifyTime = new Date();
  @Default
  private DashMetaData webData = new DashMetaData();
}
