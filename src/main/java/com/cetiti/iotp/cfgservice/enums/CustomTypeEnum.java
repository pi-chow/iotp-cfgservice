package com.cetiti.iotp.cfgservice.enums;

/**
 * 数据类型转换
 */
public enum CustomTypeEnum {
    INT("int", int.class), BOOLEAN("boolean", boolean.class), FLOAT("float", float.class), STRING("string",
            String.class), VARSTRING("varstring",
            String.class), ARRAY("array", byte[].class), VARIANT("variant", byte[].class);

    private String key;
    private Class value;

    CustomTypeEnum(String key, Class value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Class getValue() {
        return value;
    }

    public static Class getValueByKey(String key) {
        for (CustomTypeEnum typeEnum : CustomTypeEnum.values()) {
            if (typeEnum.getKey().equals(key)) {
                return typeEnum.getValue();
            }
        }
        return null;
    }

}
