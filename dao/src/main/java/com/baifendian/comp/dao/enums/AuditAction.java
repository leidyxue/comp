package com.baifendian.comp.dao.enums;

public enum AuditAction {
  /**
   * 新增操作，需要向系统中新插入数据的时候，比如增加：逻辑实体、物理实体、表、数据源等操作
   */
  INSERT,
  /**
   * 修改操作，修改已经存在的记录，更新状态等
   */
  UPDATE,
  /**
   * 删除操作，删除某个记录
   */
  DELETE,
  /**
   * 发布操作，发布信息，比如，发布资源、发布工作流都属于此类，对应有 "开发和线上状态" 的都需要
   */
  PUBLISH,
  /**
   * 执行操作，提交到调度系统执行的，需要观察运行状态的，属于此类
   */
  EXECUTION,
  /**
   * 用户登录
   */
  LOGIN,
  /**
   * 用户登出
   */
  LOGOUT,

  /**
   * 文件上传
   */
  FILE_IMPORT
}
