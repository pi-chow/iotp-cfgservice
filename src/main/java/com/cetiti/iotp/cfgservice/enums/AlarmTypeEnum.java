package com.cetiti.iotp.cfgservice.enums;

/**
 * @author zhouliyu
 * @since 2019-03-26 11:44:11
 */
public enum  AlarmTypeEnum {

    SENSORY("感知报警", "sensory"), OFFLINE("离线报警", "offline");

    private String label;
    private String value;

    AlarmTypeEnum(String label, String value)
    {
        this.label = label;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
