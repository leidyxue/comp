package com.baifendian.comp.api.dto.table;

import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.bi.engine.enums.FieldType;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_EMPTY)
public class SchemaData {

  private String id;
  private String name;
  private String originName;
  private String fileOriginName;
  private FieldType type;
  private String originType;
  private FieldGenType genType;
  private String aggregator;
  private String desc;
  private String formula;
  private Date createTime;
  private Date modifyTime;
}
