package com.cetiti.iotp.cfgservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cetiti.iotpcfgservice.mapper")
@EnableDubbo
public class IotpCfgserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotpCfgserviceApplication.class, args);
	}

}
