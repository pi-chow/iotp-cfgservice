package com.cetiti.iotp.cfgservice.domain;

import java.io.Serializable;

/**
 * @author zhouliyu
 * @since 2019-03-26 15:00:15
 */
public class AlarmType implements Serializable {
    private static final long serialVersionUID = -4601158124967947136L;
    
    private String label;
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
