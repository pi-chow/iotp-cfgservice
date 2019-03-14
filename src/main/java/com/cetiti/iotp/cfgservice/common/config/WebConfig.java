package com.cetiti.iotp.cfgservice.common.config;

import com.cetiti.iotp.cfgservice.common.access.AccessInterceptor;
import com.cetiti.iotp.cfgservice.common.access.AccountArgumentResolver;
import com.cetiti.iotp.cfgservice.common.utils.StringToDateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
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
    AccountArgumentResolver accountArgumentResolver;

    @Autowired
    AccessInterceptor accessInterceptor;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    private static final List<String> WHITELIST = new ArrayList<>();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        WHITELIST.add("/alarm/getAlarmCfg");
        WHITELIST.add("/modelFlow/thingModelDefinition.jar");

        registry.addInterceptor(accessInterceptor)
                 .excludePathPatterns(WHITELIST)
                .excludePathPatterns("/swagger-resources/**", "/swagger-ui.html/**")
        ;

    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(accountArgumentResolver);
    }


    /**
     * 增加字符串转日期的功能
     */

    @PostConstruct
    public void initEditableValidation() {

        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
        if (initializer.getConversionService() != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();

            genericConversionService.addConverter(new StringToDateConverter());

        }

    }

}
