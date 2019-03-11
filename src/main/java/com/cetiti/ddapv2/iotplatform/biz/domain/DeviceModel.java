package com.cetiti.ddapv2.iotplatform.biz.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 传感器类型实体类。
 *
 * @author yangshutian
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class DeviceModel implements Serializable {

    /**
     * 自增Id
     */
    public int id;

    /**
     * 传感器类型Id
     */
    public String deviceModelId;

    /**
     * 传感器类型
     */
    public String deviceModel;

    /**
     * 名称
     */
    @JsonProperty("name")
    public String deviceModelName;

    /**
     * 创建时间
     */
    public Date createTime;

    /**
     * 修改时间
     */
    public Date modifyTime;

    /**
     * 创建用户Id
     */
    public String createUserId;

    /**
     * 修改用户Id
     */
    public String modifyUserId;

    /**
     * 关联的传感器数量
     */
    public int deviceNum;

    /**
     * 关联的应用数量
     */
    public int applicationNum;

    /**
     * 图片编号
     */
    public Integer imageNum;

    /**
     * 接入方式
     */
    public String pluginType;

    /**
     * 规模
     */
    public Integer scale;

    /**
     * 功能描述
     */
    public String description;

    /**
     * 接入方计划上线时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date planOnlineTime;

    @Override
    public String toString() {
        return "DeviceModel [id=" + id + ", deviceModelId=" + deviceModelId
                + ", deviceModel=" + deviceModel + ", deviceModelName="
                + deviceModelName + ", createTime=" + createTime
                + ", modifyTime=" + modifyTime + ", createUserId="
                + createUserId + ", modifyUserId=" + modifyUserId
                + ", deviceNum=" + deviceNum + ", applicationNum="
                + applicationNum + ", imageNum=" + imageNum + "]";
    }
}
