package com.baifendian.comp.api.service.audit;

import com.baifendian.comp.api.dto.ParamUtil;
import com.baifendian.comp.api.dto.comparison.TaskDTO;
import com.baifendian.comp.common.annotation.AuditProperty;
import com.baifendian.comp.dao.enums.AuditAction;
import com.baifendian.comp.dao.enums.AuditPageName;
import com.baifendian.comp.dao.postgresql.model.table.Table;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.Field;

public class AuditUtil {

  public static void main(String[] args) {
    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setName("test");
    System.out.println(getAuditData(taskDTO));

    AuditUtil.pushInsert(AuditPageName.COMPTASK, AuditUtil.getAuditData(taskDTO));
  }

  public static String getAuditData(Object obj){
    if (obj == null){
      return " ";
    }

    Class classObj = obj.getClass();
    Field fields[] = classObj.getDeclaredFields();
    if (fields == null){
      return " ";
    }

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode root = mapper.createObjectNode();

    for (Field field: fields) {
      AuditProperty paramProperty = field.getAnnotation(AuditProperty.class);
      if (paramProperty == null){
        continue;
      }
      Object val = ParamUtil.getFieldValue(obj, field);
      if (val == null){
        continue;
      }
      root.put(field.getName(), val.toString());
    }

    return root.toString();
  }

  public static void pushDel(AuditPageName pageName, Object... params){
    Audit.pushDelete(pageName, getKey(AuditAction.DELETE, pageName), params);
  }
  public static void pushInsert(AuditPageName pageName, Object... params){
    Audit.push(pageName, AuditAction.INSERT, getKey(AuditAction.INSERT, pageName), params);
  }

  public static void pushUpdate(AuditPageName pageName, Object... params){
    Audit.pushUpdate(pageName, getKey(AuditAction.UPDATE, pageName), params);
  }

  public static void pushFileImport(AuditPageName pageName, Object... params){
    Audit.pushUpdate(pageName, getKey(AuditAction.FILE_IMPORT, pageName), params);
  }

  public static String getKey(AuditAction action, AuditPageName pageName){
    String name = pageName.name();

    switch (action){
      case UPDATE:
        return "Update "+name+", old "+name+": {0}, new "+name+": {1}";

      case INSERT:
        return "Insert "+name+", "+name+":{0}";

      case DELETE:
        return "Delete "+name+", "+name+":{0}";

      case FILE_IMPORT:
        return "Upload file generate table, tableName:{0}";
    }

    return "";
  }

  public static String getTable(AuditAction action){
    switch (action){
      case UPDATE:
        return "Update worksheet, old worksheet: {0}, new worksheet: {1}";

      case INSERT:
        return "Insert worksheet, worksheet:{0}";

      case DELETE:
        return "Delete worksheet, worksheet:{0}";
    }

    return "";
  }

  public static String getTableUpdate(){
    return "Update worksheet, old worksheet: {0}, new worksheet: {1}";
  }

  public static String getTableInsert(){
    return "Update worksheet, old worksheet: {0}, new worksheet: {1}";
  }
}
