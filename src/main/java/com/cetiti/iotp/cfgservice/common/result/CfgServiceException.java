package com.cetiti.iotp.cfgservice.common.result;

import com.cetiti.ddapv2.iotplatform.common.BaseResultCode;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import xiaojian.toolkit.base.ResultCode;

/**
 * @author zhouliyu
 * @since 2019-04-11 14:21:07
 */
public class CfgServiceException extends BizLocaleException {
    private static final long serialVersionUID = 9056666767267362261L;

    public CfgServiceException(CfgErrorCodeEnum errorCodeEnum){
        ResultCode resultCode = new BaseResultCode(errorCodeEnum.getCode(), errorCodeEnum.getMsg());
        super.setErrorCode(resultCode);
    }

    public CfgServiceException(CfgErrorCodeEnum errorCodeEnum, Object... args){
        String msg = String.format(errorCodeEnum.getMsg(), args);
        ResultCode resultCode = new BaseResultCode(errorCodeEnum.getCode(), msg);
        super.setErrorCode(resultCode);
    }

}
