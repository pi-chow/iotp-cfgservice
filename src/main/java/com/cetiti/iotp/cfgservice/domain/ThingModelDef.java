package com.cetiti.iotp.cfgservice.domain;


import com.cetiti.ddapv2.iotplatform.common.StoreTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.ThingDataStrutTypeEnum;
import com.cetiti.iotp.cfgservice.domain.BaseDomain;
import com.cetiti.iotp.itf.cfgservice.vo.ThingModelField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 消息体
 * @author zhouliyu
 */
@ApiModel(value = "ThingModelDef", description = "物模型定义")
public class ThingModelDef extends BaseDomain implements Serializable {
	private static final long serialVersionUID = 7677886815498330090L;

	// 自增id
	@JsonIgnore
	private int id;
	// 模型名称
	private String name;
	// 设备类型ID
	@ApiModelProperty(value = "设备型号ID")
	private String deviceModelId;
	// 设备类型名
	@ApiModelProperty(value = "设备型号")
	private String deviceModel;

	@ApiModelProperty(value = "设备型号名称")
	private String deviceModelName = null;

	// 物模型类型
	@ApiModelProperty(value = "物模型类型:sensor,prop,status,event")
	private String thingModelType;
	// 物模型id
	@ApiModelProperty(value = "物模型id")
	private String thingModelId;
	// 数据流类型
	@ApiModelProperty(value = "数据流类型:json,xml,byte")
	private String structType;

	// 是否可以作为模板。
	@ApiModelProperty(value = "是否可以作为模板:0 - 不可以；1 - 可以")
	private int template;

	// 消息简介
	private String description;

	// 建表语句
	private String sqls;

	// 消息字段
	private List<ThingModelField> fields;

	/**
	 * 存储类别字段。
	 */
	private String storeTypes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public String getDeviceModelName() {
		return deviceModelName;
	}

	public void setDeviceModelName(String deviceModelName) {
		this.deviceModelName = deviceModelName;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getThingModelType() {
		return thingModelType;
	}

	public void setThingModelType(String thingModelType) {
		this.thingModelType = thingModelType;
	}

	public String getThingModelId() {
		return thingModelId;
	}

	public void setThingModelId(String thingModelId) {
		this.thingModelId = thingModelId;
	}

	public String getStructType() {
		return structType;
	}

	public void setStructType(String structType) {
		Preconditions.checkArgument(ThingDataStrutTypeEnum.check(structType));
		this.structType = structType;
	}

	public int getTemplate() {
		return template;
	}

	public void setTemplate(int template) {
		this.template = template;
	}

	public static boolean checkTemplate(ThingModelDef thingModelDef) {
		return thingModelDef.getTemplate() == 1;
	}

	public static void makeTemplate(ThingModelDef thingModelDef) {
		thingModelDef.setTemplate(1);
	}

	public static void makeNonTemplate(ThingModelDef thingModelDef) {
		thingModelDef.setTemplate(0);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSqls() {
		return sqls;
	}

	public void setSqls(String sqls) {
		this.sqls = sqls;
	}

	public List<ThingModelField> getFields() {
		return fields;
	}

	public void setFields(List<ThingModelField> fields) {
		this.fields = fields;
	}

	public String getStoreTypes() {
		return storeTypes;
	}

	public void setStoreTypes(String storeTypes) {
		Preconditions.checkArgument(StringUtils.isNotBlank(storeTypes));
		String[] storeTypeArray = StringUtils.split(storeTypes, ",");
		for (String str : storeTypeArray) {
			Preconditions.checkArgument(StoreTypeEnum.check(str));
		}

		this.storeTypes = storeTypes;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
