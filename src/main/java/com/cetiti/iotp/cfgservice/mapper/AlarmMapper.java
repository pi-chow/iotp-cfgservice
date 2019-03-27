package com.cetiti.iotp.cfgservice.mapper;


import com.cetiti.iotp.cfgservice.domain.ExceptionAlarm;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 告警信息。
 *
 * @author zhouliyu
 */
@Repository
public interface AlarmMapper {

    List<ExceptionAlarm> deviceAlarmList(Map<String, Object> params);

    int alarmCountByModel(Map<String, Object> params);
}