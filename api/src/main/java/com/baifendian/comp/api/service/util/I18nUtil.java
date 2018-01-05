package com.baifendian.comp.api.service.util;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;

public class I18nUtil {
  public static MessageSourceResolvable dsName(){
    return new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.ds");
  }

  public static MessageSourceResolvable tableName(){
    return new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.table");
  }

  public static MessageSourceResolvable fieldName(){
    return new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.field");
  }
  public static MessageSourceResolvable dashName(){
    return new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.dash");
  }

  public static MessageSourceResolvable shareName(){
    return new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.share");
  }

  public static MessageSourceResolvable dirName(){
    return new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.dir");
  }

  public static MessageSourceResolvable projectName(){
    return new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.project");
  }
  public static MessageSourceResolvable chartName(){
    return new DefaultMessageSourceResolvable("com.baifendian.bi.common.name.chart");
  }
}
