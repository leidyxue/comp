package com.baifendian.comp.common.structs.account;

import com.baifendian.comp.common.consts.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

@Deprecated
public class Session {
  /**
   * session ID
   */
  private String id;

  /**
   * 用户登录IP
   */
  private String ip;

  /**
   * 用户登录时间
   */
  @JsonFormat(pattern = Constants.BASE_DATETIME_FORMAT)
  private Date startTime;

  /**
   * 用户Session过期时间
   */
  @JsonFormat(pattern = Constants.BASE_DATETIME_FORMAT)
  private Date endTime;

  private User user;

  public String userId(){
    return user.getId();
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Session(String id, String ip, Date startTime, Date endTime,
      User user) {
    this.id = id;
    this.ip = ip;
    this.startTime = startTime;
    this.endTime = endTime;
    this.user = user;
  }

  public Session() {
  }

  /**
   * 登录用户信息
   */





  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
