package com.baifendian.comp.common.structs.dash;

import com.baifendian.comp.common.annotation.AuditProperty;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Project {

  private String id;
  @AuditProperty
  private String name;
  private String desc;
  private String owner;
  @AuditProperty
  private String parentId;
  private Date createTime;
  private Date modifyTime;
}
