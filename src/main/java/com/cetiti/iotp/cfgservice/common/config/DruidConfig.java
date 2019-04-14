package com.cetiti.iotp.cfgservice.common.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.cetiti.iotp.cfgservice.common.config.properties.DruidProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * druid监控页面配置
 * 默认地址为：localhost:8080/druid/login.html
 * @author zhouliyu
 * @since 2019-04-14 16:52:22
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = DruidProperties.IOTP_DRUID_PREFIX,  name = "enable", havingValue = "true")
public class DruidConfig {

    @Autowired
    private DruidProperties druidProperties;

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        log.info("init druid servlet configuration");
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        bean.addInitParameter("resetEnable", druidProperties.getResetEnable());
        return bean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>(new WebStatFilter());
        bean.addUrlPatterns("/*");
        // 忽略的格式信息
        bean.addInitParameter("exclusions", "*.js, *.gif, *.jpg, *.png, *.css, *.ico, /druid/*");
        return bean;
    }
}
