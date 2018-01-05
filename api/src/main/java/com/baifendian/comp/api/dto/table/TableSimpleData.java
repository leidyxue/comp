package com.baifendian.comp.api.dto.table;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.enums.TableType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class TableSimpleData {

  private String id;
  private String dsId;
  private String name;
  private String originName;
  private String dsName;
  private DSType dsType;
  private TableType tableType;
  private Date createTime;
  private Date modifyTime;
}
