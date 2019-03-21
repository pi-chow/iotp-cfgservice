package com.cetiti.iotp.cfgservice.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 异常告警实体类。
 * @author yangshutian
 */
public class ExceptionAlarm implements Serializable {

    private static final long serialVersionUID = -5266881515385621702L;

    public int id;
    /**
     * 传感器类型
     */
    public String deviceModel;

    /**
     * 传感器型号名称
     */
    public String deviceModelName;

    /**
     * 传感器序列号
     */
    public String deviceSn;

    /**
     * 报警触发时的当前值
     */
    public String value;

    /**
     * 报警信息
     */
    public String alarm;

    public Date createTime;

    /**
     * 报警规则
     */
    public String conditions;

    /**
     * 报警字段
     */
    public String indexCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceModelName() {
        return deviceModelName;
    }

    public void setDeviceModelName(String deviceModelName) {
        this.deviceModelName = deviceModelName;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
