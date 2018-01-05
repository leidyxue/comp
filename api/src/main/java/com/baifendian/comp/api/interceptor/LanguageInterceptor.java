package com.baifendian.comp.api.interceptor;

import com.baifendian.comp.common.consts.Constants;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LanguageInterceptor implements HandlerInterceptor {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Override
  public boolean preHandle(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o) throws Exception {
    Subject currentUser = null;
    try {
      currentUser = SecurityUtils.getSubject();
    }catch (Exception e){
      e.printStackTrace();
    }


    Session session;
    try {
      session = currentUser.getSession();
    }catch (Exception e){
      LOGGER.error("Get session error.", e);
      return true;
    }

    LOGGER.debug("BI locale : {}",
        session.getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE"));

    //BaseRequest baseRequest = new BaseRequest(httpServletRequest);

    //Cookie language = baseRequest.getCookieByName(Constants.SESSION_LANGUAGE);
    Object localeObe = session.getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE");
    LOGGER.info("BI locale : {}",
        session.getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE"));
//    Locale locale = Constants.zh_CN;
    Locale locale = Constants.pt_PT;
    if (localeObe != null) {
      locale = (Locale)localeObe;
    }
    LocaleContextHolder.setLocale(locale);
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
