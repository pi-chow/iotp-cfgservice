package com.cetiti.iotp.cfgservice.domain.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备协议模型头信息
 * @author zhouliyu
 * @since 2019-04-01 10:35:41
 */
@Data
public class ThingModelHeader implements Serializable {

    private static final long serialVersionUID = -1665586298580720098L;

    /**
     * 设备型号
     * */
    private String deviceModel;

    /**
     * 设备型号名
     * */
    private String deviceModelName;

    /**
     * 设备协议模型ID
     * */
    private String thingModelId;

    /**
     * 设备协议模型类型:sensor,prop,status,event
     * */
    private String thingModelType;

    /**
     * 设备协议模型名称
     * */
    private String thingModelName;

    /**
     * 存储类型：MYSQL、HBASE、HBASE_BATCH、REDIS、JMS、PUSH
     * */
    private String storeType;

    /**
     * 结构类型：lv_bytes、json、xml
     * */
    private String structType;

    /**
     * 描述
     * */
    private String description;

    /**
     * 建表语句
     * */
    private String sqlStatements;

    /**
     * 是否可以当模板使用： 0.不可以;1.可以
     * */
    private Integer template;

    private Date createTime;

    private String createUser;

    private Date modifyTime;

    private String modifyUser;

}
