package com.baifendian.comp.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 自定义异常类型，支持国际化，可以携带国际化的msgKey
 *
 * <p>
 */
public class BiException extends RuntimeException{
  /**
   * Serial version UID
   */
  private static final long serialVersionUID = 1L;
  // 国际化的参数
  private Object[] args;
  // 国际化的msgKey
  private String msgKey;
  // Http返回状态码
  private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  public BiException(Throwable cause, String msgKey, HttpStatus httpStatus, Object... obj) {
    super(cause);
    this.msgKey = msgKey;
    this.httpStatus = httpStatus;
    args = obj;
  }
  public BiException(String msgKey, HttpStatus httpStatus, Object... obj) {
    super(msgKey);
    this.msgKey = msgKey;
    this.httpStatus = httpStatus;
    args = obj;
  }


  public BiException(String msgKey){
    super(msgKey);
    this.msgKey = msgKey;
  }

  public BiException(Throwable cause, String msgKey, Object... obj) {
    super(msgKey, cause);
    this.msgKey = msgKey;
    args = obj;
  }

  public BiException(String msg, String msgKey, Object[] obj) {
    super(msg);
    this.msgKey = msgKey;
    args = obj;
  }

  public Object[] getArgs() {
    return args;
  }

  public void setArgs(Object[] args) {
    this.args = args;
  }

  public String getMsgKey() {
    return msgKey;
  }

  public void setMsgKey(String msgKey) {
    this.msgKey = msgKey;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }
}
