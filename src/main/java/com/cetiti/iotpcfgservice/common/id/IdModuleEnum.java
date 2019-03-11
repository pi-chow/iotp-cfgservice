package com.cetiti.iotpcfgservice.common.id;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

public enum IdModuleEnum {

    USER("USER", "01"), GATEWAY("GATEWAY", "02"), APPLICATION("APPLICATION", "03"), DEVICETYPE("DEVICETYPE",
            "04"), DEVICE("DEVICE", "05"), ALARM("ALARM", "06"), MODEL("MODEL", "07"), GROUP("GROUP", "08");

    private static Map<String, IdModuleEnum> lookup = new HashMap<String, IdModuleEnum>();

    static {
        for (IdModuleEnum me : IdModuleEnum.values()) {
            lookup.put(me.moduleName, me);
        }
    }

    /**
     * 类别
     */
    public String moduleName;
    public String code;

    IdModuleEnum(String name, String prefix) {
        this.moduleName = name;
        this.code = prefix;
    }

    public static String getPrefix(String moduleName) {
        Preconditions.checkArgument(moduleName != null && moduleName.trim().length() > 0);

        IdModuleEnum me = lookup.get(moduleName);
        if (me == null) {
            return null;
        }

        return me.code;
    }

}
