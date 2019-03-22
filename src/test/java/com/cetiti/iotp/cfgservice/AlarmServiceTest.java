package com.cetiti.iotp.cfgservice;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotp.cfgservice.domain.DeviceAlarmConfig;
import com.cetiti.iotp.cfgservice.service.AlarmService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhouliyu
 * @since 2019-03-22 12:57:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlarmServiceTest {

    @Autowired
    private AlarmService alarmService;
    @Test
    public void addAlarmConfig(){
        DeviceAlarmConfig alarmConfig = new DeviceAlarmConfig();
        alarmConfig.setDeviceModel("ThingSensoryMsgDemo");
        alarmConfig.setField("attribute1");
        alarmConfig.setConditions("attribute1>300");
        JwtAccount account = new JwtAccount();
        account.setUserId("111");
        alarmService.addAlarmConfig(account, alarmConfig);
    }

}
