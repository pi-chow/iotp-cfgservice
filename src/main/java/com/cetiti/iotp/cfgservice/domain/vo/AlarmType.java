package com.cetiti.iotp.cfgservice.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 告警类型
 * @author zhouliyu
 * @since 2019-03-29 15:57:42
 */
@Data
public class AlarmType implements Serializable {

    private static final long serialVersionUID = -6897419797303704248L;

    /**
     * 告警类型标签
     * */
    private String label;
    /**
     * 告警类型 : 感知;离线
     * */
    private String value;
}
