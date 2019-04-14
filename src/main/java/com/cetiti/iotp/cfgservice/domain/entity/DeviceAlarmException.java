package com.cetiti.iotp.cfgservice.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备告警异常
 * @author zhouliyu
 * @since 2019-03-28 17:28:36
 */
@Data
public class DeviceAlarmException implements Serializable {
    private static final long serialVersionUID = 8351937639752776487L;

    private Integer id;
    private String deviceModel;
    private String deviceModelName;
    private String deviceSn;
    /**
     * 触发报警时的当前值
     * */
    private String value;
    /**
     * 告警信息
     * */
    private String alarm;
    /**
     * 告警等级
     * */
    private String alarmLevel;

    /**
     * 告警时间
     * */
    public Date createTime;

    /**
     * 告警条件
     * */
    private String conditions;
    /**
     * 字段：设备监测属性
     * */
    private String indexCode;


}
