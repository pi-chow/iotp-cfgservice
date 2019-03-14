package com.cetiti.iotp.cfgservice.common.id;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ModuleEnum {

    GW("GW", "01"), CLOUD("CLOUD", "02");

    private static Map<String, ModuleEnum> lookup = new HashMap<String, ModuleEnum>();

    static {
        for (ModuleEnum me : ModuleEnum.values()) {
            lookup.put(me.moduleName, me);
        }
    }

    /**
     * 得到所有的模块列表。
     *
     * @return 模块列表。
     */
    public static List<ModuleEnum> getModules() {
        return Lists.newArrayList(lookup.values());
    }

    /**
     * 检查模块名字是否在模块列表之中。
     *
     * @param moduleName 模块名称。
     * @return
     */
    public static boolean checkModuleValid(String moduleName) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(moduleName));

        return lookup.containsKey(moduleName);
    }

    /**
     * 通过模块名称查找。
     *
     * @param moduleName
     * @return
     */
    public static ModuleEnum lookup(String moduleName) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(moduleName));

        return lookup.get(moduleName);
    }

    /**
     * 类别
     */
    public String moduleName;
    public String code;

    ModuleEnum(String name, String prefix) {
        this.moduleName = name;
        this.code = prefix;
    }

    public static String getPrefix(String moduleName) {
        Preconditions.checkArgument(moduleName != null && moduleName.trim().length() > 0);

        ModuleEnum me = lookup.get(moduleName);
        if (me == null) {
            return null;
        }

        return me.code;
    }
}
