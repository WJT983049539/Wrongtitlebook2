package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import android.content.Context;

/**
 * 主要得到user信息的类
 * Created by Administrator on 2017/6/27.
 */

public class GetUserInfoClass {
    public  Context context;
    private String userName;
    private Integer userType;
    private Long id;
    private  PerferenceService service;
    public GetUserInfoClass(Context context){
        this.context=context;
        service=new PerferenceService(context);

    }

    public Long getUserId(){
        id=service.getsharedPre().getLong("WrongId",0);
        return id;
    }
    public String getUserName(){
        userName=service.getsharedPre().getString("UserName","");
        return userName;
    }
    public Integer getUserType(){
        userType=service.getsharedPre().getInt("UserType",0);
        return userType;
    }


}
