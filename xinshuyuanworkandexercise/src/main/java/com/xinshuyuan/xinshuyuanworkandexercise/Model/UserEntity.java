package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/3.
 */

public class UserEntity implements Serializable {
    /*
      * 用户类型
      * 用户名字
      * id
      */
    private String userName;
    private Integer userType;
    private Long id;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Integer getUserType() {
        return userType;
    }
    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}