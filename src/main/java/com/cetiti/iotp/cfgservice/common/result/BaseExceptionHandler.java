package com.cetiti.iotp.cfgservice.common.result;

import com.cetiti.ddapv2.iotplatform.common.BaseResultCode;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.ddapv2.iotplatform.common.tip.ErrorTip;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常拦截器
 * @author zhouliyu
 * @since 2019-03-27 09:01:25
 */
@ControllerAdvice
public class BaseExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseExceptionHandler.class);

    /**
     * 业务异常
     * */
    @ExceptionHandler(BizLocaleException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorTip getBizLocaleException(BizLocaleException e){
        LOGGER.info("业务异常：" + e);
        return new ErrorTip(e.getErrorCode());
    }

    /**
     * RPC异常
     * */
    @ExceptionHandler(RpcException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorTip getRpcException(RpcException e){
        LOGGER.info("RPC调用异常：" + e);
        return new ErrorTip(e.getCode(), e.getMessage() != null ? "RPC接口异常：" + e.getMessage() : "非常抱歉，运行异常");
    }

    /**
     * 运行异常
     * */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorTip getRuntimeException(RuntimeException e){
        LOGGER.info("业务异常：" + e);
        return new ErrorTip(BaseResultCode.SERVER_ERROR.getResultCode(), e.getMessage() != null ? e.getMessage() : "非常抱歉，运行异常");
    }
}
