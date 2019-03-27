package com.cetiti.iotp.cfgservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cetiti.iotp.cfgservice.mapper")
@EnableDubbo
public class CfgServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(CfgServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CfgServiceApplication.class, args);
		logger.info("iotp-cfg service startup successful");
	}

}