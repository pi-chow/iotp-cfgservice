package com.cetiti.iotp.cfgservice.mapper;

import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 设备协议模型头信息
 * @author zhouliyu
 * @since 2019-04-01 11:35:25
 */
@Repository
public interface ThingModelMapper {

    /**
     * 获取模型头信息列表
     *
     * @param deviceModel    设备型号
     * @param thingModelType 模型类型
     */
    List<ThingModelHeader> listThingModelHeader(@Param("deviceModel") String deviceModel, @Param("thingModelType") String thingModelType);

    /**
     * 获取模型头信息
     *
     * @param thingModelId 模型ID
     */
    ThingModelHeader getThingModelHeader(@Param("thingModelId") String thingModelId, @Param("deviceModel") String deviceModel, @Param("thingModelType") String thingModelType);

    /**
     * 新增模型头信息
     *
     * @param thingModelHeader 模型头信息
     */
    int insertSelective(ThingModelHeader thingModelHeader);

    /**
     * 删除模型信息
     *
     * @param thingModelId 模型ID
     */
    int deleteByPrimaryKey(@Param("thingModelId") String thingModelId);

    /**
     * 获取作为模型模板的模型列表
     *
     * @param params 搜索条件
     */
    List<ThingModelHeader> listThingModelHeaderAsTemplate(Map<String, Object> params);

    /**
     * 删除模型模板
     *
     * @param deviceModel 设备型号
     */
    int deleteThingModelHeaderAsTemplate(@Param("deviceModel") String deviceModel);

    /**
     * 模型作为模板
     * @param thingModelHeader 模型头信息
     */
    int thingModelAsTemplate(ThingModelHeader thingModelHeader);

    /**
     * 模型统计
     * @param thingModelHeader 模型头信息
     */
    int selectCount(ThingModelHeader thingModelHeader);

    /**
     * 更新模型对应型号名
     * */
    int updateDeviceModelName(ThingModelHeader thingModelHeader);
}