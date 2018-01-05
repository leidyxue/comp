package com.baifendian.comp.common.utils;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 返回数据，国际化
 *
 * <p>
 *
 * @author : shuanghu
 */
@Service
public class BaseResponseUtils {

  private static Logger LOGGER = LoggerFactory.getLogger(BaseResponseUtils.class);

  @Autowired
  public void setMessageSource(MessageSource messageSource) {
    BaseResponseUtils.messageSource = messageSource;
  }

  static private MessageSource messageSource;

  /**
   * 国际化
   */
  static public String baseResponseTranslation(String code, Object... args) {
    return baseResponseTranslation(LocaleContextHolder.getLocale(), code, args);
  }

  /**
   * 国际化
   */
  static public String baseResponseTranslation(Locale local, String code, Object... args) {
    String msg = null;

    if (!StringUtils.isEmpty(code)) {
      try {
        msg = messageSource.getMessage(code, args, local);
      } catch (Exception e) {
        LOGGER.error("Translation baseResponse error!", e);
        return code;
      }

      LOGGER.debug("code: {}, msg: {}", code, msg);
    }

    return msg;
  }
}
