package com.baifendian.comp.api.controller;

import com.baifendian.comp.common.utils.BaseResponseUtils;
import com.baifendian.comp.api.dto.error.ExceptionResponse;
import com.baifendian.comp.api.dto.error.NotFoundException;
import com.baifendian.comp.api.dto.error.ParameterException;
import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.api.dto.error.ServerException;
import com.baifendian.comp.api.dto.error.UnAuthorizedException;
import com.baifendian.comp.common.exception.BiException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常处理类
 *
 * <p>
 *
 * @author : shuanghu
 */
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  /**
   * 默认异常处理
   * @param req
   * @param e
   * @return
   */
  @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  ResponseEntity handleAllException(HttpServletRequest req, Exception e) {
    LOGGER.error("INTERNAL_SERVER_ERROR", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ExceptionResponse.build(ServerException.serverException(e)));
  }

  /**
   * 404类型异常
   * @param req
   * @param e
   * @return
   */
  @ResponseStatus(value= HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  ResponseEntity handleNotFoundException(HttpServletRequest req, NotFoundException e) {
    LOGGER.error("NOT_FOUND", e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ExceptionResponse.build(e));
  }

  /**
   * 401类型异常
   * @param req
   * @param e
   * @return
   */
  @ResponseStatus(value= HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UnAuthorizedException.class)
  ResponseEntity handleUnauthorizedException(HttpServletRequest req, UnAuthorizedException e) {
    LOGGER.error("UNAUTHORIZED", e);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(ExceptionResponse.build(e));
  }

  /**
   * 412类型异常
   * @param req
   * @param e
   * @return
   */
  @ResponseStatus(value= HttpStatus.PRECONDITION_FAILED)
  @ExceptionHandler(PreFailedException.class)
  ResponseEntity handlePreFailedException(HttpServletRequest req, PreFailedException e) {
    LOGGER.error("PRECONDITION_FAILED", e);
    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
        .body(ExceptionResponse.build(e));
  }

  /**
   * 400类型异常
   * @param req
   * @param e
   * @return
   */
  @ResponseStatus(value= HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ParameterException.class)
  ResponseEntity handleParameterException(HttpServletRequest req, ParameterException e) {
    LOGGER.error("BAD_REQUEST", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ExceptionResponse.build(e));
  }

  /**
   */
  @ExceptionHandler(BiException.class)
  ResponseEntity handleBaseException(HttpServletRequest req, BiException e) {
    LOGGER.error("Base exception", e);
    return ResponseEntity.status(e.getHttpStatus())
        .body(ExceptionResponse.build(e));
  }

  @ExceptionHandler(DuplicateKeyException.class)
  ResponseEntity handleDuplicateException(HttpServletRequest req, DuplicateKeyException e) {
    LOGGER.error("Base exception", e);
    String msg = BaseResponseUtils.baseResponseTranslation("com.baifendian.bi.api.common.duplicate", "");
    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
        .body(new ExceptionResponse(msg, e.getMessage()));
  }
}
