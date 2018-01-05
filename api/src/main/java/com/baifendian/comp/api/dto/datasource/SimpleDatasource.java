package com.baifendian.comp.api.dto.datasource;

import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.enums.ds.DsMetaType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDatasource {

  private String id;
  private String name;
  private String desc;
  private DSType type;
  private DsMetaType metaType;
  private Date createTime;
  private Date modifyTime;
}
