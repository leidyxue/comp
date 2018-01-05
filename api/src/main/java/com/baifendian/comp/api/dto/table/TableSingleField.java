package com.baifendian.comp.api.dto.table;

import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.bi.engine.enums.FieldType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableSingleField {

  private String tableId;
  private String id;
  private String name;
  private String originName;
  private FieldType type;
  private FieldGenType genType;
  private String aggregator;
  //private String param;
  private String formula;
  private Date createTime;
  private Date modifyTime;
  private String desc;
}
