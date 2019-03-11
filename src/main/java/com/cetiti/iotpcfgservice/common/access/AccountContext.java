package com.cetiti.iotpcfgservice.common.access;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;

/**
 * 用来存放从session中获取的user。
 *
 * @author yangshutian
 */
public class AccountContext {
    private static ThreadLocal<JwtAccount> accountHolder = new ThreadLocal<JwtAccount>();

    public static void setAccount(JwtAccount account) {
        accountHolder.set(account);
    }

    public static JwtAccount getAccount() {
        return accountHolder.get();
    }
}
