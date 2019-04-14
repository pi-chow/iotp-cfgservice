package com.cetiti.iotp.cfgservice.common.result;

/**
 * @author zhouliyu
 * @since 2019-04-02 14:02:43
 */
public enum CfgErrorCodeEnum {
    /**
     * 全局异常
     * */
    NOT_ALLOWED(90009002, "未登录,禁止访问"),
    ACCESS_JWT_ERROR(90009003, "jwt错误"),
    ZOOKEEPER_ERROR(90009004, "zookeeper连接失败"),
    ZOOKEEPER_UPDATE_DATA_ERROR(90009005, "ZK节点数据更新失败"),

    /***
     * 设备型号
     * */
    DEVICE_MODEL_NOT_EXIST(90001001, "设备型号不存在,请先添加"),


    /**
     * 告警配置
     * */
    ALARM_ADD_ERROR(90002001, "告警配置新增失败"),
    ALARM_UPDATE_ERROR(90002002, "告警配置修改失败"),
    ALARM_DELETE_ERROR(90002003, "告警配置删除失败"),
    ALARM_DESCRIPTION_EMPTY(90002004,"描述字段为空格"),
    ALARM_EVENT_OFFLINE_REDIS(90002005, "设备离线配置上报异常"),
    ALARM_EVENT_OFFLINE_ZERO(90002006, "设备离线配置需大于0"),

    /**
     * 设备协议模型
     * */
    THING_MODEL_EXIST(90003001, "设备协议模型已存在"),
    THING_MODEL_INVALID(90003002, "格式错误：属性、状态、感知数据唯一"),
    THING_MODEL_SENSOR_NOT_EXIST(90003003, "缺少感知协议模型"),
    THING_MODEL_ADD_ERROR(90003004, "设备协议模型添加失败"),
    THING_MODEL_DELETE_ERROR(90003005, "设备协议模型删除失败"),
    THING_MODEL_NAME_EXISTED(90003006, "设备协议模型事件名称重复"),

    THING_MODEL_FIELD_NAME_EMPTY(90003015, "字段名为空"),
    THING_MODEL_FIELD_NAME_EXIST(90003016, "字段名重复"),
    THING_MODEL_FIELD_ADD_ERROR (90003017, "字段添加失败"),
    THING_MODEL_FIELD_UPDATE_ERROR(90003018, "字段更新失败"),
    THING_MODEL_FIELD_DELETE_ERROR(90003019, "字段删除失败"),
    THING_MODEL_LINK_FIELD_EMPTY (90003020, "关联字段不存在"),
    THING_MODEL_LINK_FIELD_NOT_INT(90003021, "关联字段非整型"),
    THING_MODEL_LINK_FIELD_EXIST(90003022, "该字段已作为关联字段,需先切换非定长字符串关联字段"),

    THING_MODEL_TEMPLATE_MISS(90003041, "从模板创建模型失败，没有发现模板"),
    THING_MODEL_TEMPLATE_INVALID(90003042, "该型号未定义模板"),
    THING_MODEL_TEMPLATE_EXIST(90003043, "已经是模板"),
    THING_MODEL_TEMPLATE_DELETE(90003044, "模板删除失败"),

    THING_MODEL_CONSTRUCTION_FAIL(90003051, "设备协议模型构建失败"),
    THING_MODEL_COMPILE_FAIL(90003052, "设备协议模型编译失败"),
    THING_MODEL_PACKAGE_FAIL(90003053, "设备协议模型打包失败"),
    THING_MODEL_JAVA_EMPTY(90003054, "设备协议模型源文件为空"),
    THING_MODEL_MISS_FIELD(90003055, "设备协议模型缺少字段"),
    THING_MODEL_JAR_NOT_FOUND(90003056, "thing-model.jar获取失败"),
    THING_MODEL_PUBLISH_ERROR(90003057, "设备协议模型发布失败");






    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    CfgErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
