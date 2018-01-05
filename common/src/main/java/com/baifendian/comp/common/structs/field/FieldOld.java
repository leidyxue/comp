package com.baifendian.comp.common.structs.field;

import com.baifendian.comp.common.annotation.AuditProperty;
import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.bi.engine.enums.FieldType;
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
public class FieldOld {

  private String id;

  @AuditProperty
  private String name;

  @AuditProperty
  private String originName;
  private String fileOriginName;
  private String desc;

  @AuditProperty
  private FieldType type;
  private String originType;

  private FieldGenType genType;

  @AuditProperty
  private String aggregator;

  private String parameter;

  private String owner;

  private String tableId;

  @AuditProperty
  private String tableName;
  @AuditProperty
  private String tableOrgName;

  @Default
  private Date createTime = new Date();

  @Default
  private Date modifyTime = new Date();

  @Default
  private Integer sortId = null;

  @Default
  private boolean deleted = false;

  public boolean isNative(){
    return FieldGenType.NATIVE == genType;
  }
  public boolean nonNative(){
    return FieldGenType.NATIVE != genType;
  }
}
