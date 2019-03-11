package com.cetiti.iotpcfgservice.mapper;


import com.cetiti.iotpcfgservice.domain.DeviceAlarmConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DeviceAlarmConfigMapper {
    int deleteByPrimaryKey(String alarmId);

    int insert(DeviceAlarmConfig record);

    int insertSelective(DeviceAlarmConfig record);

    DeviceAlarmConfig selectByPrimaryKey(Long id);

    DeviceAlarmConfig getAlarmConfig(String alarmId);

    int updateByPrimaryKeySelective(DeviceAlarmConfig record);

    int updateByPrimaryKey(DeviceAlarmConfig record);

    List<DeviceAlarmConfig> searchDeviceAlarmConfig(Map<String, Object> params);
}