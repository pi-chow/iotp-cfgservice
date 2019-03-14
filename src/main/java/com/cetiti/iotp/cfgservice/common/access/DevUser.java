package com.cetiti.iotp.cfgservice.common.access;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;

/**
 * 判断用户是否是开发者
 * */
public class DevUser {

    public static String isDeveloper(JwtAccount account){

        String userId = null;

        if(account == null){
            return userId;
        }

        if(account.getRoles().equals("dev")){
            userId = account.getUserId();
        }

        return userId;
    }
}
