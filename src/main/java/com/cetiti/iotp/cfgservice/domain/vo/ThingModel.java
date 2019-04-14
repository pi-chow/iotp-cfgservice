package com.cetiti.iotp.cfgservice.domain.vo;

import com.cetiti.iotp.cfgservice.domain.entity.ThingModelField;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 设备协议模型
 * @author zhouliyu
 * @since 2019-04-01 11:13:27
 */
@Data
public class ThingModel implements Serializable {
    private static final long serialVersionUID = 3343118326899539603L;
    private ThingModelHeader thingModelHeader;
    private List<ThingModelField> thingModelFields;
}
