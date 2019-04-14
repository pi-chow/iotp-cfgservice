package com.cetiti.iotp.cfgservice.service.impl;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.ThingModelTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.utils.GenerationSequenceUtil;
import com.cetiti.iotp.cfgservice.common.enums.RoleEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgErrorCodeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgServiceException;
import com.cetiti.iotp.cfgservice.common.utils.DeviceModelValidateManger;
import com.cetiti.iotp.cfgservice.common.utils.ThingModelValidateManager;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelField;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;
import com.cetiti.iotp.cfgservice.mapper.ThingModelMapper;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import com.cetiti.iotp.cfgservice.service.ThingModelTemplateService;
import com.cetiti.iotp.itf.assetservice.vo.DeviceModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhouliyu
 * @since 2019-04-02 09:00:42
 */
@Slf4j
@Service
public class ThingModelTemplateServiceImpl implements ThingModelTemplateService {

    private static final int IS_TEMPLATE = 1;

    @Autowired
    private ThingModelMapper thingModelMapper;

    @Autowired
    private ThingModelService thingModelService;

    @Autowired
    private ThingModelValidateManager thingModelValidateManager;

    @Autowired
    private DeviceModelValidateManger deviceModelValidateManger;

    @Override
    public void addThingModelTemplate(JwtAccount account, String deviceModel) {
        thingModelValidateManager.validateAsTemplate(deviceModel);
        List<ThingModelHeader> thingModelHeaderList = thingModelMapper.listThingModelHeader(deviceModel, null);
        for (ThingModelHeader thingModelHeader : thingModelHeaderList){
            buildTemplate(account, thingModelHeader);
            thingModelMapper.thingModelAsTemplate(thingModelHeader);
        }
    }


    private void buildTemplate(JwtAccount account, ThingModelHeader thingModelHeader){
        thingModelHeader.setTemplate(IS_TEMPLATE);
        thingModelHeader.setModifyUser(account.getUserId());
        thingModelHeader.setModifyTime(new Date());
    }


    @Override
    public void copyThingModelTemplate(JwtAccount account, String deviceModel, String templateId) {
        thingModelValidateManager.validateCopyTemplate(deviceModel, templateId);
        List<ThingModelHeader> templateList = thingModelMapper.listThingModelHeader(templateId, null);
        ThingModelComparator(templateList);
        if (templateList == null || templateList.size() == 0) {
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_TEMPLATE_MISS);
        }
        templateList.forEach(e->buildThingModel(account,e,deviceModel));
    }

    /**
     * 模型按顺序List 感知-属性-状态-事件-结构体
     * @param templateList 模型模板
     * */
    private void ThingModelComparator(List<ThingModelHeader> templateList){
        Collections.sort(templateList, new Comparator<ThingModelHeader>() {
            @Override
            public int compare(ThingModelHeader o1, ThingModelHeader o2) {
                int index1 = ThingModelTypeEnum.getThingModelTypeEnum(o1.getThingModelType()).getIndex();
                int index2 = ThingModelTypeEnum.getThingModelTypeEnum(o2.getThingModelType()).getIndex();
                if(index1 > index2){
                    return 1;
                }else if(index1 == index2){
                    return 0;
                }
                return -1;
            }
        });
    }

    private void buildThingModel(JwtAccount account,ThingModelHeader thingModelHeader, String deviceModel){
        List<ThingModelField> thingModelFields = thingModelService.listThingModelField(thingModelHeader.getThingModelId());
        thingModelHeader.setThingModelId(GenerationSequenceUtil.uuid());
        DeviceModel deviceModelView = deviceModelValidateManger.getDeviceModel(deviceModel);
        thingModelHeader.setDeviceModel(deviceModelView.deviceModel);
        thingModelHeader.setDeviceModelName(deviceModelView.deviceModelName);
        thingModelHeader.setTemplate(0);
        thingModelHeader.setCreateUser(account.getUserId());
        thingModelHeader.setCreateTime(new Date());
        thingModelHeader.setModifyUser(account.getUserId());
        thingModelHeader.setModifyTime(new Date());
        thingModelService.addThingModelHeader(thingModelHeader);
        if(thingModelFields.size() != 0){
            for(ThingModelField thingModelField : thingModelFields){
                thingModelField.setThingModelId(thingModelHeader.getThingModelId());
                thingModelField.setThingModelFieldId(GenerationSequenceUtil.uuid());
                thingModelService.addThingModelField(thingModelField);
            }
        }
    }


    @Override
    public List<ThingModelHeader> listThingModelTemplate(JwtAccount account, Map<String, Object> params) {
        if(StringUtils.equalsIgnoreCase(account.getRoles(), RoleEnum.ROLE_DEV.getValue())){
            params.put("createUser", account.getUserId());
        }
        return thingModelMapper.listThingModelHeaderAsTemplate(params);
    }

    @Override
    public boolean deleteThingModelTemplate(String deviceModel) {
        return thingModelMapper.deleteThingModelHeaderAsTemplate(deviceModel) >= 1;
    }
}
