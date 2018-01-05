package com.baifendian.comp.api.dto.error;

import com.baifendian.comp.common.exception.BiException;

public class ParameterException extends BiException{

  public ParameterException(String msgKey) {
    super(msgKey);
  }

  public ParameterException(String msgKey, Object... args) {
    super(msgKey, msgKey, args);
  }

  public ParameterException(Throwable cause, String msgKey, Object... obj) {
    super(cause, msgKey, obj);
  }

  public static <T> ParameterException emptyException(String paramName, Class<T> tClass) {

    return new ParameterException("com.baifendian.bi.api.dto.error.ParameterException.empty",
        paramName, tClass.getSimpleName());
  }
}
