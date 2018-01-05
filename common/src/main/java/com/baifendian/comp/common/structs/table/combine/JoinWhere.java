package com.baifendian.comp.common.structs.table.combine;

import com.baifendian.bi.engine.enums.CombineLogic;
import java.util.List;

public class JoinWhere {

  private CombineLogic linkType;
  private List<WhereFilter> filters;
}
