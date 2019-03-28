package com.cetiti.iotp.cfgservice;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.iotp.cfgservice.domain.DeviceAlarmConfig;
import com.cetiti.iotp.cfgservice.service.AlarmService;
import com.cetiti.iotp.itf.assetservice.DeviceModelService;
import com.cetiti.iotp.itf.coreservice.CtrlService;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author zhouliyu
 * @since 2019-03-22 12:57:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AlarmServiceTest {

    @Autowired
    private AlarmService alarmService;
    @Reference
    private CtrlService ctrlService;


    @Test
    public void addAlarmConfig(){
        DeviceAlarmConfig alarmConfig = new DeviceAlarmConfig();
        alarmConfig.setDeviceModel("ThingSensoryMsgDemo");
        alarmConfig.setField("attribute1");
        JwtAccount account = new JwtAccount();
        account.setUserId("111");
        alarmConfig.setDescription("1111");
        alarmConfig.setAlarmType("offline");
        alarmService.updateAlarmConfig(account, alarmConfig);
    }

    @Test
    public void testOffLine(){
                try {
                    ctrlService.syncSendDeviceDataStream("1_rtu_1", "rtu", "test");
                }catch (UndeclaredThrowableException e){
                        e.printStackTrace();
                }

    }

}
