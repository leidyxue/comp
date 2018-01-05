package com.baifendian.comp.api.dto;

import com.baifendian.comp.api.dto.error.PreFailedException;
import com.baifendian.comp.common.annotation.ParamProperty;
import com.baifendian.comp.common.exception.ParamException;
import com.baifendian.comp.common.utils.HttpUtil;
import com.google.common.base.Joiner;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;

public class ParamUtil {
  private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

  static public void paramCheck(Object body){
    if (body == null){
      return;
    }

    Class classObj = body.getClass();
    Field fields[] = classObj.getDeclaredFields();
    if (fields == null){
      return;
    }

    for (Field field: fields){
      ParamProperty paramProperty = field.getAnnotation(ParamProperty.class);
      if (paramProperty != null){
        Object value = getFieldValue(body, field);
        if (paramProperty.required() ){
          if (null == value){
            throw new ParamException("com.baifendian.bi.api.dto.error.ParameterException.empty", field.getName(), field.getType().getSimpleName());
          }
        }

        if (paramProperty.recursive()){
          paramCheck(value);
          continue;
        }

        if (field.getType().isEnum()){
          String[] values = paramProperty.range();
          if (values.length == 0){
            continue;
          }
          Class<Enum> clz = (Class<Enum>) field.getType();
          if (Arrays.stream(values)
              .noneMatch(v -> value == Enum.valueOf(clz, v))){
            throw new ParamException(
                "com.baifendian.bi.api.dto.error.ParameterException.valueError"
                , field.getName(), array2String(values));
          }
        }else if (field.getType().isAssignableFrom(String.class) && value != null){
          // check length
          String strVal = (String) value;
          int maxLen = paramProperty.maxLen();
          if (strVal.length() > maxLen){
            String[] names = paramProperty.name();
            DefaultMessageSourceResolvable msg = null;
            if (names.length >= 1){
              for (int i=0;i<names.length; ++i){
                if (msg == null){
                  msg = new DefaultMessageSourceResolvable(names[i]);
                }else {
                  msg = new DefaultMessageSourceResolvable(new String[]{names[i]}, new Object[]{msg});
                }
              }

              throw new PreFailedException("com.bfd.bi.api.common.length.tooLong", msg, maxLen);
            }

          }
        }

//        if (StringUtils.isNotEmpty(paramProperty.check())) {
//
//          Method method;
//          try {
//            method = paramProperty.checkClass()
//                .getMethod(paramProperty.check(), field.getClass());
//          } catch (NoSuchMethodException e) {
//            continue;
//          }
//
//          if (method != null) {
//            try {
//              method.invoke(null, value);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//              logger.debug("method error. param :" + classObj.getName(), e);
//            }
//          }
//        }
      }
    }
  }

  private static String array2String(String[] arr){
    return "[" + joiner.join(arr) + "]";
  }
  private static Joiner joiner = Joiner.on(",");

  static public Object getFieldValue(Object body, Field field){
    try {
      PropertyDescriptor pd = new PropertyDescriptor(field.getName(), body.getClass());
      Method getMethod = pd.getReadMethod();//获得get方法
      getMethod.setAccessible(true);
      return getMethod.invoke(body);//执行get方法返回一个Object
    } catch (Exception e) {
      logger.info("Param check error.", e);
    }
    return null;
  }

  static public void checkName(String name){
    if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(name.trim())){
      return;
    }

    throw new PreFailedException("com.bfd.bi.api.common.name.isSpace");
  }
}
