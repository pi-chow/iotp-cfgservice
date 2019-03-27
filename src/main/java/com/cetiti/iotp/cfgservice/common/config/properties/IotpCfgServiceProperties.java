package com.cetiti.iotp.cfgservice.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhouliyu
 * @since 2019-03-20 17:18:37
 */
@Component
@ConfigurationProperties(prefix = IotpCfgServiceProperties.IOTP_PREFIX)
public class IotpCfgServiceProperties {

    public static final String IOTP_PREFIX = "iotp";

    /**
     * 版本号，每次发布需更新application中对应的版本
     */
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
