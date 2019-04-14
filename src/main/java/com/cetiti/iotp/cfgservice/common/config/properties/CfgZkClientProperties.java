package com.cetiti.iotp.cfgservice.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Zookeeper配置
 * @author zhouliyu
 * @since 2019-04-14 16:24:41
 */
@Data
@Component
@ConfigurationProperties(prefix = CfgZkClientProperties.IOTP_CFG_ZKCLIENT_PREFIX)
public class CfgZkClientProperties {

    public static final String IOTP_CFG_ZKCLIENT_PREFIX = "iotp.cfg.zkclient";

    private String host;
    private Integer timeout=2000;
    private List<String> watcherPaths;

}
