package com.cetiti.iotp.cfgservice.domain.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备协议模型字段信息
 * @author zhouliyu
 * @since 2019-04-01 10:56:51
 */
@Data
public class ThingModelField implements Serializable {
    private static final long serialVersionUID = 6921437579251038082L;

    /**
     * 设备协议模型头信息ID
     * */
    private String thingModelId;

    /**
     * 字段ID
     * */
    private String thingModelFieldId;

    /**
     * 字段名
     * */
    private String fieldName;

    /**
     * 对应数据库字段
     * */
    private String column;

    /**
     * 中文标签
     * */
    private String label;

    /**
     * 数据类型
     * */
    private String customType;

    /**
     * 长度值：定长
     * */
    private Integer fixedLength;

    /**
     * 长度关联字段ID:非定长
     * */
    private String linkField;

    /**
     * 单位
     * */
    private String unit;

    /**
     * 描述
     * */
    private String description;

    /**
     * 字段下标
     * */
    private Integer indexNum;

    /**
     * 编码格式
     * */
    private String charset;

    private Date createTime;

    private String createUser;

    private Date modifyTime;

    private String modifyUser;
}
