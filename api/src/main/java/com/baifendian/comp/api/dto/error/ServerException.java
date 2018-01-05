package com.baifendian.comp.api.dto.error;

import com.baifendian.comp.common.exception.BiException;

public class ServerException extends BiException{

  public ServerException(String msgKey) {
    super(msgKey);
  }

  public ServerException(String msgKey, Object[] args) {
    super(msgKey, msgKey, args);
  }

  public ServerException(String msg, String msgKey, Object[] obj) {
    super(msg, msgKey, obj);
  }

  public ServerException(Throwable cause, String msgKey, Object... obj) {
    super(cause, msgKey, obj);
  }

  public static ServerException serverException() {
    return new ServerException("com.baifendian.bi.api.dto.ServerException");
  }

  public static ServerException serverException(Throwable cause) {
    return new ServerException(cause, "com.baifendian.bi.api.dto.ServerException");
  }
}
