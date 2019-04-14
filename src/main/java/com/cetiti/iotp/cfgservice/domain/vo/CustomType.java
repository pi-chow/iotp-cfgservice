package com.cetiti.iotp.cfgservice.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 字段类型
 * @author zhouliyu
 * @since 2019-04-08 19:04:52
 */
@Data
public class CustomType implements Serializable {

    private static final long serialVersionUID = -8222913309920883312L;

    private String type;

    private String name;
}
