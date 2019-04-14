package com.cetiti.iotp.cfgservice.common.access;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;

/**
 * 配置UserArgumentResolver。
 * @author zhouliyu
 * @since 2019-04-11 14:19:07
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
