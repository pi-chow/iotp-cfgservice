package com.cetiti.iotpcfgservice.service;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotpcfgservice.domain.DeviceAlarmConfig;
import com.cetiti.iotpcfgservice.domain.ExceptionAlarm;

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

}
