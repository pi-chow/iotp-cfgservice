package com.cetiti.iotp.cfgservice.mapper;

import com.cetiti.iotp.cfgservice.domain.entity.DeviceAlarmException;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 设备告警异常信息
 * @author zhouliyu
 * @since 2019-03-29 10:33:52
 */
@Repository
public interface DeviceAlarmExceptionMapper {

    /**
     * 获取设备告警异常信息列表
     * @param params 搜索条件
     * */
    List<DeviceAlarmException> listDeviceAlarmException(Map<String, Object> params);

    /**
     * 设备告警异常信息统计
     * @param deviceModel 设备型号
     * */
    int selectCount(@Param("deviceModel") String deviceModel);
}
