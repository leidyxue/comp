package com.baifendian.comp.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamProperty {

//  Class<?> checkClass() default ParamCheck.class;
//  String check() default "";

  int maxLen() default 65535;
  String[] name() default {};

  String[] range() default {};

  boolean required() default true;

  boolean recursive() default false;
}
