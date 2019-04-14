package com.cetiti.iotp.cfgservice.service.impl;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.CustomTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.StructTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.ThingModelTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.relation.StructTypeCustomTypeRelation;
import com.cetiti.ddapv2.iotplatform.common.thingModel.relation.ThingModelTypeStoreTypeRelation;
import com.cetiti.iotp.cfgservice.common.config.properties.CfgZkClientProperties;
import com.cetiti.iotp.cfgservice.common.result.CfgErrorCodeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgServiceException;
import com.cetiti.iotp.cfgservice.common.utils.DeviceModelValidateManger;
import com.cetiti.iotp.cfgservice.common.utils.ThingModelValidateManager;
import com.cetiti.iotp.cfgservice.common.zookeeper.CfgZkClient;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelField;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;
import com.cetiti.iotp.cfgservice.domain.vo.CustomType;
import com.cetiti.iotp.cfgservice.domain.vo.ThingModel;
import com.cetiti.iotp.cfgservice.domain.vo.ThingModelType;
import com.cetiti.iotp.cfgservice.mapper.ThingModelFieldMapper;
import com.cetiti.iotp.cfgservice.mapper.ThingModelMapper;
import com.cetiti.iotp.cfgservice.service.ThingModelProcess;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import com.cetiti.iotp.itf.assetservice.vo.DeviceModel;
import com.cetiti.iotp.itf.cfgservice.vo.ThingModelFieldVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhouliyu
 * @since 2019-04-01 17:05:46
 */
@Slf4j
@org.apache.dubbo.config.annotation.Service(interfaceClass = com.cetiti.iotp.itf.cfgservice.ThingModelService.class, timeout = 6000)
@Service
public class ThingModelServiceImpl implements ThingModelService {

    @Autowired
    private CfgZkClientProperties properties;

    @Autowired
    private CfgZkClient cfgZkClient;

    @Autowired
    private ThingModelMapper thingModelMapper;

    @Autowired
    private ThingModelFieldMapper thingModelFieldMapper;

    @Autowired
    private ThingModelProcess thingModelProcess;

    @Autowired
    private ThingModelValidateManager thingModelValidateManager;

    @Autowired
    private DeviceModelValidateManger deviceModelValidateManger;

    /**
     * 初始化构造器
     * */
    @PostConstruct
    private void initThingModelBuild() {
        List<String> failure = publish(null);
        if(failure != null && failure.size() > 0){
            log.warn("以下模型发布失败[" + failure.stream().collect(Collectors.joining(","))+ "]");
        }
    }



    @Override
    public List<ThingModel> listThingModel(String deviceModel) {
        List<ThingModelHeader> thingModelHeaderList = thingModelMapper.listThingModelHeader(deviceModel,null);
        List<ThingModel> thingModelList = new ArrayList<>(thingModelHeaderList.size());
        for (ThingModelHeader thingModelHeader: thingModelHeaderList){
            ThingModel thingModel = new ThingModel();
            thingModel.setThingModelHeader(thingModelHeader);
            thingModel.setThingModelFields(thingModelFieldMapper.listThingModelField(thingModelHeader.getThingModelId()));
            thingModelList.add(thingModel);
        }
        return thingModelList;
    }

    @Override
    public int thingModelCount(String deviceModel) {
        deviceModelValidateManger.validateDeviceModel(deviceModel);
        ThingModelHeader thingModelHeader = new ThingModelHeader();
        thingModelHeader.setDeviceModel(deviceModel);
        return thingModelMapper.selectCount(thingModelHeader);
    }

    @Override
    public boolean updateDeviceModelName(JwtAccount account, String deviceModel) {
        DeviceModel deviceModelView = deviceModelValidateManger.getDeviceModel(deviceModel);
        ThingModelHeader thingModelHeader = new ThingModelHeader();
        thingModelHeader.setDeviceModelName(deviceModelView.deviceModelName);
        thingModelHeader.setDeviceModel(deviceModel);
        thingModelHeader.setModifyUser(account.getUserId());
        thingModelHeader.setModifyTime(new Date());
        return thingModelMapper.updateDeviceModelName(thingModelHeader) >= 1;
    }

    @Override
    public List<String> publish(String deviceModel) {
        List<ThingModel> thingModelList = listThingModel(deviceModel);
        List<String> failure = new ArrayList<>();
        List<String> javaFullPath = new ArrayList<>();

        for (ThingModel thingModel : thingModelList){
            try {
                javaFullPath.add(thingModelProcess.classConstructor(thingModel));
            }catch (BizLocaleException e){
                failure.add(thingModel.getThingModelHeader().getDeviceModel()
                        .concat(":").concat(thingModel.getThingModelHeader().getThingModelType()));
                thingModelProcess.delFailureThingModel(thingModel.getThingModelHeader());
            }
        }
        if(thingModelProcess.classCompiler(javaFullPath)){
            if (thingModelProcess.classLoader()){
                updateNodeData();
            }
        }
        return failure;
    }

    @Override
    public File getThingModelJar() {
        return thingModelProcess.getThingModelJar();
    }

    @Override
    public boolean addThingModelHeader(ThingModelHeader thingModelHeader) {
        thingModelValidateManager.validateThingModelHeader(thingModelHeader);
        switch (ThingModelTypeEnum.getThingModelTypeEnum(thingModelHeader.getThingModelType())){
            case SENSOR:
                thingModelHeader.setThingModelName(thingModelHeader.getThingModelType());
                return thingModelMapper.insertSelective(thingModelHeader) >= 1;
            case PROP:
                thingModelHeader.setThingModelName(thingModelHeader.getThingModelType());
                buildThingModelHeaderExceptSensor(thingModelHeader);
                return thingModelMapper.insertSelective(thingModelHeader) >= 1;
            case STATUS:
                thingModelHeader.setThingModelName(thingModelHeader.getThingModelType());
                buildThingModelHeaderExceptSensor(thingModelHeader);
                return thingModelMapper.insertSelective(thingModelHeader) >= 1;
            case EVENT:
                buildThingModelHeaderExceptSensor(thingModelHeader);
                return thingModelMapper.insertSelective(thingModelHeader) >= 1;
        }
        return false;
    }

    /**
     * 构建模型头信息：非感知类
     * @param thingModelHeader 消息头信息
     * */
    private void buildThingModelHeaderExceptSensor(ThingModelHeader thingModelHeader){
        List<String> storeTypes = ThingModelTypeStoreTypeRelation.getStoreType(thingModelHeader.getThingModelType());
        thingModelHeader.setStoreType(storeTypes.stream().collect(Collectors.joining(",")));
        ThingModelHeader thingModelHeaderSensor = thingModelMapper.getThingModelHeader(null,
                thingModelHeader.getDeviceModel(), ThingModelTypeEnum.SENSOR.getValue());
        if(thingModelHeaderSensor != null){
            thingModelHeader.setStructType(thingModelHeaderSensor.getStructType());
        }else {
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_SENSOR_NOT_EXIST);
        }
    }

    @Override
    public boolean deleteThingModel(String thingModelId) {
        return thingModelMapper.deleteByPrimaryKey(thingModelId) >= 1;
    }

    @Override
    public boolean addThingModelField(ThingModelField thingModelField) {
        thingModelValidateManager.validateThingModelField(thingModelField);
        thingModelField.setFixedLength(setFixLength(thingModelField));
        return thingModelFieldMapper.insertSelective(thingModelField) >= 1;
    }

    @Override
    public boolean updateThingModelField(ThingModelField thingModelField) {
        thingModelValidateManager.validateThingModelField(thingModelField);
        thingModelField.setFixedLength(setFixLength(thingModelField));
        return thingModelFieldMapper.updateSelective(thingModelField) >= 1;
    }

    @Override
    public boolean deleteThingModelField(String thingModelFieldId) {
        ThingModelField thingModelField = thingModelFieldMapper.getThingModelField(thingModelFieldId);
        thingModelValidateManager.validateThingModelField(thingModelField);
        return thingModelFieldMapper.deleteByPrimaryKey(thingModelFieldId) >= 1;
    }

    @Override
    public ThingModelField getThingModelField(String thingModelFieldId) {
        return thingModelFieldMapper.getThingModelField(thingModelFieldId);
    }


    @Override
    public List<ThingModelField> listThingModelField(String thingModelId) {
        return thingModelFieldMapper.listThingModelField(thingModelId);
    }

    @Override
    public List<ThingModelFieldVo> listSensorFieldByDeviceModel(String deviceModel) {

        return thingModelFieldMapper.listSensorFieldByDeviceModel(deviceModel).stream().map(this::buildSensorFieldVo).collect(Collectors.toList());
    }

    private ThingModelFieldVo buildSensorFieldVo(ThingModelField thingModelField){
        ThingModelFieldVo sensorField = new ThingModelFieldVo();
        sensorField.setFieldName(thingModelField.getFieldName());
        sensorField.setCustomType(thingModelField.getCustomType());
        sensorField.setLabel(thingModelField.getLabel());
        sensorField.setUnit(thingModelField.getUnit());
        return sensorField;
    }

    @Override
    public List<ThingModelType> getThingModelType() {
        List<ThingModelTypeEnum> thingModelTypeEnumList = Arrays.asList(ThingModelTypeEnum.values());
        return thingModelTypeEnumList.stream()
                .filter(e -> e != ThingModelTypeEnum.STRUCT)
                .map(this :: buildThingModelType)
                .collect(Collectors.toList());

    }

    private ThingModelType buildThingModelType(ThingModelTypeEnum thingModelTypeEnum){
        ThingModelType thingModelType = new ThingModelType();
        thingModelType.setType(thingModelTypeEnum.getValue());
        thingModelType.setName(thingModelTypeEnum.getName());
        return thingModelType;
    }



    @Override
    public List<String> getStoreType(String thingModelType) {
        return ThingModelTypeStoreTypeRelation.getStoreType(thingModelType);
    }

    @Override
    public List<String> getStructType() {
        List<StructTypeEnum> list = Arrays.asList(StructTypeEnum.values());
        return list.stream().map(StructTypeEnum::getValue).collect(Collectors.toList());
    }

    @Override
    public List<CustomType> getFieldCustomType(String structType) {
        List<String> CustomTypeList = StructTypeCustomTypeRelation.listCustomType(structType);
        if(CustomTypeList != null){
            return CustomTypeList.stream().map(this::setCustomType).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * customType类型转换
     * @param customType 字段类型
     * */
    private CustomType setCustomType(String customType){
        CustomType result = new CustomType();
        CustomTypeEnum customTypeEnum = CustomTypeEnum.getCustomTypeEnum(customType);
        if(customTypeEnum != null){
            result.setType(customTypeEnum.getCustomType());
            result.setName(customTypeEnum.getName());
        }
        return result;
    }

    /**
     * 添加基本数据类型字节长度
     * @param thingModelField 模型字段
     * */
    private int setFixLength(ThingModelField thingModelField){
        ThingModelHeader thingModelHeader = thingModelMapper.getThingModelHeader(thingModelField.getThingModelId(), null, null);
        String structType = thingModelHeader.getStructType();
        if(structType.equals(StructTypeEnum.LV_BYTES.getValue())){
                String customType = thingModelField.getCustomType();
                if(StringUtils.isNoneBlank(customType)){
                    int fixedLength = CustomTypeEnum.getCustomTypeEnum(customType).getFixedLength();
                    switch (CustomTypeEnum.getCustomTypeEnum(customType)){
                        case FIXED_STRING:
                            return thingModelField.getFixedLength();
                        case UNFIXED_STRING:
                            break;
                        default:
                            return fixedLength;
                    }
                }
        }
        return 0;
    }

    /**
     * zookeeper 更新/iotp/cfg/thingmodel/publish/time节点
     * */
    private void updateNodeData() {
        String currentTime = String.valueOf(System.currentTimeMillis());
        try {
            cfgZkClient.setData(properties.getWatcherPaths().get(0),currentTime.getBytes());
        } catch (KeeperException | InterruptedException exception) {
            log.error("zookeeper error: thingModel->" + exception);
            throw new CfgServiceException(CfgErrorCodeEnum.ZOOKEEPER_UPDATE_DATA_ERROR);
        }
    }
}
