package com.cetiti.iotpcfgservice.service.impl;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotpcfgservice.common.access.DevUser;
import com.cetiti.iotpcfgservice.common.id.UniqueIdGenerator;
import com.cetiti.iotpcfgservice.domain.DeviceAlarmConfig;
import com.cetiti.iotpcfgservice.domain.ExceptionAlarm;
import com.cetiti.iotpcfgservice.mapper.AlarmMapper;
import com.cetiti.iotpcfgservice.mapper.DeviceAlarmConfigMapper;
import com.cetiti.iotpcfgservice.service.AlarmService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 告警服务实现。
 *
 * @author yangshutian
 */
@org.apache.dubbo.config.annotation.Service(interfaceClass = AlarmService.class)
@Service
public class AlarmServiceImpl implements AlarmService {

    private Logger logger = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    private UniqueIdGenerator uniqueIdGenerator;

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private DeviceAlarmConfigMapper deviceAlarmConfigMapper;


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
     * @param record 告警配置类
     * @return
     */
    @Override
    public String addAlarmConfig(JwtAccount account, DeviceAlarmConfig record) {
        setLastModified();
        String alarmId = uniqueIdGenerator.generateAlarmId();
        record.setAlarmId(alarmId);
        record.setCreateTime(new Date());
        record.setCreateUser(account.getUserId());
        deviceAlarmConfigMapper.insert(record);
        return alarmId;
    }

    /**
     * 修改告警配置
     * @param account 账户
     * @param record 告警配置类
     * @return
     */
    @Override
    public boolean updateAlarmConfig(JwtAccount account, DeviceAlarmConfig record) {
        setLastModified();
        record.setModifyTime(new Date());
        record.setModifyUser(account.getUserId());
        return deviceAlarmConfigMapper.updateByPrimaryKeySelective(record) == 1;
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

    @Override
    public int alarmCountByModel(String deviceModel) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(deviceModel), "设备型号不能为空！");
        Map<String, Object> map = Maps.newHashMap();
        map.put("deviceModel", deviceModel);
        return alarmMapper.alarmCountByModel(map);
    }

    /**
     * 获取告警列表
     *
     * @return
     */
    @Override
    public List<ExceptionAlarm> deviceAlarmList(JwtAccount account, Map<String, Object> params) {
        return alarmMapper.deviceAlarmList(params);
    }


    @Override
    public DeviceAlarmConfig getAlarmConfig(String alarmId) {

        return deviceAlarmConfigMapper.getAlarmConfig(alarmId);
    }

}