package com.cetiti.iotp.cfgservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 消息字段
 *
 * @author zhouliyu
 */
public class ThingModelField implements Serializable {

    private static final long serialVersionUID = 8103537855216843026L;
    // 自增id
    @JsonIgnore
    private int id;
    // 消息体id
    @JsonIgnore
    private String thingModelId;
    // 名
    private String name;
    // 描述
    private String description;
    // 数据类型
    private String customType;
    // 默认值
    private String defaultValue;
    // 字节长度{int、float、boolean、String、byte[]}
    private int valueLength;
    // 长度关联字段名
    private String lenField;
    // 消息体中的索引位置
    private int indexNum;
    // 编码
    private String charset;

    /**
     * 对应表里面的字段名称。
     */
    private String column;

    /**
     * 单位。可选。
     */
    private String unit;

    /**
     * 中文标签。
     */
    private String label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThingModelId() {
        return thingModelId;
    }

    public void setThingModelId(String thingModelId) {
        this.thingModelId = thingModelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getValueLength() {
        return valueLength;
    }

    public void setValueLength(int valueLength) {
        this.valueLength = valueLength;
    }

    public String getLenField() {
        return lenField;
    }

    public void setLenField(String lenField) {
        this.lenField = lenField;
    }

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
