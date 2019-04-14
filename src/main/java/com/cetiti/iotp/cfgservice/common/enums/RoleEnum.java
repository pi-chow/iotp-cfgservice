package com.cetiti.iotp.cfgservice.common.enums;

/**
 * @author zhouliyu
 * @since 2019-04-11 16:12:39
 */
public enum RoleEnum {
    ROLE_DEV("dev");


    private String value;

    public String getValue() {
        return value;
    }

    RoleEnum(String value) {
        this.value = value;
    }
}
