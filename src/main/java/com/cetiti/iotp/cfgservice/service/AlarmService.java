package com.cetiti.iotp.cfgservice.service;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotp.cfgservice.domain.AlarmType;
import com.cetiti.iotp.cfgservice.domain.DeviceAlarmConfig;
import com.cetiti.iotp.cfgservice.domain.ExceptionAlarm;
import com.cetiti.iotp.itf.cfgservice.vo.ThingModelField;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public interface AlarmService extends com.cetiti.iotp.itf.cfgservice.AlarmService {

	/**
	 * 添加告警配置
	 * 
	 * @param account
	 *            用户
	 * @param deviceAlarmConfig
	 *            配置信息
	 * @return
	 */
	String addAlarmConfig(JwtAccount account, DeviceAlarmConfig deviceAlarmConfig);

	DeviceAlarmConfig getAlarmConfig(String alarmId);

	boolean updateAlarmConfig(JwtAccount account, DeviceAlarmConfig alarm);

	boolean deleteAlarmConfig(String alarmId);

	List<DeviceAlarmConfig> getAlarmConfig(JwtAccount account, Map<String, Object> params);

	List<ExceptionAlarm> deviceAlarmList(JwtAccount account, Map<String, Object> params);

	Map<String, Object> getAlarmCfgInfo();

	int alarmCountByModel(String deviceModel);

	/**
	 * 获取告警类型：离线
	 * */
	ThingModelField getDeviceModelStatus();

	/**
	 *获取告警类型
	 * */
	List<AlarmType> alarmTypeList();

}
