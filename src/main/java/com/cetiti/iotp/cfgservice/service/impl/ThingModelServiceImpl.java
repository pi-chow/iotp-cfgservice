package com.cetiti.iotp.cfgservice.service.impl;


import com.cetiti.ddapv2.iotplatform.biz.domain.DeviceModel;
import com.cetiti.ddapv2.iotplatform.biz.service.DeviceModelService;
import com.cetiti.ddapv2.iotplatform.common.DataTypeStoreTypeRelation;
import com.cetiti.ddapv2.iotplatform.common.StoreTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.ThingDataStrutTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.ThingDataTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.iotp.cfgservice.common.zookeeper.CfgZkClient;
import com.cetiti.iotp.cfgservice.common.access.DevUser;
import com.cetiti.iotp.cfgservice.common.id.UniqueIdGenerator;
import com.cetiti.iotp.cfgservice.common.result.CfgResultCode;
import com.cetiti.iotp.cfgservice.common.utils.SqlGenerator;
import com.cetiti.iotp.cfgservice.domain.ThingModelDef;
import com.cetiti.iotp.cfgservice.domain.ThingModelField;
import com.cetiti.iotp.cfgservice.domain.UserDefStrut;
import com.cetiti.iotp.cfgservice.mapper.ThingModelDefMapper;
import com.cetiti.iotp.cfgservice.mapper.ThingModelFieldMapper;
import com.cetiti.iotp.cfgservice.service.ThingModelProcessor;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 模型服务实现类
 *
 * @author zhouliyu
 */
@org.apache.dubbo.config.annotation.Service(interfaceClass = ThingModelService.class)
@Service
public class ThingModelServiceImpl implements ThingModelService {

	private static final Logger logger = LoggerFactory.getLogger(ThingModelServiceImpl.class);
    private static final String PATH = "/iot_cfg_publish";

	@Autowired
	private ThingModelDefMapper modelDefMapper;

	@Reference
    private DeviceModelService deviceModelService;

	@Autowired
	private ThingModelFieldMapper fieldMapper;

	@Autowired
	private ThingModelProcessor modelProcessor;

	@Autowired
	private UniqueIdGenerator uniqueIdGenerator;

	@Autowired
	private CfgZkClient cfgZkClient;


	/**
	 * 发布所有的物模型。
	 *
	 * @return 返回没有发布成功的物模型。
	 */
	public List<ThingModelDef> allModelPublish(JwtAccount account) {

		logger.debug("Publis all model, account[{}].", account);

		List<ThingModelDef> modelList = modelDefMapper.modelList(DevUser.isDeveloper(account), null);
        List<ThingModelDef> failure = Lists.newArrayList();

		if (modelList == null || modelList.size() == 0) {
			return Lists.newArrayList();
		}
		//物模型发布
		List<ThingModelDef> thingModelList = modelList.stream().filter(e -> {
			return e.getThingModelType() != null
					&& !e.getThingModelType().equalsIgnoreCase(ThingDataTypeEnum.STRUT.getValue());
		}).collect(Collectors.toList());

		if(thingModelList.size()!=0){
            //-- 清空
            modelProcessor.clearAllTarget();
        }

		for (ThingModelDef model : thingModelList) {
			try {
				modelProcessor.constructor(model);
			} catch (BizLocaleException exception) {
				logger.error("Construct model[" + model + "] fail.", exception);
				failure.add(model);
			}
		}
        //如果结构体发布失败，则需要返回错误，因为可能结构体发布失败会影响模型发布失败。
        if (failure.size() > 0) {
            return failure;
        }else {
            // -- 打包
            if(modelProcessor.packageAll() == 0){
                // -- zookeeper 推送通知
                updateNodeData();
            }
        }
		return failure;
	}

	/**
	 * 根据型号查询对应的模型列表。
	 */
	@Override
	public List<ThingModelDef> modelListByDeviceModelId(String deviceModelId, JwtAccount account) {
		Preconditions.checkArgument(StringUtils.isNoneBlank(deviceModelId), "设备编号不能为空！");
		List<ThingModelDef> modelList = modelDefMapper.modelListByDeviceModelId(deviceModelId);
		return modelList;
	}

	/**
	 * 通过模型编号获取模型
	 * 
	 * @param modelId
	 *            模型编号
	 *
	 * @return
	 */
	@Override
	public ThingModelDef modelViewById(String modelId, JwtAccount account) {
		Preconditions.checkArgument(StringUtils.isNoneBlank(modelId), "模型编号不能为空！");
		return modelDefMapper.modelViewById(modelId);
	}

	@Override
	public int thingModelCount(String deviceModel, JwtAccount account) {
		Preconditions.checkArgument(StringUtils.isNoneBlank(deviceModel), "设备型号不能为空！");
		return modelDefMapper.thingModelCount(deviceModel);
	}

	/**
	 * 从一个模板里面复制一个物模型.
	 * 
	 * @param modelTemplateId
	 *            模板ID，也就是可以作为物模型模板的物模型ID。
	 * @param deviceModelId
	 *            设备型号ID。
	 */
	@Transactional
	public void addModelFromTemplate(String modelTemplateId, String deviceModelId, JwtAccount account) {
		Preconditions.checkArgument(StringUtils.isNotBlank(modelTemplateId));
		Preconditions.checkArgument(StringUtils.isNotBlank(deviceModelId));

		List<ThingModelDef> existModelDefs = this.modelListByDeviceModelId(deviceModelId, null);
		if (existModelDefs != null && existModelDefs.size() > 0) {
			throw new BizLocaleException(CfgResultCode.THING_MODEL_EXIST);
		}

		List<ThingModelDef> sensoryModelDefs = modelDefMapper.modelViewByDeviceModelIdAndModelType(modelTemplateId,
				ThingDataTypeEnum.SENSORY.getValue());

		if (sensoryModelDefs == null || sensoryModelDefs.size() != 1) {
			throw new BizLocaleException(CfgResultCode.THING_MODEL_INVALID);
		}

		if (!ThingModelDef.checkTemplate(sensoryModelDefs.get(0))) {
			throw new BizLocaleException(CfgResultCode.THING_MODEL_TEMPLATE_INVALID);
		}

		List<ThingModelDef> templateThingModelDefs = modelDefMapper.modelListByDeviceModelId(modelTemplateId);
		if (templateThingModelDefs == null || templateThingModelDefs.size() == 0) {
			throw new BizLocaleException(CfgResultCode.THING_MODEL_TEMPLATE_MISS);
		}

		DeviceModel deviceModel = deviceModelService.deviceModelById(deviceModelId);
		if (deviceModel == null) {
			throw new BizLocaleException(CfgResultCode.DEVICE_MODEL_NOT_EXIST);
		}

		// -- 修改为新型号Id。
		for (ThingModelDef templateThingModelDef : templateThingModelDefs) {
			templateThingModelDef.setThingModelId(uniqueIdGenerator.generateModelId());
			templateThingModelDef.setDeviceModel(deviceModel.deviceModel);
			templateThingModelDef.setDeviceModelName(deviceModel.deviceModelName);
			templateThingModelDef.setDeviceModelId(deviceModel.deviceModelId);
			ThingModelDef.makeNonTemplate(templateThingModelDef);

			addModel(templateThingModelDef, account);
		}
	}

	/**
	 * 添加模型
	 * 
	 * @param model
	 *
	 * @return
	 */
	@Transactional
	@Override
	public String addModel(ThingModelDef model, JwtAccount account) {
		Preconditions.checkArgument(StringUtils.isNoneBlank(model.getDeviceModelId()), "设备编号不能为空！");
		Preconditions.checkArgument(StringUtils.isNoneBlank(model.getThingModelType()), "模型类型不能为空！");
		Preconditions.checkArgument(ThingDataTypeEnum.check(model.getThingModelType()), "模型类型不存在");
		Preconditions.checkArgument(ThingDataStrutTypeEnum.check(model.getStructType()), "结构类型不存在");
		Preconditions.checkArgument(account != null, "登录用户信息不存在。");

		List<ThingModelDef> existModelDefs = modelDefMapper
				.modelViewByDeviceModelIdAndModelType(model.getDeviceModelId(), model.getThingModelType());

		
		if (existModelDefs != null && existModelDefs.size() > 1
				&& !ThingDataTypeEnum.allowMultiRecord(model.getThingModelType())) {
			throw new BizLocaleException(CfgResultCode.THING_MODEL_INVALID);
		}
		
		// --  名称、对应的设备id、类别一致的话，存在。
		for (ThingModelDef def : existModelDefs) {
			if (StringUtils.equalsIgnoreCase(def.getName(), model.getName()) 
					&& StringUtils.equalsIgnoreCase(def.getDeviceModelId(), model.getDeviceModelId()) 
					&& StringUtils.equalsIgnoreCase(def.getThingModelType(), model.getThingModelType())) {
				throw new BizLocaleException(CfgResultCode.THING_MODEL_EXIST);
			}
		}

		model.setThingModelId(uniqueIdGenerator.generateModelId());
		model.setCreateTime(new Date());
		model.setModifyTime(new Date());
		model.setCreateUser(account.getUserId());
		model.setModifyUser(account.getUserId());
		if (StringUtils.isBlank(model.getName())) {
			model.setName(model.getThingModelType());
		}
		modelDefMapper.modelAdd(model);

		List<ThingModelField> fieldList = model.getFields();
		if (fieldList != null && fieldList.size() > 0) {
			fieldListAdd(null, model.getThingModelId(), fieldList);
		}
		return model.getThingModelId();
	}

	/**
	 * 修改模型
	 * 
	 * @param model
	 *
	 * @return
	 */
	@Transactional
	@Override
	public boolean updateModel(JwtAccount account, ThingModelDef model) {
		Preconditions.checkArgument(StringUtils.isNoneBlank(model.getThingModelId()), "模型编号不能为空！");
		Preconditions.checkArgument(StringUtils.isNoneBlank(model.getThingModelType()), "模型类型不能为空！");
		Preconditions.checkArgument(ThingDataTypeEnum.check(model.getThingModelType()), "模型类型不存在");
		Preconditions.checkArgument(account != null, "登录用户信息不存在。");

		model.setModifyTime(new Date());
		model.setModifyUser(account.getUserId());
		if (model.getFields() != null && model.getFields().size() > 0) {
			String sqlString = SqlGenerator.createTable(model);
			model.setSqls(sqlString);
		}
		int c = modelDefMapper.modelUpdate(model);

		// -- 删除全部的字段。
		delFieldList(model.getThingModelId());

		List<ThingModelField> fieldList = model.getFields();
		if (fieldList == null || fieldList.size() == 0) {
			return c > 0;
		}

		if (fieldList != null && fieldList.size() > 0) {
			fieldListAdd(null, model.getThingModelId(), fieldList);
		}
		return c > 0;
	}

	/**
	 * 修改模型型号名称
	 * 
	 * @param deviceModelName
	 *
	 * @return
	 */
	@Override
	public boolean updateModelName(JwtAccount account, String deviceModelName, String deviceModel) {
		Preconditions.checkArgument(StringUtils.isNotBlank(deviceModel));
		Preconditions.checkArgument(StringUtils.isNotBlank(deviceModelName));
		Preconditions.checkArgument(account != null);
		Map<String,Object> args = new HashMap<>();
		args.put("deviceModelName", deviceModelName);
		args.put("deviceModel", deviceModel);
		args.put("modifyUser", account.getUserId());
		return modelDefMapper.deviceModelNameUpdateByModel(args) > 0;
	}

	/**
	 * 删除设备模型
	 * 
	 * @param modelId
	 *
	 * @return
	 */
	@Transactional
	@Override
	public boolean deleteModel(JwtAccount account, String modelId) {
		Preconditions.checkArgument(StringUtils.isNoneBlank(modelId), "模型编号不能为空！");
		int c = modelDefMapper.modelDelete(modelId);
		delFieldList(modelId);
		return c > 0;
	}

	/**
	 * 删除一个模板.
	 * 
	 * @param templateId
	 *            模板ID即为设备模型标识.
	 * 
	 * @return
	 */
	public boolean deleteTemplate(JwtAccount account, String templateId) {
		Preconditions.checkArgument(StringUtils.isNotBlank(templateId));
		List<ThingModelDef> thingModelDefs = modelListByDeviceModelId(templateId, null);
		if (thingModelDefs == null || thingModelDefs.size() == 0) {
			throw new BizLocaleException(CfgResultCode.THING_MODEL_TEMPLATE_MISS);
		}

		return modelDefMapper.deleteTemplateByDeviceModelId(templateId) > 0;
	}

	/**
	 * 新增模型字段列表
	 * 
	 * @param fieldList
	 * @param modelId
	 *
	 * @return
	 */
	@Transactional
	@Override
	public boolean fieldListAdd(JwtAccount account, String modelId, List<ThingModelField> fieldList) {
		Preconditions.checkArgument(StringUtils.isNoneBlank(modelId), "模型编号不能为空！");
		Preconditions.checkArgument(fieldList != null && fieldList.size() > 0);
		for (ThingModelField field : fieldList) {
			field.setThingModelId(modelId);
			boolean success = addField(field);
			if (!success) {
				throw new BizLocaleException(CfgResultCode.DATABASE_ERROR);
			}
		}
		return true;
	}

	/**
	 * 新增模型字段
	 */
	protected boolean addField(ThingModelField field) {
		Preconditions.checkArgument(StringUtils.isNoneBlank(field.getName()), "模型字段名不能为空！");
		Preconditions.checkArgument(StringUtils.isNoneBlank(field.getCustomType()), "模型字段数据类型不能为空");
		int c = fieldMapper.add(field);
		return c > 0;
	}

	/**
	 * 删除模型字段列表
	 */
	protected boolean delFieldList(String modelId) {
		int c = fieldMapper.fieldListDelete(modelId);
		return c > 0;
	}

	@Override
	public List<String> strutType() {
		List<ThingDataStrutTypeEnum> list = Arrays.asList(ThingDataStrutTypeEnum.values());
		return list.stream().map(e -> {
			return e.getValue();
		}).collect(Collectors.toList());
	}

	@Override
	public List<String> storeTypes() {
		List<StoreTypeEnum> list = Lists.newArrayList(StoreTypeEnum.values());

		return list.stream().map(e -> {
			return e.getValue();
		}).collect(Collectors.toList());
	}

	/**
	 * 用户定义的结构体。
	 */
	@Override
	public List<UserDefStrut> userDefStrutType(JwtAccount account) {
		Preconditions.checkArgument(account != null, "用户信息没有。");

		List<ThingModelDef> userDefineModelDef = modelDefMapper.modelList(account.getUserId(),
				ThingDataTypeEnum.STRUT.getValue());

		return userDefineModelDef.stream().map(e -> {
			UserDefStrut userDefStrut = new UserDefStrut();
			userDefStrut.deviceModel = e.getDeviceModel();
			userDefStrut.name = e.getName();
			userDefStrut.thingModelId = e.getThingModelId();
			return userDefStrut;
		}).collect(Collectors.toList());
	}

	@Override
	public List<ThingModelField> listSensoryThingModelFieldByDeviceModel(String deviceModel) {
		Preconditions.checkArgument(StringUtils.isNotBlank(deviceModel));

		return fieldMapper.listSensoryThingModelFieldByDeviceModel(deviceModel);
	}

	@Override
	public List<String> getUsableStoreType(String dataType) {
		Preconditions.checkArgument(StringUtils.isNotBlank(dataType));
		return DataTypeStoreTypeRelation.getStoreType(dataType);
	}

	@Override
	public PageInfo<ThingModelDef> templateModelView(JwtAccount account, String deviceModel, String deviceModelName, int currentPage, int pageSize) {

		Map<String, Object> paras = Maps.newHashMap();

		paras.put("userId", StringUtils.isBlank(DevUser.isDeveloper(account)) ? null : account.getUserId());
		paras.put("deviceModel", StringUtils.isBlank(deviceModel) ? null : deviceModel);
		paras.put("deviceModelName", StringUtils.isBlank(deviceModelName) ? null : deviceModelName);

		PageHelper.startPage(currentPage, pageSize);
		List<ThingModelDef> docs = modelDefMapper.templateModelView(paras);
		PageInfo<ThingModelDef> pageInfo = new PageInfo<>(docs);
		return pageInfo;
	}

	/**
	 * 一个设备型号的物模型作为模板。
	 * 
	 * @param deviceModelId
	 *            设备型号。
	 */
	public void makeTemplate(String deviceModelId, JwtAccount account) {

		Preconditions.checkArgument(StringUtils.isNoneBlank(deviceModelId));

		List<ThingModelDef> sensoryModelDefs = modelDefMapper.modelViewByDeviceModelIdAndModelType(deviceModelId,
				ThingDataTypeEnum.SENSORY.getValue());

		if (sensoryModelDefs.size() == 0) {
			throw new BizLocaleException(CfgResultCode.THING_MODEL_TEMPLATE_CREATE_SENSORY_NOT_EXIST);
		}

		if (ThingModelDef.checkTemplate(sensoryModelDefs.get(0))) {
			throw new BizLocaleException(CfgResultCode.THING_MODEL_TEMPLATE_CREATE_EXIST);
		}

		ThingModelDef.makeTemplate(sensoryModelDefs.get(0));
		this.updateModel(account, sensoryModelDefs.get(0));

	}


	/**
     * zookeeper 设备协议发布生成node
     * */
	private void updateNodeData() {
        String currentTime = String.valueOf(System.currentTimeMillis());
        try {
            if(!cfgZkClient.isConnect()){
                throw new BizLocaleException(CfgResultCode.ZOOKEEPER_ERROR);
            }

            if(cfgZkClient.exists(PATH) == null){
                cfgZkClient.createNode(PATH, currentTime.getBytes());
            }else {
                cfgZkClient.setData(PATH,currentTime.getBytes());
            }
        } catch (KeeperException | InterruptedException exception) {
            logger.error("zookeeper error: " + exception);
        }
    }

}
