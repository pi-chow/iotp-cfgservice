package com.cetiti.iotp.cfgservice;

import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.CustomTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.StoreTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.StructTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.ThingModelTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.utils.GenerationSequenceUtil;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelField;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author zhouliyu
 * @since 2019-04-04 15:49:19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ThingModelServiceTest {

    private static final String DEVICE_MODEL = "TEST";

    @Autowired
    private ThingModelService thingModelService;

    @Test
    public void listThingModel(){
        System.out.println(thingModelService.listThingModel(DEVICE_MODEL));
    }

    @Test
    public void addThingModelHeaderSensor(){
        ThingModelHeader thingModelHeader = new ThingModelHeader();
        thingModelHeader.setDeviceModel("TEST1");
        thingModelHeader.setDeviceModelName("测试设备");
        thingModelHeader.setThingModelId(GenerationSequenceUtil.uuid());
        thingModelHeader.setThingModelType(ThingModelTypeEnum.SENSOR.getValue());
        thingModelHeader.setStructType(StructTypeEnum.LV_BYTES.getValue());
        thingModelHeader.setStoreType(StoreTypeEnum.HBASE.getValue());
        thingModelHeader.setDescription("测试模型");
        thingModelHeader.setTemplate(0);
        thingModelHeader.setSqlStatements("");
        thingModelHeader.setCreateTime(new Date());
        thingModelHeader.setCreateUser("test");
        thingModelHeader.setModifyTime(new Date());
        thingModelHeader.setModifyUser("test");
        thingModelService.addThingModelHeader(thingModelHeader);
    }

    @Test
    public void addThingModelHeader(){
        ThingModelHeader thingModelHeader = new ThingModelHeader();
        thingModelHeader.setDeviceModel("TEST");
        thingModelHeader.setDeviceModelName("测试设备");
        thingModelHeader.setThingModelId(GenerationSequenceUtil.uuid());
        thingModelHeader.setThingModelType(ThingModelTypeEnum.EVENT.getValue());
        thingModelHeader.setThingModelName("event1");
        thingModelHeader.setDescription("测试模型");
        thingModelHeader.setTemplate(0);
        thingModelHeader.setSqlStatements("");
        thingModelHeader.setCreateTime(new Date());
        thingModelHeader.setCreateUser("test");
        thingModelHeader.setModifyTime(new Date());
        thingModelHeader.setModifyUser("test");
        thingModelService.addThingModelHeader(thingModelHeader);
    }


    @Test
    public void addThingModelField(){
        ThingModelField thingModelField = new ThingModelField();
        thingModelField.setThingModelId("e54d905c9c674a33ae2dbbdd197e9eee");
        thingModelField.setThingModelFieldId(GenerationSequenceUtil.uuid());
        thingModelField.setFieldName("field2");
        thingModelField.setColumn("field2");
        thingModelField.setLabel("字段2");
        thingModelField.setCustomType(CustomTypeEnum.FLOAT.getCustomType());
        thingModelField.setLinkField("");
        thingModelField.setUnit("unit");
        thingModelField.setDescription("test");
        thingModelField.setIndexNum(1);
        thingModelField.setCharset("UTF-8");
        thingModelService.addThingModelField(thingModelField);
    }

    @Test
    public void updateThingModelField(){
        ThingModelField thingModelField = new ThingModelField();
        thingModelField.setThingModelFieldId("0ba6b238d8764813a9276d2d0069aa2c");
        thingModelField.setFieldName("field");
        thingModelField.setColumn("field");
        thingModelField.setLabel("字段");
        thingModelField.setCustomType(CustomTypeEnum.INT.getCustomType());
        thingModelField.setFixedLength(4);
        thingModelField.setLinkField("");
        thingModelField.setUnit("unit");
        thingModelField.setDescription("test1");
        thingModelField.setIndexNum(0);
        thingModelField.setCharset("UTF-8");
        thingModelService.updateThingModelField(thingModelField);
    }

    @Test
    public void getStoreType(){
        System.out.println(thingModelService.getStoreType(ThingModelTypeEnum.STATUS.getValue()));
    }

    @Test
    public void getStructType(){
        System.out.println(thingModelService.getStructType());
    }

    @Test
    public void getFieldCustomType(){
        System.out.println(thingModelService.getFieldCustomType(StructTypeEnum.LV_BYTES.getValue()));
    }

    @Test
    public void delete(){
        thingModelService.deleteThingModel("7a4bf8729f774448803445301d89e411");
    }

    @Test
    public void publish(){
        System.out.println(thingModelService.publish("TEST"));
    }

    
}
