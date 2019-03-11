package com.cetiti.ddapv2.iotplatform.biz.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 传感器类型id,type,name实体类。
 *
 * @author yangshutian
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class IdModelName {
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
}
