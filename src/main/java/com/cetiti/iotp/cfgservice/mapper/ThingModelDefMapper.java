package com.cetiti.iotp.cfgservice.mapper;

import com.cetiti.iotp.cfgservice.domain.ThingModelDef;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 消息
 *
 * @author zhouliyu
 */
@Repository
public interface ThingModelDefMapper {

	/**
	 * 根据用户以及类别查询设备模型定义列表。
	 * 
	 * @param userId
	 * @param strutType
	 * @return
	 */
	List<ThingModelDef> modelList(@Param("userId") String userId, @Param("strutType") String strutType);

	/**
	 * 根据设备型号ID获得设备型号对应的设备模型列表。
	 */
	List<ThingModelDef> modelListByDeviceModelId(String deviceId);

	List<ThingModelDef> modelViewByDeviceModelIdAndModelType(@Param("deviceModelId") String deviceModelId,
                                                             @Param("modelType") String modelType);

	/**
	 * 通过物模型编号获取设备模型信息。
	 */
	ThingModelDef modelViewById(String modelId);

	/**
	 * 通过型号查询数量。
	 */
	int thingModelCount(@Param("userId") String userId, @Param("deviceModel") String deviceModel);

	/**
	 * 新增设备模型。
	 */
	int modelAdd(ThingModelDef model);

	/**
	 * 在设备型号更新名字的时候同时更新t_thing_model表里面的冗余字段。
	 *
	 * @param args
	 *            参数name - 设备型号新名字； deviceModelId - 设备型号ID。modifyUser -
	 * @return
	 */
	int deviceModelNameUpdateById(@Param("name") String name, @Param("deviceModelId") String deviceModelId,
                                  @Param("modifyUser") String modifyUser);

	/**
	 * 更新消息
	 */
	int modelUpdate(ThingModelDef model);

	/**
	 * 在设备型号更新名字的时候同时更新t_thing_model表里面的冗余字段。
	 */
	int deviceModelNameUpdateByModel(Map<String, Object> args);

	/**
	 * 删除消息
	 *
	 * @param modelId
	 */
	int modelDelete(String modelId);

	/**
	 * 得到作为模板的物模型列表，提取感知设备数据。
	 * 
	 * @return 得到作为模板的物模型列表。
	 */
	List<ThingModelDef> templateModelView(Map<String, Object> paras);

	/**
	 * 删除设备模型模板。数据库里面修改templateId为0
	 * 
	 * @param deviceModelId
	 * @return
	 */
	int deleteTemplateByDeviceModelId(String deviceModelId);

}
