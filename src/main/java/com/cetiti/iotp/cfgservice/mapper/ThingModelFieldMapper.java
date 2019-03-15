package com.cetiti.iotp.cfgservice.mapper;


import org.springframework.stereotype.Repository;

import com.cetiti.iotp.itf.cfgservice.vo.ThingModelField;

import java.util.List;

/**
 * 消息字段
 *
 * @author zhouliyu
 */
@Repository
public interface ThingModelFieldMapper {

    /**
     * 获取消息字段列表
     *
     * @param messageId
     */
    List<ThingModelField> fieldListById(String messageId);

    /**
     * 新增消息字段
     *
     * @param field
     */
    int add(ThingModelField field);

    /**
     * 删除消息字段列表
     *
     * @param messageId
     */
    int fieldListDelete(String messageId);

    /**
     * 根据设备型号获取物模型感知属性。
     */
    List<ThingModelField> listSensoryThingModelFieldByDeviceModel(String deviceModel);
}
