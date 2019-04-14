package com.cetiti.iotp.cfgservice.mapper;

import com.cetiti.iotp.cfgservice.domain.entity.ThingModelField;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设备协议模型字段
 * @author zhouliyu
 * @since 2019-04-01 11:23:23
 */
@Repository
public interface ThingModelFieldMapper {

    /**
     * 获取字段列表
     * @param thingModelId 模型ID
     * */
    List<ThingModelField> listThingModelField(@Param("thingModelId") String thingModelId);

    /**
     * 获取字段
     * */
    ThingModelField getThingModelField(@Param("thingModelFieldId") String thingModelFieldId);

    /**
     * 新增字段
     *@param thingModelField 模型字段
     * */
    int insertSelective(ThingModelField thingModelField);

    /**
     * 修改字段
     * @param thingModelField 模型字段
     * */
    int updateSelective(ThingModelField thingModelField);

    /**
     * 删除字段
     * @param thingModelFieldId 字段ID
     * */
    int deleteByPrimaryKey(@Param("thingModelFieldId") String thingModelFieldId);

    /**
     * 获取设备感知数据属性
     * @param deviceModel 设备型号
     * */
    List<ThingModelField> listSensorFieldByDeviceModel(@Param("deviceModel")String deviceModel);


    /**
     * 字段统计
     * @param thingModelField 模型字段
     * */
    int selectCount(ThingModelField thingModelField);

}
