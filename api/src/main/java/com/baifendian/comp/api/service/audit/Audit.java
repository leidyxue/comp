package com.baifendian.comp.api.service.audit;

import com.baifendian.comp.dao.enums.AuditAction;
import com.baifendian.comp.dao.enums.AuditPageName;
import com.bfd.systemmanager.client.holder.ShiroUserHolder;
import com.bfd.systemmanager.remote.ShiroUser;
import java.text.MessageFormat;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Audit {

  private final static Logger LOGGER = LoggerFactory.getLogger(Audit.class);

  public void pushInsert(AuditPageName pageName, String msgKey,
      Object... params) {
    push(pageName, AuditAction.INSERT, msgKey, params);
  }

  public static void pushUpdate(AuditPageName pageName, String msgKey,
      Object... params) {
    push(pageName, AuditAction.UPDATE, msgKey, params);
  }

  public static void pushDelete(AuditPageName pageName, String msgKey,
      Object... params) {
    push(pageName, AuditAction.DELETE, msgKey, params);
  }

  public static void push(AuditPageName pageName, AuditAction action, String msgKey, Object... params) {
    String host = null;
    String userName = null;
    try {
      ShiroUser shiroUser = ShiroUserHolder.getUser();
      if (shiroUser != null) {
        userName = shiroUser.getName();
      }
      Session session = SecurityUtils.getSubject().getSession();
      if (session != null){
        host = session.getHost();
      }
    } catch (Exception e) {
      LOGGER.error("Get session error.", e);
      //return;
    }

    LOGGER.info(AUDIT_LOG, userName, "Comparison", pageName.name(), action.name(),
        host, MessageFormat.format(msgKey, params));
  }

  private static final String AUDIT_LOG = "[{}][{}][{}][{}][{}]{}";
}
