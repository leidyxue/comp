package com.baifendian.bi.engine.factory;

import com.baifendian.bi.engine.custom.CustomHandle;
import com.baifendian.bi.engine.custom.PostgreSqlHandle;
import com.baifendian.bi.engine.element.table.SimpleTable;
import com.baifendian.bi.engine.meta.SQLTable;

public class PostgreSqlFactory implements SqlBuilder{

  private static CustomHandle handle = new PostgreSqlHandle();
  private int alisaCount = 0;

  @Override
  public SQLTable table(String name) {
    return new SimpleTable(handle, name, "t"+alisaCount++);
  }

  @Override
  public CustomHandle getCustomHandle() {
    return handle;
  }
}
