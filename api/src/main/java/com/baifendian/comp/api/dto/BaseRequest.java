package com.baifendian.comp.api.dto;

import com.baifendian.comp.common.consts.Constants;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseRequest {

  private HttpServletRequest request;

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());


  public BaseRequest(HttpServletRequest request) {
    this.request = request;
  }

  public String getSessionId(){
    Cookie cookie = getCookieByName(Constants.SESSION_ID_NAME);
    String sessionId = strParam("sessionId");

    if (sessionId == null && cookie != null) {
      sessionId = cookie.getValue();
    }
    if (StringUtils.isEmpty(sessionId)){
      sessionId = "a123";
    }

    return sessionId;
  }

  /**
   * 从请求中获取 cookie 信息
   *
   * @param name
   * @return Cookie
   */
  public Cookie getCookieByName( String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (name.equals(cookie.getName())) {
          return cookie;
        }
      }
    }
    return null;
  }

  public String getClientIpAddress() {
    String ip = request.getHeader("X-Forwarded-For");
    if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      //多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    ip = request.getHeader("X-Real-IP");
    if (org.apache.commons.lang3.StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      return ip;
    }
    return request.getRemoteAddr();
  }

  public String strParam(String param) {
    return request.getParameter(param);
  }

  public boolean boolParam(String name) {
    String rst = request.getParameter(name);
    return Boolean.parseBoolean(rst);
  }

}
