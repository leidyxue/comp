package com.baifendian.comp.common.structs.account;

import com.baifendian.comp.common.consts.Constants;
import com.baifendian.comp.common.enums.UserRole;
import com.baifendian.comp.common.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

@Deprecated
public class User {
  /**
   * 用户ID
   */
  private String id;

  /**
   * 用户名
   */
  private String name;

  /**
   * 用户密码
   */
  private String password;

  /**
   * 用户邮箱
   */
  private String email;

  /**
   * 用户手机号
   */
  private String phone;

  /**
   * 用户状态
   */
  private UserStatus status;

  /**
   * 用户身份
   */
  private UserRole role;

  /**
   * 用户创建时间
   */
  @JsonFormat(pattern = Constants.BASE_DATETIME_FORMAT)
  private Date createTime = new Date();

  /**
   * 用户修改时间
   */
  @JsonFormat(pattern = Constants.BASE_DATETIME_FORMAT)
  private Date modifyTime = new Date();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }
}
