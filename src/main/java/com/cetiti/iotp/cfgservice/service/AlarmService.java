package com.cetiti.iotp.cfgservice.service;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotp.cfgservice.domain.entity.DeviceAlarmConfig;
import com.cetiti.iotp.cfgservice.domain.entity.DeviceAlarmException;
import com.cetiti.iotp.cfgservice.domain.vo.AlarmType;
import com.cetiti.iotp.cfgservice.domain.vo.DeviceAlarmConfigVo;

import java.util.List;
import java.util.Map;

/**
 * 设备告警服务
 * @author zhouliyu
 * @since 2019-03-29 15:40:10
 */
public interface AlarmService extends com.cetiti.iotp.itf.cfgservice.AlarmService {

    /**
     * 添加告警配置
     * @param account 用户
     * @param deviceAlarmConfig 告警配置
     * */
    boolean addDeviceAlarmConfig(JwtAccount account, DeviceAlarmConfig deviceAlarmConfig);

    /**
     * 获取告警配置
     * @param alarmId 告警配置ID
     * */
    DeviceAlarmConfig getDeviceAlarmConfig(String alarmId);

    /**
     * 获取告警配置列表
     * @param account 用户
     * @param params 查询条件
     * */
    List<DeviceAlarmConfig> listDeviceAlarmConfig(JwtAccount account, Map<String, Object> params);

    /**
     * 更新告警配置
     * @param account 用户
     * @param deviceAlarmConfig 告警配置
     * */
    boolean updateDeviceAlarmConfig(JwtAccount account, DeviceAlarmConfig deviceAlarmConfig);

    /**
     * 删除告警配置
     * @param alarmId 告警配置ID
     * */
    boolean deleteDeviceAlarmConfig(String alarmId);

    /**
     * 获取设备异常信息列表
     * @param account 用户
     * @param params 查询条件
     * */
    List<DeviceAlarmException> listDeviceAlarmException(JwtAccount account, Map<String, Object> params);

    /**
     * 设备异常信息统计
     * @param deviceModel 设备型号
     * */
    int CountAlarmException(String deviceModel);

    /**
     * 获取告警类型
     * */
    List<AlarmType> listAlarmType();

    /**
     * 获取设备告警配置列表-》上传报警条件
     * @param alarmType 告警类型
     * */
    List<DeviceAlarmConfigVo> uploadListAlarmConfig(String alarmType);

}
