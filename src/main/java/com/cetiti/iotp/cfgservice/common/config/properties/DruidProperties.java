package com.cetiti.iotp.cfgservice.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Druid数据源
 * @author zhouliyu
 * @since 2019-04-14 16:47:55
 */
@Data
@Component
@ConfigurationProperties(prefix = DruidProperties.IOTP_DRUID_PREFIX)
public class DruidProperties {

    public static final String IOTP_DRUID_PREFIX = "iotp.druid";
    
    private String resetEnable;

}
