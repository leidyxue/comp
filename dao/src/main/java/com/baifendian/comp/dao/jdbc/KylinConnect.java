package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.dao.kylin.KylinClient;
import com.baifendian.comp.dao.structs.sql.DbTableMeta;
import java.util.List;

public class KylinConnect extends DsConnect{

  private KylinClient kylinClient = null;

  public KylinConnect(JDBCParam jdbcParam)
      throws Exception {
    super(jdbcParam);

    kylinClient = new KylinClient(jdbcParam.getAddress(), jdbcParam.getUser(), jdbcParam.getPassword());
  }

  @Override
  public List<DbTableMeta> getTableMetas() {
    return null;
  }
}
