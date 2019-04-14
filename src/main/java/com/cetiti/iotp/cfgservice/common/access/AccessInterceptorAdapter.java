package com.cetiti.iotp.cfgservice.common.access;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.utils.CookieUtil;
import com.cetiti.ddapv2.iotplatform.common.utils.JwtUtil;
import com.cetiti.iotp.cfgservice.common.result.CfgErrorCodeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgServiceException;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.cetiti.ddapv2.iotplatform.common.AuthConstant.ACCESS_TOKEN;

/**
 * 用户权限拦截器
 * @author zhouliyu
 * @since 2019-04-11 14:19:07
 */
@Slf4j
@Component
public class AccessInterceptorAdapter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        JwtAccount account = getAccount(request);
        String uri = request.getRequestURI();
        if(account == null){
            throw new CfgServiceException(CfgErrorCodeEnum.NOT_ALLOWED);
        }
        log.info("Request uri[{}], account[{}].", uri, account);
        AccountContext.setAccount(account);
        return true;
    }

    private JwtAccount getAccount(HttpServletRequest request){
        String accessToken = CookieUtil.get(request, ACCESS_TOKEN);
        if(StringUtils.isBlank(accessToken)){
            return null;
        }
        try {
            return JwtUtil.verifyAndParse(accessToken);
        }catch (Exception e){
            throw new CfgServiceException(CfgErrorCodeEnum.ACCESS_JWT_ERROR);
        }

    }
}
