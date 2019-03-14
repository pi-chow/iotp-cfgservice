package com.cetiti.iotpcfgservice.service.dubbo;

import com.cetiti.iotp.itf.cfgservice.AlarmService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class AlarmServiceImplWrap implements AlarmService {
    @Autowired
    private com.cetiti.iotpcfgservice.service.AlarmService alarmService;

    @Override
    public int alarmCountByModel(String deviceModel) {
        return alarmService.alarmCountByModel(deviceModel);
    }
}
