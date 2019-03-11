package com.cetiti.iotpcfgservice.common.access;

import com.alibaba.fastjson.JSON;
import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.utils.CookieUtil;
import com.cetiti.ddapv2.iotplatform.common.utils.JwtUtil;
import com.cetiti.iotpcfgservice.common.result.CfgResultCode;
import com.cetiti.iotpcfgservice.common.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import xiaojian.toolkit.base.ResultCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

import static com.cetiti.ddapv2.iotplatform.common.AuthConstant.ACCESS_TOKEN;

/**
 * 权限拦截器。
 *
 * @author yangshutian
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        JwtAccount account = getAccount(request, response);
        if (account == null) {
            render(response, CfgResultCode.NOT_ALLOWED);
            return false;
        }

        logger.info("Request uri[{}], account[{}].", uri, account);

        AccountContext.setAccount(account);
        return true;
    }

    private JwtAccount getAccount(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = CookieUtil.get(request, ACCESS_TOKEN);
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }

        try {
            return JwtUtil.verifyAndParse(accessToken);
        } catch (Exception e) {
            logger.error("access jwt错误.", e);
            return null;
        }
    }

    private void render(HttpServletResponse response, ResultCode cfgResultCode) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cfgResultCode));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
