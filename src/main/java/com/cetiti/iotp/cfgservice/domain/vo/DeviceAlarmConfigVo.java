package com.cetiti.iotp.cfgservice.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author zhouliyu
 * @since 2019-03-28 10:44:55
 */
public class DeviceAlarmConfigVo implements Serializable {
    private static final long serialVersionUID = 5680656056419787628L;

    private String deviceModel;

    private String field;

    private String conditions;

    private String description;

    private String createUser;

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
