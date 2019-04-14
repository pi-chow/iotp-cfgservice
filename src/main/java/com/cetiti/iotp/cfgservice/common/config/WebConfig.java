package com.cetiti.iotp.cfgservice.common.config;

import com.cetiti.iotp.cfgservice.common.access.AccessInterceptorAdapter;
import com.cetiti.iotp.cfgservice.common.access.AccountArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 将得到的UserArgumentResolver和权限拦截器加入到webMvc。
 *
 * @author yangshutian
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AccountArgumentResolver accountArgumentResolver;

    @Autowired
    private AccessInterceptorAdapter accessInterceptorAdapter;


    private static final List<String> WHITELIST = new ArrayList<>();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        WHITELIST.add("/alarm/getAlarmCfg");
        WHITELIST.add("/modelFlow/thingModelDefinition.jar");

        registry.addInterceptor(accessInterceptorAdapter)
                 .excludePathPatterns(WHITELIST)
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(accountArgumentResolver);
    }

}
