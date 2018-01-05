package com.baifendian.comp.common.utils;

import com.baifendian.comp.common.exception.ParamException;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.common.utils.json.JsonUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 */
public class HttpUtil {
  private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

  static public <T> List<T> listRequiredParam(HttpServletRequest request, String param, Class<T> clazz) {
    String rst = request.getParameter(param);
    if (!StringUtils.isNotEmpty(rst)) {
      throw ParamException.buildEmptyException(param, clazz);
    }

    try {
      return JsonUtil.parseObjList(rst, clazz);
    } catch (IOException e) {
      logger.info("param:{}, value:{} is invalid", param, rst);
      throw ParamException.buildInvalidException(param, rst, clazz);
    }
  }

  static public <T> List<T> listParam(HttpServletRequest request, String param, Class<T> clazz) {
    String rst = request.getParameter(param);
    if (!StringUtils.isNotEmpty(rst)) {
      return new ArrayList<>();
    }

    try {
      return JsonUtil.parseObjList(rst, clazz);
    } catch (IOException e) {
      logger.info("param:{}, value:{} is invalid", param, rst);
      throw ParamException.buildInvalidException(param, rst, clazz);
    }
  }

  static public <T> T objectRequiredParam(HttpServletRequest request, String param, Class<T> clazz) {
    String rst = request.getParameter(param);
    if (!StringUtils.isNotEmpty(rst)) {
      throw ParamException.buildEmptyException(param, clazz);
    }
    try {
      return JsonUtil.parseObject(rst, clazz);
    } catch (Exception e) {
      logger.info("param:{}, value:{} is invalid", param, rst);
      throw ParamException.buildInvalidException(param, rst, clazz);
    }
  }

  static public void checkParam(JDBCParam jdbcParam) {
    if (jdbcParam == null || jdbcParam.getType() == null
        || StringUtils.isEmpty(jdbcParam.getAddress())
        || StringUtils.isEmpty(jdbcParam.getDatabase())
        || StringUtils.isEmpty(jdbcParam.getPassword())
        || StringUtils.isEmpty(jdbcParam.getUser())) {
      throw new ParamException("com.baifendian.bi.api.datasource.jdbcParam.empty");
    }
  }

  static public <T> T objectParam(HttpServletRequest request, String param, Class<T> clazz) {
    String rst = request.getParameter(param);
    if (!StringUtils.isNotEmpty(rst)) {
      return null;
    }

    try {
      return JsonUtil.parseObject(rst, clazz);
    } catch (Exception e) {
      logger.info("param:{}, value:{} is invalid", param, rst);
      throw ParamException.buildInvalidException(param, rst, clazz);
    }
  }

  public static String getPathParam(HttpServletRequest request){
    StringBuilder builder = new StringBuilder();

    builder.append(request.getRequestURI()).append("?");
    Enumeration<String> parameterNames = request.getParameterNames();
    while (parameterNames.hasMoreElements()){
      String name = parameterNames.nextElement();
      builder.append(name).append("=").append(request.getParameter(name)).append("&");
    }
    return builder.toString();
  }

}
