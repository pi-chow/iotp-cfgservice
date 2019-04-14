package com.cetiti.iotp.cfgservice.common.utils;

import com.cetiti.iotp.cfgservice.common.result.CfgErrorCodeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgServiceException;
import com.cetiti.iotp.itf.assetservice.DeviceModelService;
import com.cetiti.iotp.itf.assetservice.vo.DeviceModel;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * 设备型号校验
 * @author zhouliyu
 * @since 2019-04-11 09:44:10
 */
@Component
public class DeviceModelValidateManger {

    @Reference
    private DeviceModelService deviceModelService;

    /**
     * 设备型号是否存在
     * @param deviceModel 设备型号
     * */
    public void validateDeviceModel(String deviceModel){
        Preconditions.checkArgument(StringUtils.isNoneBlank(deviceModel), "设备型号不为空");
        if(deviceModelService.viewDetail(deviceModel) == null){
            throw new CfgServiceException(CfgErrorCodeEnum.DEVICE_MODEL_NOT_EXIST);
        }
    }

    /**
     * 设备型号
     * @param deviceModel 设备型号
     * */
    public DeviceModel getDeviceModel(String deviceModel){
        Preconditions.checkArgument(StringUtils.isNoneBlank(deviceModel), "设备型号不为空");
        DeviceModel deviceModelView = deviceModelService.viewDetail(deviceModel);
        if(deviceModelView == null){
            throw new CfgServiceException(CfgErrorCodeEnum.DEVICE_MODEL_NOT_EXIST);
    }
        return deviceModelView;
    }
}
