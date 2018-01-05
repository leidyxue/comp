package com.baifendian.comp.api.dto.error;

import com.baifendian.comp.common.exception.BiException;

public class PreFailedException extends BiException{

  public PreFailedException(String msgKey) {
    super(msgKey);
  }

  public PreFailedException(String msgKey, Object... param) {
    super(msgKey, msgKey, param);
  }

  public PreFailedException(Throwable cause, String msgKey, Object... param) {
    super(cause, msgKey, param);
  }
}
