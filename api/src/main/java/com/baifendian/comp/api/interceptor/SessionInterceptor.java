package com.baifendian.comp.api.interceptor;

import com.bfd.systemmanager.client.holder.ShiroUserHolder;
import com.bfd.systemmanager.remote.ShiroUser;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionInterceptor implements HandlerInterceptor {

  private static List<String> EXCLUDE_PATH = Arrays.asList(
      "/login", "/islogin", "/logout", "/shiro/shiroManager", "/error"
  );

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Override
  public boolean preHandle(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o) throws Exception {

    String path = httpServletRequest.getServletPath();

    if (EXCLUDE_PATH.contains(path)) {
      return true;
    }

    //非统一登陆
//      BaseRequest baseRequest = new BaseRequest(httpServletRequest);
//      Session session = sessionService.getSessionFromRequest(baseRequest);
//      httpServletRequest.setAttribute("session", session);
//      httpServletRequest.setAttribute("user", session.getUser());
//      httpServletRequest.setAttribute("userId", session.getUser().getId());

    //统一登录
    String url = httpServletRequest.getRequestURI();
    //httpServletRequest.setAttribute("session", httpServletRequest.getSession());
    ShiroUser shiroUser = ShiroUserHolder.getUser();
    //httpServletRequest.setAttribute("user", shiroUser);
    if (shiroUser == null){
      String rst = httpServletRequest.getParameter("userId");
      if (StringUtils.isEmpty(rst)) {
        httpServletRequest.setAttribute("userId", "590090585582574419");
      }else{
        httpServletRequest.setAttribute("userId", rst);
      }
    }else {
      httpServletRequest.setAttribute("userId", ShiroUserHolder.getUser().getId());
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)
      throws Exception {
  }

  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

  }
}
