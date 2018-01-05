package com.baifendian.comp.dao.postgresql.model.table;

import com.baifendian.comp.common.annotation.AuditProperty;
import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.enums.TableType;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.common.structs.table.TableParam;
import java.util.Date;
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
public class Table {

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
  private boolean deleted;

  public boolean isFile(){
    return TableType.CSV == type || TableType.EXCEL == type;
  }

  public boolean nonDeleted(){
    return !deleted;
  }
  public boolean isShare(){
    return TableType.SHARE == type;
  }

}
