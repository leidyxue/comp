package com.baifendian.comp.common.structs.table;

import com.baifendian.comp.common.annotation.AuditProperty;
import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.enums.TableType;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.common.structs.field.FieldOld;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_EMPTY)
public class TableOld {
  private String id;

  @AuditProperty
  private String name;

  @AuditProperty
  private String originName;

  @AuditProperty
  private String desc;

  private TableType type;

  private TableParam parameter;
  private JDBCParam jdbcParameter;

  @Default
  private Date createTime = new Date();

  @Default
  private Date modifyTime = new Date();

  private String owner;
  private String parentId;

  private String dsId;
  private String dsName;
  private DSType dsType;

  private List<FieldOld> schema;

  public boolean isShare(){
    return TableType.SHARE == type;
  }

  public boolean fieldIn(String id){
    return schema.stream().anyMatch(field -> field.getId().equals(id));
  }
}
