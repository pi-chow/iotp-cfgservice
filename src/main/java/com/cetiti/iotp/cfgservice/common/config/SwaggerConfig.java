package com.cetiti.iotp.cfgservice.common.config;

import com.cetiti.iotp.cfgservice.common.config.properties.IotpCfgServiceProperties;
import com.cetiti.iotp.cfgservice.common.config.properties.SwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = SwaggerProperties.IOTP_SWAGGER_PREFIX, name = "enable", havingValue = "true")
public class SwaggerConfig {

    @Autowired
    private SwaggerProperties swaggerProperties;
    @Autowired
    private IotpCfgServiceProperties iotpCfgServiceProperties;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contact(getContact())
                .termsOfServiceUrl(swaggerProperties.getContactUrl())
                .version(iotpCfgServiceProperties.getVersion())
                .build();
    }

    /**
     * 创建文档联系人
     * */
    private Contact getContact(){
        return new Contact(swaggerProperties.getContactName(),swaggerProperties.getContactUrl(),swaggerProperties.getContactEmail());
    }

}
