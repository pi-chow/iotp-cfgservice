package com.cetiti.iotp.cfgservice.common.result;

import com.cetiti.ddapv2.iotplatform.common.BaseResultCode;
import xiaojian.toolkit.base.ResultCode;

public class CfgResultCode extends BaseResultCode {
    private static final long serialVersionUID = -2841000448976212032L;

    /**
     * 公共部分
     * */
    public static final ResultCode NOT_ALLOWED = new CfgResultCode(90009002, "禁止访问");
    public static final ResultCode DATABASE_ERROR = new CfgResultCode(90009003, "数据库错误");
    public static final ResultCode ZOOKEEPER_ERROR = new CfgResultCode(90009004, "zookeeper连接失败");

    /**
     * 设备型号
     * */
    public static final ResultCode DEVICE_MODEL_NOT_EXIST = new CfgResultCode(90001001, "设备型号不存在,请先添加");
    public static final ResultCode DEVICE_MODEL_EMPTY = new CfgResultCode(90001002, "设备型号为空");
    public static final ResultCode DEVICE_MODEL_ALREADY_EXIST = new CfgResultCode(90001003, "设备型号已存在");
    /**
     * 告警
     * */
    public static final ResultCode ALARM_EVENT_OFFLINE_REDIS = new CfgResultCode(90002001, "设备离线告警redis异常");
    public static final ResultCode ALARM_EVENT_OFFLINE_ZERO = new CfgResultCode(90002001, "设备离线告警需大于0");
    public static final ResultCode ALARM_START_AND_END = new CfgResultCode(90003002, "错误的起止时间");
    public static final ResultCode ALARM_DESCRIPTION_EMPTY = new CfgResultCode(90003003, "描述字段为空格");
    public static final ResultCode ALARM_CFG_ADD = new CfgResultCode(90001004, "新增告警配置失败");
    public static final ResultCode ALARM_CFG_UPDATE = new CfgResultCode(90001002, "更新告警配置失败");
    public static final ResultCode ALARM_CFG_DELETE = new CfgResultCode(90001002, "删除告警配置失败");
    /**
     * 设备协议模型
     * */
    public static final ResultCode THING_MODEL_EXIST = new CfgResultCode(90004001, "已经存在设备协议模型");
    public static ResultCode THING_MODEL_CONSTRUCTION_FAIL = new CfgResultCode(90004002, "设备协议模型构造失败");
    public static ResultCode THING_MODEL_COMPILE_FAIL = new CfgResultCode(90004003, "设备协议模型编译失败");
    public static ResultCode THING_MODEL_PACKAGE = new CfgResultCode(90004004, "设备协议模型打包失败");
    public static ResultCode THING_MODEL_JAVA_DELETE = new CfgResultCode(90004005, "java文件删除失败");
    public static ResultCode THING_MODEL_CLASS_DELETE = new CfgResultCode(90004006, "class文件删除失败");
    public static ResultCode THING_JAR_NOT_FOUND = new CfgResultCode(90004007, "jar文件获取失败");
    public static ResultCode THING_MODEL_MISS_FIELD = new CfgResultCode(90004008, "设备协议模型缺少属性");
    public static ResultCode THING_MODEL_MISS = new CfgResultCode(90004009, "发布模型时缺少感知设备类型");
    public static ResultCode THING_MODEL_INVALID = new CfgResultCode(90004010, "格式错误，属性、状态、感知数据只能有一个。");
    public static ResultCode THING_MODEL_TEMPLATE_MISS = new CfgResultCode(90004011, "从模板创建模型失败，没有发现模板。");
    public static ResultCode THING_MODEL_TEMPLATE_INVALID = new CfgResultCode(90004012, "从模板创建模型失败，此模型没有定义为模板。");
    public static ResultCode THING_MODEL_TEMPLATE_CREATE_EXIST = new CfgResultCode(90004013, "已经是模板");
    public static ResultCode THING_MODEL_TEMPLATE_CREATE_SENSORY_NOT_EXIST = new CfgResultCode(90004014, "缺少感知类协议模型");
    public static ResultCode THING_MODEL_STORE_TYPE_EMPTY = new CfgResultCode(90004015, "存储类型为空");
    public static ResultCode THING_MODEL_STRUCT_TYPE_EMPTY = new CfgResultCode(90004016, "结构类型空");
    public static final ResultCode THING_MODEL_ADD = new CfgResultCode(90004017, "设备模型添加失败");
    public static final ResultCode THING_MODEL_UPDATE = new CfgResultCode(90004018, "设备模型更新失败");
    public static final ResultCode THING_MODEL_DELETE = new CfgResultCode(90004019, "设备模型删除失败");
    public static final ResultCode THING_MODEL_TEMPLATE_DELETE = new CfgResultCode(90004020, "删除模板失败");
    public static final ResultCode THING_MODEL_PUBLISH = new CfgResultCode(90004021, "设备模型发布失败");



    public CfgResultCode() {
    }

    public CfgResultCode(int code, String desc) {
        super(code, desc);
    }

    public CfgResultCode(ResultCode resultCode) {
        super(resultCode);
    }


}
