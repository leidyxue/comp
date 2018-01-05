package com.baifendian.comp.common.utils;

public class NameUtil {
  static public String dataSourceName(){
    return "ds_" + GenIdUtil.genGeneralId();
  }

  static public String tableName(){
    return "tb_" + GenIdUtil.genGeneralId();
  }

  static public String fieldName(){
    return "fd_" + GenIdUtil.genGeneralId();
  }

  static public String folderName(){
    return "dir_"+ GenIdUtil.genGeneralId();
  }
}
