package com.baifendian.comp.common.structs.table.combine;

import com.baifendian.bi.engine.enums.JoinType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TableJoin {

  private String leftTableId;
  private String rightTableId;
  private JoinType JoinType;
  private List<JoinField> joinFields;
}
