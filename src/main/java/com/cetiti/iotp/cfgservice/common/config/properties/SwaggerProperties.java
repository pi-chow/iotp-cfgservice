package com.cetiti.iotp.cfgservice.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhouliyu
 * @since 2019-03-20 17:00:07
 */
@Component
@ConfigurationProperties(prefix = SwaggerProperties.IOTP_SWAGGER_PREFIX)
public class SwaggerProperties {

    public static final String IOTP_SWAGGER_PREFIX = "iotp.swagger";

    private String basePackage;
    private Boolean enable = Boolean.FALSE;
    private String title;
    private String description;
    private String contactName;
    private String contactUrl;
    private String contactEmail;

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
