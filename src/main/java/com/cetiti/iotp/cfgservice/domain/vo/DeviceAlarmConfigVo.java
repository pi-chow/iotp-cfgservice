package com.cetiti.iotp.cfgservice.domain.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * @author zhouliyu
 * @since 2019-03-28 10:44:55
 */
@Data
public class DeviceAlarmConfigVo implements Serializable {

    private static final long serialVersionUID = 6508148557689157629L;
    private String deviceModel;

    /**
     *  字段：设备监测属性
     * */
    private String field;

    /**
     *  设备告警条件
     * */
    private String conditions;

    private String description;

    private String createUser;

}
