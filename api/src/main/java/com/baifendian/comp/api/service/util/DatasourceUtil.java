package com.baifendian.comp.api.service.util;

import com.baifendian.comp.common.exception.ParamException;
import com.baifendian.comp.common.structs.datasource.Datasource;
import com.baifendian.comp.dao.postgresql.mapper.DatasourceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasourceUtil {

  @Autowired
  private DatasourceMapper datasourceMapper;

  public String getInnerDsId() {
    return "ds_inner_ds";
  }

  public Datasource createDatasource(String dataSourceId, String owner) {
    Datasource datasource = datasourceMapper.findById(dataSourceId);

    if (datasource == null) {
      throw new ParamException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.dsName());
    }

    if (datasource.getOwner() != null) {
      if (!StringUtils.equals(dataSourceId, getInnerDsId())
          && !StringUtils.equals(datasource.getOwner(), owner)) {
        throw new ParamException("com.baifendian.bi.api.common.notOwner", I18nUtil.dsName(),
            dataSourceId);
      }
    }
    return datasource;
  }

  public void checkDatasource(String dataSourceId, String owner) {
    Datasource datasource = datasourceMapper.findById(dataSourceId);

    if (datasource == null) {
      throw new ParamException("com.baifendian.bi.api.common.invalid.Id", I18nUtil.dsName());
    }

    if (!StringUtils.equals(datasource.getOwner(), owner)) {
      throw new ParamException("com.baifendian.bi.api.common.notOwner", I18nUtil.dsName(),
          dataSourceId);
    }
  }
}
