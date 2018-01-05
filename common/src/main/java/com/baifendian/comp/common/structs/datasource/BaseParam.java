package com.baifendian.comp.common.structs.datasource;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

//@JsonSubTypes({@Type(value = JDBCParam.class, name = "jdbc")})
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
public abstract class BaseParam {

  @Override
  public abstract boolean equals(Object obj);

  @Override
  public abstract int hashCode();
}
