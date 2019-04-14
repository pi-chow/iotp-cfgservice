package com.cetiti.iotp.cfgservice.common.utils;

import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.CustomTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.StructTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.ThingModelTypeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgErrorCodeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgServiceException;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelField;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;
import com.cetiti.iotp.cfgservice.domain.vo.ThingModel;
import com.cetiti.iotp.cfgservice.mapper.ThingModelFieldMapper;
import com.cetiti.iotp.cfgservice.mapper.ThingModelMapper;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.cetiti.ddapv2.iotplatform.common.thingModel.enums.CustomTypeEnum.UNFIXED_STRING;

/**
 * 设备协议模型校验
 * @author zhouliyu
 * @since 2019-04-11 09:34:13
 */
@Component
public class ThingModelValidateManager {

    private static final int IS_TEMPLATE = 1;

    @Autowired
    private  DeviceModelValidateManger deviceModelValidateManger;

    @Autowired
    private ThingModelMapper thingModelMapper;

    @Autowired
    private ThingModelFieldMapper thingModelFieldMapper;


    /**
     * 模型头信息校验
     * @param thingModelHeader 模型头信息
     * */
    public void validateThingModelHeader(ThingModelHeader thingModelHeader){
        //判断设备型号是否存在
        deviceModelValidateManger.validateDeviceModel(thingModelHeader.getDeviceModel());
        //判断模型类型(仅事件以及结构体允许多个)
        validateThingModelType(thingModelHeader.getDeviceModel(), thingModelHeader.getThingModelType());
        //事件名不重复
        validateThingModelName(thingModelHeader.getDeviceModel(), thingModelHeader.getThingModelName());
    }

    private void validateThingModelType(String deviceModel, String thingModelType){
        Preconditions.checkArgument(StringUtils.isNoneBlank(deviceModel, thingModelType), "设备型号与模型类型不为空");
        ThingModelHeader thingModelTypeExample = new ThingModelHeader();
        thingModelTypeExample.setDeviceModel(deviceModel);
        thingModelTypeExample.setThingModelType(thingModelType);
        if(isExitThingModelHeader(thingModelTypeExample) &&
                !ThingModelTypeEnum.allowMultiThingModelType(thingModelType)){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_INVALID);
        }
    }

    private void validateThingModelName(String deviceModel, String thingModelName){
        Preconditions.checkArgument(StringUtils.isNoneBlank(deviceModel, thingModelName), "设备型号与模型名称不为空");
        ThingModelHeader thingModelNameExample = new ThingModelHeader();
        thingModelNameExample.setDeviceModel(deviceModel);
        thingModelNameExample.setThingModelType(thingModelName);
        if(isExitThingModelHeader(thingModelNameExample)){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_NAME_EXISTED);
        }
    }

    private boolean isExitThingModelHeader(ThingModelHeader thingModelHeader){
        int count = thingModelMapper.selectCount(thingModelHeader);
        if(count < 1){
            return false;
        }
        return true;
    }


    public void validateThingModelField(ThingModelField thingModelField){
        //模型字段名校验
        validateFieldName(thingModelField.getThingModelId(), thingModelField.getFieldName());
        //LV_BYTES:字段类型校验
        ThingModelHeader thingModelHeader = thingModelMapper.getThingModelHeader(thingModelField.getThingModelId(), null, null);
        if(thingModelHeader != null && StructTypeEnum.LV_BYTES.getValue().equals(thingModelHeader.getStructType())){
            validateCustomType(thingModelField);
        }

    }

    /**
     * 模型字段名校验
     * @param fieldName 字段名
     * */
    private void validateFieldName(String thingModelId, String fieldName){
        Preconditions.checkArgument(StringUtils.isNoneBlank(thingModelId), "模型ID不为空");
        ThingModelField fieldNameExample = new ThingModelField();
        fieldNameExample.setFieldName(fieldName);
        fieldNameExample.setThingModelId(thingModelId);
        //字段名不为空
        if(StringUtils.isBlank(fieldNameExample.getFieldName())){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_FIELD_NAME_EMPTY);
        }
        //字段名不重复
        if(isExitThingModelField(fieldNameExample)){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_FIELD_NAME_EXIST);
        }
    }

    /**
     * 字段类型校验
     * @param thingModelField 模型字段
     * */
    private void validateCustomType(ThingModelField thingModelField){
        Preconditions.checkArgument(StringUtils.isNoneBlank(thingModelField.getCustomType()), "模型字段类型不为空");
        switch (CustomTypeEnum.getCustomTypeEnum(thingModelField.getCustomType())){
            case UNFIXED_STRING:
                validateUnfixedString(thingModelField);
            case INT:
                validateInteger(thingModelField);
        }
    }

    /**
     * 字段类型校验:非定长字符串：关联字段不为空且类型为Integer
     * @param thingModelField 模型字段
     * */
    private void validateUnfixedString(ThingModelField thingModelField){
        ThingModelField linkField = new ThingModelField();
        linkField.setThingModelId(thingModelField.getThingModelId());
        linkField.setThingModelFieldId(thingModelField.getLinkField());
        if(StringUtils.isBlank(thingModelField.getLinkField()) &&
                !isExitThingModelField(linkField)){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_LINK_FIELD_EMPTY);
        }
        String customType = thingModelFieldMapper.getThingModelField(thingModelField.getLinkField()).getCustomType();
        if(!CustomTypeEnum.INT.getCustomType().equals(customType)){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_LINK_FIELD_NOT_INT);
        }
    }

    /**
     * 字段类型校验：整型:是否作为关联字段
     * @param thingModelField 模型字段
     * */
    private void validateInteger(ThingModelField thingModelField){
        ThingModelField linkField = new ThingModelField();
        linkField.setThingModelId(thingModelField.getThingModelId());
        linkField.setLinkField(thingModelField.getThingModelFieldId());
        if(isExitThingModelField(linkField)){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_LINK_FIELD_EXIST);
        }
    }

    private boolean isExitThingModelField(ThingModelField thingModelField){
        int count = thingModelFieldMapper.selectCount(thingModelField);
        if(count < 1){
            return false;
        }
        return true;
    }


    /**
     * 设备协议模型校验
     * @param thingModel 模型
     * */
    public void validateThingModel(ThingModel thingModel){
        List<ThingModelField> thingModelFields = thingModel.getThingModelFields();
        //模型字段不为空
        if(thingModelFields == null || thingModelFields.size() == 0){
            String tips = thingModel.getThingModelHeader().getDeviceModel()
                                    .concat(":")
                                    .concat(thingModel.getThingModelHeader().getThingModelType());
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_MISS_FIELD, tips);
        }
    }

    /**
     * 设备协议模型模板校验
     * @param deviceModel 设备型号
     * */
    public void validateAsTemplate(String deviceModel){
        deviceModelValidateManger.validateDeviceModel(deviceModel);
        ThingModelHeader sensoryThingModel = thingModelMapper
                .getThingModelHeader(null, deviceModel, ThingModelTypeEnum.SENSOR.getValue());
        //感知模型不为空
        if(sensoryThingModel == null){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_SENSOR_NOT_EXIST);
        }
        //是否已作为模板
        if(sensoryThingModel.getTemplate() == IS_TEMPLATE){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_TEMPLATE_EXIST);
        }
    }

    public void validateCopyTemplate(String deviceModel, String templateId){
        deviceModelValidateManger.validateDeviceModel(deviceModel);
        List<ThingModelHeader> thingModelHeaderList = thingModelMapper.listThingModelHeader(deviceModel, null);
        //模型已存在
        if (thingModelHeaderList != null && thingModelHeaderList.size() > 0) {
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_EXIST);
        }
        //该型号未定义模板
        ThingModelHeader sensoryThingModel = thingModelMapper
                .getThingModelHeader(null, templateId, ThingModelTypeEnum.SENSOR.getValue());
        if(sensoryThingModel.getTemplate() != IS_TEMPLATE){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_TEMPLATE_INVALID);
        }

    }

}
