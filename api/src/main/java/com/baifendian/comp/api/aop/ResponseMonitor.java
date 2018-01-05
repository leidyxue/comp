package com.baifendian.comp.api.aop;

import com.baifendian.comp.api.dto.ParamUtil;
import com.baifendian.comp.common.annotation.ParamRequest;
import com.baifendian.comp.common.utils.HttpUtil;
import com.baifendian.comp.common.utils.json.JsonUtil;
import com.bfd.systemmanager.client.holder.ShiroUserHolder;
import com.bfd.systemmanager.remote.ShiroUser;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Rest请求监控，打印请求URL及请求响应时间
 *
 * <p>
 */
@Aspect
@Component
public class ResponseMonitor {

  private static Logger logger = LoggerFactory.getLogger(ResponseMonitor.class);

  /**
   * 性能监控
   */
  @Around(value = "within(com.baifendian.bi.api.controller..*) && !within(com.baifendian.comp.api.controller.ExceptionHandlerController)")
  public Object responseTranslation(ProceedingJoinPoint point) throws Throwable {
    long startTime = System.currentTimeMillis();
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();

    HttpServletRequest request = attributes.getRequest();

    String method = request.getMethod();

    String bodyStr = null;
    String url = null;
    if (!"GET".equals(method)) {
      url = request.getRequestURI();

      Object body = null;
      Object[] params = point.getArgs();
      for (Object obj : params) {
        Class classObj = obj.getClass();
        ParamRequest paramProperty = (ParamRequest) classObj.getAnnotation(ParamRequest.class);

        if (paramProperty != null) {
          body = obj;
          bodyStr = JsonUtil.toJsonString(obj);
          break;
        }
      }
      ParamUtil.paramCheck(body);
    }else{
      url = HttpUtil.getPathParam(request);
    }

    ShiroUser shiroUser = ShiroUserHolder.getUser();
    String userId = "";
    if (shiroUser != null){
      userId = shiroUser.getId();
    }
    logger.info("Start time:{}, userId:{}, aop time:{}, Request method:{}, url:{}, body:{}",  startTime, userId, System.currentTimeMillis()-startTime,method, url, bodyStr);

    Object response = point.proceed();

    // 1. (rule, action) 的平均时间, 90% 的时间, 最大, 最少的时间
    // 2. 路径可以传递, 传递一个时间(比如 2017-01-05)
    logger.info("Performance monitoring, start time:{}, status code:{}, cost time:{} ms ", startTime, attributes.getResponse().getStatus(),
        System.currentTimeMillis() - startTime);

    return response;
  }
}
