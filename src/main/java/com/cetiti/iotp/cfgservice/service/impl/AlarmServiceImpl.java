package com.cetiti.iotp.cfgservice.service.impl;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.ddapv2.iotplatform.common.utils.GenerationSequenceUtil;
import com.cetiti.iotp.cfgservice.common.result.CfgResultCode;
import com.cetiti.iotp.cfgservice.domain.AlarmType;
import com.cetiti.iotp.cfgservice.domain.DeviceAlarmConfig;
import com.cetiti.iotp.cfgservice.domain.ExceptionAlarm;
import com.cetiti.iotp.cfgservice.enums.AlarmTypeEnum;
import com.cetiti.iotp.cfgservice.mapper.AlarmMapper;
import com.cetiti.iotp.cfgservice.common.access.DevUser;
import com.cetiti.iotp.cfgservice.mapper.DeviceAlarmConfigMapper;
import com.cetiti.iotp.cfgservice.service.AlarmService;
import com.cetiti.iotp.itf.cfgservice.vo.ThingModelField;
import com.cetiti.iotp.itf.coreservice.OfflineCheckService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 告警服务实现。
 *
 * @author yangshutian
 */
@org.apache.dubbo.config.annotation.Service(interfaceClass = com.cetiti.iotp.itf.cfgservice.AlarmService.class, timeout = 6000)
@Service
public class AlarmServiceImpl implements AlarmService {

    private Logger logger = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private DeviceAlarmConfigMapper deviceAlarmConfigMapper;

    @Reference
    private OfflineCheckService offlineCheckService;


    private static final String EVENT_NAME = "offline";

    private static File file = new File("exception-guard.xml");

    private static Long LastModified = System.currentTimeMillis();

    public static void setLastModified() {
        LastModified = System.currentTimeMillis();
    }

    public static Long getLastModified() {
        return LastModified;
    }

    /**
     * 新增告警配置
     * @param account 账户
     * @param alarmConfig 告警配置类
     * @return
     */
    @Override
    public String addAlarmConfig(JwtAccount account, DeviceAlarmConfig alarmConfig) {
        setLastModified();
        String alarmId = GenerationSequenceUtil.uuid();
        alarmConfig.setAlarmId(alarmId);
        alarmConfig.setCreateTime(new Date());
        alarmConfig.setCreateUser(account.getUserId());
        alarmConfig.setModifyTime(new Date());
        alarmConfig.setModifyUser(account.getUserId());
        insertOffline(alarmConfig.getAlarmType(), alarmConfig.getThreshold(), alarmConfig.getDeviceModel());
        deviceAlarmConfigMapper.insert(alarmConfig);
        return alarmId;
    }

    /**
     * 修改告警配置
     * @param account 账户
     * @param alarmConfig 告警配置类
     * @return
     */
    @Override
    public boolean updateAlarmConfig(JwtAccount account, DeviceAlarmConfig alarmConfig) {
        setLastModified();
        alarmConfig.setModifyTime(new Date());
        alarmConfig.setModifyUser(account.getUserId());
        if(alarmConfig.getAlarmType().equals("offline")){
            alarmConfig.setAlarmLevel(null);
            alarmConfig.setDescription(null);
        }
        insertOffline(alarmConfig.getAlarmType(), alarmConfig.getThreshold(), alarmConfig.getDeviceModel());
        return deviceAlarmConfigMapper.updateByPrimaryKeySelective(alarmConfig) == 1;
    }

    /**
     * 删除告警配置
     *
     * @param alarmId 告警配置id
     * @return
     */
    @Override
    public boolean deleteAlarmConfig(String alarmId) {
        setLastModified();
        DeviceAlarmConfig alarmConfig = getAlarmConfig(alarmId);
        if(alarmConfig.getAlarmType().equals(EVENT_NAME)){
            try {
                offlineCheckService.removeNeedCheckModel(alarmConfig.getDeviceModel());
            }catch (Exception e){
                throw new  BizLocaleException(CfgResultCode.ALARM_EVENT_OFFLINE_REDIS);
            }
        }
        return deviceAlarmConfigMapper.deleteByPrimaryKey(alarmId) == 1;
    }

    /**
     * 获取告警配置列表
     *
     * @return
     */
    @Override
    public List<DeviceAlarmConfig> getAlarmConfig(JwtAccount account, Map<String, Object> params) {

        String createUser = DevUser.isDeveloper(account);

        if(StringUtils.isNoneBlank(createUser)){
            params.put("createUser", createUser);
        }

        return deviceAlarmConfigMapper.searchDeviceAlarmConfig(params);
    }

    /**
     * 拉取设备配置文件
     *
     * @return
     */
    @Override
    public Map<String, Object> getAlarmCfgInfo() {
        if (!file.exists()) {
            return null;
        }
        Map<String, Object> map = Maps.newHashMap();
        try {
            map.put("content", FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            logger.error("Read file {} fail", file.getName(), e);
            return null;
        }
        map.put("lastModified", file.lastModified());
        return map;
    }

    /**
     * 设备异常信息统计
     * @param deviceModel
     * @return
     * */
    @Override
    public int alarmCountByModel(String deviceModel) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(deviceModel), "设备型号不能为空！");
        Map<String, Object> map = Maps.newHashMap();
        map.put("deviceModel", deviceModel);
        return alarmMapper.alarmCountByModel(map);
    }

    /**
     * 获取设备型号状态
     * */
    @Override
    public ThingModelField getDeviceModelStatus() {
        ThingModelField thingModelFieldOffLine = new ThingModelField();
        thingModelFieldOffLine.setCharset("UTF-8");
        thingModelFieldOffLine.setName("offline");
        thingModelFieldOffLine.setLabel("离线");
        thingModelFieldOffLine.setUnit("s");
        return thingModelFieldOffLine;
    }

    /**
     *获取告警类型
     * */
    @Override
    public List<AlarmType> alarmTypeList() {
        List<AlarmTypeEnum> alarmTypeList = Lists.newArrayList(AlarmTypeEnum.values());
        List<AlarmType> alarmTypes = new ArrayList<>();
        for (AlarmTypeEnum alarmTypeEnum : alarmTypeList){
            AlarmType alarmType = new AlarmType();
            alarmType.setValue(alarmTypeEnum.getValue());
            alarmType.setLabel(alarmTypeEnum.getLabel());
            alarmTypes.add(alarmType);
        }
        return alarmTypes;
    }

    /**
     * 获取设备告警列表
     * @return
     */
    @Override
    public List<ExceptionAlarm> deviceAlarmList(JwtAccount account, Map<String, Object> params) {
        return alarmMapper.deviceAlarmList(params);
    }


    /**
     * 获取告警配置信息
     * @param alarmId
     * @re
     * */
    @Override
    public DeviceAlarmConfig getAlarmConfig(String alarmId) {

        return deviceAlarmConfigMapper.getAlarmConfig(alarmId);
    }

    /**
     * 离线告警
     * */
    private void insertOffline(String alarmType, String threshold, String deviceModel){
        if(alarmType.equals(EVENT_NAME)){
            int offlineTimeInterval = Integer.parseInt(threshold);
            if(offlineTimeInterval <= 0){
                throw new  BizLocaleException(CfgResultCode.ALARM_EVENT_OFFLINE_ZERO);
            }
            try {
                offlineCheckService.addNeedCheckModel(deviceModel, offlineTimeInterval);
            }catch (Exception e){
                throw new  BizLocaleException(CfgResultCode.ALARM_EVENT_OFFLINE_REDIS);
            }
        }
    }

}
