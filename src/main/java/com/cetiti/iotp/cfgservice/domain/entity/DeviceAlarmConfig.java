package com.cetiti.iotp.cfgservice.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 告警配置
 * @author zhouliyu
 * @since 2019-03-28 14:25:08
 */
@Data
public class DeviceAlarmConfig implements Serializable {

    private static final long serialVersionUID = -5620306789722539022L;

    /**
     * 告警配置主键
     * */
    private String alarmId;

    private String deviceModel;

    private String deviceModelName;

    /**
     * 告警类型 : 感知;离线
     * */
    private String alarmType;

    /**
     * 字段：设备属性
     * */
    private String field;

    /**
     * 关系：阈值关系
     * */
    private String relation;

    /**
     * 阈值
     * */
    private String threshold;

    /**
     * 告警等级 0警告;1一般;2重要;3紧急;
     * */
    private Integer alarmLevel;

    private String description;

    private Date createTime;

    private String createUser;

    private Date modifyTime;

    private String modifyUser;

}
