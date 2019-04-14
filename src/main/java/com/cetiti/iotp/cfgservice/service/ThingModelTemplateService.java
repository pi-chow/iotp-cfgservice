package com.cetiti.iotp.cfgservice.service;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;

import java.util.List;
import java.util.Map;

/**
 * 设备协议模型模板
 * @author zhouliyu
 * @since 2019-04-02 08:59:05
 */
public interface ThingModelTemplateService {

    /**
     * 设备协议模型作为模板
     * @param account 用户
     * @param deviceModel 设备型号
     * */
    void addThingModelTemplate(JwtAccount account, String deviceModel);

    /**
     * 复制模板
     * @param account 用户
     * @param deviceModel 设备型号
     * */
    void copyThingModelTemplate(JwtAccount account, String deviceModel, String templateId);

    /**
     * 设备协议模型模板列表
     * @param account 用户
     * @param params 搜索条件
     * */
    List<ThingModelHeader> listThingModelTemplate(JwtAccount account, Map<String, Object> params);

    /**
     * 删除协议模型模板
     * @param deviceModel 设备型号
     * */
    boolean deleteThingModelTemplate(String deviceModel);
}
