package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.exception.BiException;
import org.springframework.http.HttpStatus;

public class JdbcException extends BiException {

  public JdbcException(Throwable cause) {
    super(cause, "com.baifendian.bi.api.datasource.operator.error",  HttpStatus.GATEWAY_TIMEOUT);
  }

  public JdbcException(String msgKey, Throwable cause) {
    super(cause, msgKey,  HttpStatus.GATEWAY_TIMEOUT);
  }

  public JdbcException(String msgKey, Object... obj) {
    super(msgKey,  HttpStatus.GATEWAY_TIMEOUT, obj);
  }
}
