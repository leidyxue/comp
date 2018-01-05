package com.baifendian.bi.engine.meta;

import com.baifendian.bi.engine.custom.Customable;
import com.baifendian.bi.engine.enums.FieldType;

public interface Column extends Customable {

  ColumnType getType();

  default String cast(FieldType type) {
    if (type == getType().getOrgType()){
      return getName();
    }
    switch (type){
      case DATE:
        return getCustomHandle().castDate(getName());

      case NUM:
        return getCustomHandle().castNum(getName());

      case TEXT:
        return getCustomHandle().castText(getName());
    }
    return getName();
  }

  String getId();

  Column modifyId(String newId);

  default String getAlisa() {
    return "c_" + getId();
  }

  String getName();

  default String toSelect() {
    return "(" + getName() + ") as " + getAlisa();
  }
}
