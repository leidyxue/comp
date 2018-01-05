package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.exception.BiException;
import org.springframework.http.HttpStatus;

public class BiSQLException extends BiException{

  public BiSQLException(Throwable cause, String msgKey, Object... obj) {
    super(cause, msgKey, HttpStatus.INTERNAL_SERVER_ERROR, obj);
  }
}
