package com.cetiti.iotp.cfgservice.service.impl;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.ddapv2.iotplatform.common.utils.GenerationSequenceUtil;
import com.cetiti.iotp.cfgservice.common.enums.RoleEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgErrorCodeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgServiceException;
import com.cetiti.iotp.cfgservice.common.zookeeper.CfgZkClient;
import com.cetiti.iotp.cfgservice.domain.entity.DeviceAlarmConfig;
import com.cetiti.iotp.cfgservice.domain.entity.DeviceAlarmException;
import com.cetiti.iotp.cfgservice.domain.vo.AlarmType;
import com.cetiti.iotp.cfgservice.domain.vo.DeviceAlarmConfigVo;
import com.cetiti.iotp.cfgservice.common.enums.AlarmTypeEnum;
import com.cetiti.iotp.cfgservice.mapper.DeviceAlarmConfigMapper;
import com.cetiti.iotp.cfgservice.mapper.DeviceAlarmExceptionMapper;
import com.cetiti.iotp.cfgservice.service.AlarmService;
import com.cetiti.iotp.itf.coreservice.OfflineCheckService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 设备告警服务
 * @author zhouliyu
 * @since 2019-03-29 16:07:24
 */
@Slf4j
@org.apache.dubbo.config.annotation.Service(interfaceClass = com.cetiti.iotp.itf.cfgservice.AlarmService.class,  timeout = 6000)
@Service
public class AlarmServiceImpl implements AlarmService {

    @Value("${iotp.cfg.zkClient.watcher.paths}")
    private String ZK_CLIENT_WATCHER_PATHS;

    @Autowired
    private CfgZkClient cfgZkClient;

    @Autowired
    private DeviceAlarmConfigMapper alarmConfigMapper;

    @Autowired
    private DeviceAlarmExceptionMapper alarmExceptionMapper;

    @Reference
    private OfflineCheckService offlineCheckService;

    @Override
    public boolean addDeviceAlarmConfig(JwtAccount account, DeviceAlarmConfig deviceAlarmConfig) {
        String alarmId = GenerationSequenceUtil.uuid();
        deviceAlarmConfig.setAlarmId(alarmId);
        deviceAlarmConfig.setCreateTime(new Date());
        deviceAlarmConfig.setCreateUser(account.getUserId());
        deviceAlarmConfig.setModifyTime(new Date());
        deviceAlarmConfig.setModifyUser(account.getUserId());
        insertOffline(deviceAlarmConfig.getAlarmType(),deviceAlarmConfig.getThreshold(), deviceAlarmConfig.getDeviceModel());
        boolean success = alarmConfigMapper.insertSelective(deviceAlarmConfig) >= 1;
        updateNodeData();
        return success;
    }

    @Override
    public DeviceAlarmConfig getDeviceAlarmConfig(String alarmId) {
        return alarmConfigMapper.getDeviceAlarmConfig(alarmId);
    }

    @Override
    public List<DeviceAlarmConfig> listDeviceAlarmConfig(JwtAccount account, Map<String, Object> params) {
        if(StringUtils.equalsIgnoreCase(account.getRoles(), RoleEnum.ROLE_DEV.getValue())){
            params.put("createUser", account.getUserId());
        }
        return alarmConfigMapper.listDeviceAlarmConfig(params);
    }

    @Override
    public boolean updateDeviceAlarmConfig(JwtAccount account, DeviceAlarmConfig deviceAlarmConfig) {
        deviceAlarmConfig.setModifyTime(new Date());
        deviceAlarmConfig.setModifyUser(account.getUserId());
        if(deviceAlarmConfig.getAlarmType().equals("offline")){
            deviceAlarmConfig.setAlarmLevel(null);
            deviceAlarmConfig.setDescription(null);
        }
        insertOffline(deviceAlarmConfig.getAlarmType(),deviceAlarmConfig.getThreshold(), deviceAlarmConfig.getDeviceModel());
        boolean success = alarmConfigMapper.updateSelective(deviceAlarmConfig) >= 1;
        updateNodeData();
        return success;
    }

    @Override
    public boolean deleteDeviceAlarmConfig(String alarmId) {
        DeviceAlarmConfig alarmConfig = alarmConfigMapper.getDeviceAlarmConfig(alarmId);
        if(StringUtils.equals(alarmConfig.getAlarmType(), AlarmTypeEnum.OFFLINE.getValue())){
            try {
                offlineCheckService.removeNeedCheckModel(alarmConfig.getDeviceModel());
            }catch (Exception e){
                throw new CfgServiceException(CfgErrorCodeEnum.ALARM_EVENT_OFFLINE_REDIS);
            }
        }

        boolean success = alarmConfigMapper.deleteByPrimaryKey(alarmId) >= 1;
        updateNodeData();
        return success;
    }

    @Override
    public List<DeviceAlarmException> listDeviceAlarmException(JwtAccount account, Map<String, Object> params) {
        if(StringUtils.equalsIgnoreCase(account.getRoles(), RoleEnum.ROLE_DEV.getValue())){
            params.put("createUser", account.getUserId());
        }
        return alarmExceptionMapper.listDeviceAlarmException(params);
    }

    @Override
    public int CountAlarmException(String deviceModel) {
        return alarmExceptionMapper.selectCount(deviceModel);
    }

    @Override
    public List<AlarmType> listAlarmType() {
        List<AlarmTypeEnum> listAlarmTypeEnum = Lists.newArrayList(AlarmTypeEnum.values());
        List<AlarmType> listAlarmType = new ArrayList<>();
        listAlarmTypeEnum.forEach(e -> listAlarmType.add(e.getAlarmType(e)));
        return listAlarmType;
    }

    @Override
    public List<DeviceAlarmConfigVo> uploadListAlarmConfig(String alarmType) {
        Map<String, Object> params = new HashMap<>();
        params.put("alarmType", alarmType);
        List<DeviceAlarmConfig> listDeviceAlarmConfig = alarmConfigMapper.listDeviceAlarmConfig(params);
        List<DeviceAlarmConfigVo> listAlarmConfigVo = new ArrayList<>();
        listDeviceAlarmConfig.forEach(e->listAlarmConfigVo.add(buildDeviceAlarmConfigVo(e)));
        return listAlarmConfigVo;
    }

    /**
     * 离线告警
     * @param alarmType 告警类型
     * @param threshold 阈值
     * @param deviceModel 设备型号
     * */
    private void insertOffline(String alarmType, String threshold, String deviceModel){
        if(StringUtils.equals(alarmType, AlarmTypeEnum.OFFLINE.getValue())){
            int offlineTimeInterval = Integer.parseInt(threshold);
            if(offlineTimeInterval <= 0){
                throw new CfgServiceException(CfgErrorCodeEnum.ALARM_EVENT_OFFLINE_ZERO);
            }
            try {
                offlineCheckService.addNeedCheckModel(deviceModel, offlineTimeInterval);
            }catch (Exception e){
                throw new CfgServiceException(CfgErrorCodeEnum.ALARM_EVENT_OFFLINE_REDIS);
            }
        }
    }

    /**
     * 设备告警配置信息类型转换
     * @param deviceAlarmConfig 数据库告警配置信息
     * */
    private DeviceAlarmConfigVo buildDeviceAlarmConfigVo(DeviceAlarmConfig deviceAlarmConfig){
        String conditions = deviceAlarmConfig.getField()
                                .concat(deviceAlarmConfig.getRelation())
                                .concat(deviceAlarmConfig.getThreshold());
        DeviceAlarmConfigVo alarmConfigVo = new DeviceAlarmConfigVo();
        alarmConfigVo.setDeviceModel(deviceAlarmConfig.getDeviceModel());
        alarmConfigVo.setConditions(conditions);
        alarmConfigVo.setField(deviceAlarmConfig.getField());
        alarmConfigVo.setDescription(deviceAlarmConfig.getDescription());
        alarmConfigVo.setCreateUser(deviceAlarmConfig.getCreateUser());
        return alarmConfigVo;
    }

    /**
     * zookeeper 更新/iotp/cfg/alarm/config/time节点
     * */
    private void updateNodeData() {
        String currentTime = String.valueOf(System.currentTimeMillis());
        try {
            cfgZkClient.setData(ZK_CLIENT_WATCHER_PATHS.split(",")[1],currentTime.getBytes());
        } catch (KeeperException | InterruptedException exception) {
            log.error("zookeeper error: alarm->" + exception);
            throw new CfgServiceException(CfgErrorCodeEnum.ZOOKEEPER_UPDATE_DATA_ERROR);
        }
    }
}
