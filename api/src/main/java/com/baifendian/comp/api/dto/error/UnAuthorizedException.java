package com.baifendian.comp.api.dto.error;

import com.baifendian.comp.common.exception.BiException;

public class UnAuthorizedException extends BiException{
  public UnAuthorizedException(String msgKey, Object... args) {
    super("", msgKey, args);
  }
}
