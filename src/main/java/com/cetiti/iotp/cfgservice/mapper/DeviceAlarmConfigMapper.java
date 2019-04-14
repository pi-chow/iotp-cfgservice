package com.cetiti.iotp.cfgservice.mapper;

import com.cetiti.iotp.cfgservice.domain.entity.DeviceAlarmConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 设备告警配置数据接口
 * @author zhouliyu
 * @since 2019-03-29 10:33:52
 */
@Repository
public interface DeviceAlarmConfigMapper {

    /**
     * 保存告警配置信息
     * @param deviceAlarmConfig 告警配置信息
     * */
    int insertSelective(DeviceAlarmConfig deviceAlarmConfig);

    /**
     * 更新告警配置信息
     * @param deviceAlarmConfig 告警配置信息
     * */
    int updateSelective(DeviceAlarmConfig deviceAlarmConfig);

    /**
     * 删除告警配置信息
     * @param alarmId 告警配置ID
     * */
    int deleteByPrimaryKey(@Param("alarmId") String alarmId);

    /**
     * 获取告警配置列表
     * @param params 搜索条件
     * */
    List<DeviceAlarmConfig> listDeviceAlarmConfig(Map<String, Object> params);

    /**
     * 根据ID获取告警配置
     * @param alarmId 告警配置ID
     * */
    DeviceAlarmConfig getDeviceAlarmConfig(@Param("alarmId") String alarmId);

}
