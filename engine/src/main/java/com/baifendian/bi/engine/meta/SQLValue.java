package com.baifendian.bi.engine.meta;

import com.baifendian.bi.engine.custom.Customable;
import com.baifendian.bi.engine.enums.FieldType;

public interface SQLValue extends Customable{

  FieldType getType();

  String getValue();
}
