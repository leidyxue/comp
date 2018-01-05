package com.baifendian.comp.api.service.util;

import com.baifendian.comp.common.enums.FieldGenType;
import com.baifendian.comp.common.exception.ParamException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TableUtil {

  static String idNameTransform(String org, Map<String, String> refMap) {
    if (StringUtils.isEmpty(org)) {
      return null;
    }
    String result = org;
    Matcher matcher = FieldNamePattern.matcher(org);
    while (matcher.find()) {
      String name = matcher.group(1);
      if (!refMap.containsKey(name)){
        //return "null";
        //result = result.replace("${" + name + "}", " null ");
        throw new ParamException("com.baifendian.bi.api.datasource.table.field.name.error", name);
      }else {
        result = result.replace("${" + name + "}", "${" + refMap.get(name) + "}");
      }
    }
    return result;
  }

  static public void checkFieldGenType(FieldGenType genType) {
    if (FieldGenType.C_GENERATE != genType && FieldGenType.T_GENERATE != genType) {
      throw new ParamException("com.baifendian.bi.api.table.field.genType.error",
          "[" + FieldGenType.T_GENERATE.toString() + "," + FieldGenType.C_GENERATE + "]");
    }
  }

  static Pattern FieldNamePattern = Pattern.compile("\\$\\{([^${]*)\\}");
}
