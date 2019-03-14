package com.cetiti.iotp.cfgservice.common.result;

import com.cetiti.ddapv2.iotplatform.common.BaseResultCode;
import xiaojian.toolkit.base.ResultCode;

public class CfgResultCode extends BaseResultCode {
    private static final long serialVersionUID = -2841000448976212032L;

    /**
     * 公共部分
     * */
    public static final ResultCode NOT_ALLOWED = new CfgResultCode(9002, "禁止访问");
    public static final ResultCode DATABASE_ERROR = new CfgResultCode(9003, "数据库错误");
    public static final ResultCode ZOOKEEPER_ERROR = new CfgResultCode(9004, "zookeeper连接失败");

    /**
     * 设备型号
     * */
    public static final ResultCode DEVICE_MODEL_NOT_EXIST = new CfgResultCode(4001, "设备型号不存在,请先添加");
    public static final ResultCode DEVICE_MODEL_ALREADY_EXIST = new CfgResultCode(4002, "设备型号已存在");


    /**
     * 设备协议模型
     * */
    public static final ResultCode THING_MODEL_EXIST = new CfgResultCode(4003, "已经存在设备协议模型");
    public static ResultCode THING_MODEL_CONSTRUCTION_FAIL = new CfgResultCode(6001, "设备协议模型构造失败");
    public static ResultCode THING_MODEL_COMPILE_FAIL = new CfgResultCode(6002, "设备协议模型编译失败");
    public static ResultCode THING_MODEL_PACKAGE = new CfgResultCode(6003, "设备协议模型打包失败");
    public static ResultCode THING_MODEL_JAVA_DELETE = new CfgResultCode(6004, "java文件删除失败");
    public static ResultCode THING_MODEL_CLASS_DELETE = new CfgResultCode(6005, "class文件删除失败");
    public static ResultCode THING_JAR_NOT_FOUND = new CfgResultCode(6005, "jar文件获取失败");
    public static ResultCode THING_MODEL_MISS_FIELD = new CfgResultCode(6006, "设备协议模型缺少属性");
    public static ResultCode THING_MODEL_MISS = new CfgResultCode(6007, "发布模型时缺少感知设备类型");
    public static ResultCode THING_MODEL_INVALID = new CfgResultCode(6008, "格式错误，属性、状态、感知数据只能有一个。");
    public static ResultCode THING_MODEL_TEMPLATE_MISS = new CfgResultCode(6009, "从模板创建模型失败，没有发现模板。");
    public static ResultCode THING_MODEL_TEMPLATE_INVALID = new CfgResultCode(6010, "从模板创建模型失败，此模型没有定义为模板。");
    public static ResultCode THING_MODEL_TEMPLATE_CREATE_EXIST = new CfgResultCode(6011, "已经是模板");
    public static ResultCode THING_MODEL_TEMPLATE_CREATE_SENSORY_NOT_EXIST = new CfgResultCode(6012, "缺少感知类协议模型");

    public CfgResultCode() {
    }

    public CfgResultCode(int code, String desc) {
        super(code, desc);
    }

    public CfgResultCode(ResultCode resultCode) {
        super(resultCode);
    }


}
