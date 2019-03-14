package com.cetiti.iotp.cfgservice.service;


import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotp.cfgservice.domain.ThingModelDef;
import com.cetiti.iotp.cfgservice.domain.ThingModelField;
import com.cetiti.iotp.cfgservice.domain.UserDefStrut;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 模型服务
 *
 * @author zhouliyu
 */
public interface ThingModelService {


    /**
     * 发布所有的物模型。
     * @param account 用户账号。
     */
    List<ThingModelDef> allModelPublish(JwtAccount account);

    /**
     * 根据设备型号编号获取模型列表
     * @param account 设备账户。
     */
    List<ThingModelDef> modelListByDeviceModelId(String deviceModelId, JwtAccount account);

    /**
     * 通过模型编号获取模型
     * @param account 登录账号
     */
    ThingModelDef modelViewById(String modelId, JwtAccount account);

    /**
     * 通过设备型号获取物模型数量
     * @param account 账户。
     */
    int thingModelCount(String deviceModel, JwtAccount account);

    /**
     * 新增模型
     * @param account 账户类型。
     */
    String addModel(ThingModelDef message, JwtAccount account);
    
    /**
	 * 从一个模板里面复制一个物模型.
	 * 
	 * @param modelTemplateId 模板ID，也就是可以作为物模型模板的物模型ID。
     * @param deviceModelId 设备型号ID。
     * @param account 账户类型。
	 */
	void addModelFromTemplate(String modelTemplateId, String deviceModelId, JwtAccount account);
	
	/**
	 * 一个设备型号的物模型作为模板。
	 * 
	 * @param deviceModelId 设备型号。
	 * @param account 登录账号。
	 */
	void makeTemplate(String deviceModelId, JwtAccount account);

    /**
     * 修改模型
     * @param account 账户类型。
     */
    boolean updateModel(JwtAccount account, ThingModelDef message);

    /**
     * 修改模型
     * @param account 账户类型。
     */
    boolean updateModelName(JwtAccount account, String deviceModelName, String deviceModel);

    /**
     * 删除模型
     * @param account 账户类型。
     */
    boolean deleteModel(JwtAccount account, String thingModelId);

    /**
     * 新增模型字段列表
     * @param account 账号。
     */
    boolean fieldListAdd(JwtAccount account, String thingModelId, List<ThingModelField> fieldList);

    /**
     * 获取模型类型
     */
    List<String> strutType();

    /**
     * 获取模型存储类型。
     *
     * @return 消息存储类型。
     */
    List<String> storeTypes();
    
    /**
     * 用户定义结构体。
     * @param account 用户。
     * 
     * @return
     */
    List<UserDefStrut> userDefStrutType(JwtAccount account);

    /**
     * 得到可支持的存储类型。
     *
     * @param storeType
     * @return
     */
    List<String> getUsableStoreType(String storeType);

    /**
     * 根据设备型号获取物模型感知属性。
     *
     * @return 消息存储类型。
     */
    List<ThingModelField> listSensoryThingModelFieldByDeviceModel(String deviceModel);
    
    /**
     * 物模型模板分页。
     * 
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageInfo<ThingModelDef> templateModelView(JwtAccount account, String deviceModel, String deviceModelName, int currentPage, int pageSize);
    
    /**
     * 删除一个模板.
     * @param account 用户账户。
     * 
     * @param templateId 模板ID即为设备模型标识.
     * 
     * @return
     */
    boolean deleteTemplate(JwtAccount account, String templateId);

}
