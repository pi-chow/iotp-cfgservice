package com.cetiti.iotp.cfgservice.service;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelField;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;
import com.cetiti.iotp.cfgservice.domain.vo.CustomType;
import com.cetiti.iotp.cfgservice.domain.vo.ThingModel;
import com.cetiti.iotp.cfgservice.domain.vo.ThingModelType;
import com.cetiti.iotp.itf.cfgservice.vo.ThingModelFieldVo;

import java.io.File;
import java.util.List;

/**
 * 设备协议模型服务
 * @author zhouliyu
 * @since 2019-04-01 16:29:18
 */
public interface ThingModelService extends com.cetiti.iotp.itf.cfgservice.ThingModelService {

    /**
     * 设备协议模型列表
     * @param deviceModel 设备型号
     * */
    List<ThingModel> listThingModel(String deviceModel);

    /**
     * 获取设备协议模型数量
     * @param deviceModel 设备型号
     * */
    int thingModelCount(String deviceModel);

    /**
     * 修改设备型号名
     * @param deviceModel 设备型号
     * @param account 用户
     * */
    boolean updateDeviceModelName(JwtAccount account, String deviceModel);

    /**
     * 设备协议模型发布
     * @param deviceModel 设备型号
     * */
    List<String> publish(String deviceModel);

    /**
     * 获取thing-model.jar
     * */
    File getThingModelJar();

    /**
     * 添加模型头信息
     * @param thingModelHeader 模型头信息
     * */
    boolean addThingModelHeader(ThingModelHeader thingModelHeader);

    /**
     * 删除模型信息
     * @param thingModelId 模型ID
     * */
    boolean deleteThingModel(String thingModelId);

    /**
     * 添加模型字段
     * */
    boolean addThingModelField(ThingModelField thingModelField);

    /**
     * 修改模型字段
     * */
    boolean updateThingModelField(ThingModelField thingModelField);

    /**
     * 删除模型字段
     * @param thingModelFieldId 模型字段ID
     * */
    boolean deleteThingModelField(String thingModelFieldId);

    /**
     * 获取模型字段
     * @param thingModelFieldId 模型字段ID
     * */
    ThingModelField getThingModelField(String thingModelFieldId);

    /**
     * 获取模型字段列表
     * @param thingModelId 模型ID
     * */
    List<ThingModelField> listThingModelField(String thingModelId);

    /**
     * 获取感知模型字段
     * */
    List<ThingModelFieldVo> listSensorFieldByDeviceModel(String deviceModel);

    /**
     * 获取模型类型
     * */
    List<ThingModelType> getThingModelType();

    /**
     * 获取存储类型
     * @param thingModelType 模型类型
     */
    List<String> getStoreType(String thingModelType);

    /**
     * 获取结构类型
     * */
    List<String> getStructType();

    /**
     * 获取字段数据类型
     * @param structType 模型结构类型
     * */
    List<CustomType> getFieldCustomType(String structType);

}
