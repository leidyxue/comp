package com.baifendian.comp.common.exception;

import org.springframework.http.HttpStatus;

public class ParamException extends BiException {

  public ParamException(String msgKey, Object... obj) {
    super(msgKey, HttpStatus.PRECONDITION_FAILED, obj);
  }

  public static <T> ParamException buildInvalidException(String paramName, String value,
      Class<T> tClass) {
    return new ParamException("com.baifendian.bi.api.dto.param.exception.paramInvalid", paramName, tClass.getSimpleName(),
        value);
  }

  public static <T> ParamException buildEmptyException(String paramName, Class<T> tClass) {
    return new ParamException("com.baifendian.bi.api.dto.error.ParameterException.empty", paramName, tClass.getSimpleName());
  }
}
