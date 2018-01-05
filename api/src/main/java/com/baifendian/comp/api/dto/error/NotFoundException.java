package com.baifendian.comp.api.dto.error;

import com.baifendian.comp.api.service.util.I18nUtil;
import com.baifendian.comp.common.exception.BiException;

public class NotFoundException extends BiException{

  public NotFoundException(String msgKey, Object... args) {
    super(msgKey, msgKey, args);
  }

  public static NotFoundException createDs(){
    return new NotFoundException("com.baifendian.bi.api.common.duplicate", I18nUtil.dsName());
  }
}
