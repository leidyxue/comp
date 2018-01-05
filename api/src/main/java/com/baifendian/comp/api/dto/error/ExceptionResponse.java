package com.baifendian.comp.api.dto.error;

import com.baifendian.comp.common.utils.BaseResponseUtils;
import com.baifendian.comp.common.exception.BiException;

/**
 * <p>
 *
 * @author : shuanghu
 */
public class ExceptionResponse {

  private String msg;

  private String detailInfo;

  public String getMsg() {
    return msg;
  }

  public ExceptionResponse() {
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getDetailInfo() {
    return detailInfo;
  }

  public void setDetailInfo(String detailInfo) {
    this.detailInfo = detailInfo;
  }

  public ExceptionResponse(String msg, String detailInfo) {
    this.msg = msg;
    this.detailInfo = detailInfo;
  }

  /**
   * 构造一个异常返回
   */
  public static ExceptionResponse build(BiException e) {
    ExceptionResponse exceptionResponse = new ExceptionResponse();
    String msg = BaseResponseUtils.baseResponseTranslation(e.getMsgKey(), e.getArgs());
    return new ExceptionResponse(msg, e.getMessage());
  }
}
