package com.cetiti.iotpcfgservice.service;


import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotpcfgservice.domain.DeviceAlarmConfig;
import com.cetiti.iotpcfgservice.domain.ExceptionAlarm;

import java.util.List;
import java.util.Map;

public interface AlarmService {

    String addAlarmConfig(JwtAccount account, DeviceAlarmConfig alarm);

    DeviceAlarmConfig getAlarmConfig(String alarmId);

    boolean updateAlarmConfig(JwtAccount account, DeviceAlarmConfig alarm);

    boolean deleteAlarmConfig(String alarmId);

    List<DeviceAlarmConfig> getAlarmConfig(JwtAccount account, Map<String, Object> params);
    List<DeviceAlarmConfig> getAlarmConfig(Map<String, Object> params);

    List<ExceptionAlarm> deviceAlarmList(JwtAccount account, Map<String, Object> params);

    Map<String, Object> getAlarmCfgInfo();

    int alarmCountByModel(String deviceModel);

}
