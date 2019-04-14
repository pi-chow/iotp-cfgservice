package com.cetiti.iotp.cfgservice.controller;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.tip.BaseTip;
import com.cetiti.ddapv2.iotplatform.common.tip.ErrorTip;
import com.cetiti.ddapv2.iotplatform.common.tip.SuccessTip;
import com.cetiti.iotp.cfgservice.common.result.CfgErrorCodeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgServiceException;
import com.cetiti.iotp.cfgservice.common.utils.DeviceModelValidateManger;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelField;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;
import com.cetiti.iotp.cfgservice.domain.vo.ThingModel;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import com.cetiti.iotp.cfgservice.service.ThingModelTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备协议模型控制器
 * @author zhouliyu
 * @since 2019-04-09 16:40:40
 */
@Api("设备协议模型控制器")
@RequestMapping("/modelFlow")
@RestController
public class ThingModelController {

    @Autowired
    private ThingModelService thingModelService;

    @Autowired
    private ThingModelTemplateService thingModelTemplateService;

    @Autowired
    private DeviceModelValidateManger deviceModelValidateManger;


    @ApiOperation(value = "发布设备协议模型", notes = "设备型号为空表示全发布，否则根据设备型号发布")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "deviceModel", value = "设备型号")})
    @GetMapping(value = "/publish")
    public BaseTip thingModelPublish(String deviceModel) {
        List<String> failure = thingModelService.publish(deviceModel);
        if(failure.size() != 0){
            return new ErrorTip(CfgErrorCodeEnum.THING_MODEL_PUBLISH_ERROR.getCode(),
                    "以下模型发布失败[" + failure.stream().collect(Collectors.joining(","))+ "]");
        }
        return new SuccessTip<>("设备协议模型发布成功");
    }

    @ApiOperation("获取模型列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", required = true, name = "deviceModel", value = "设备型号")})
    @GetMapping(value = "/list")
    public BaseTip listThingModel(String deviceModel){
        deviceModelValidateManger.validateDeviceModel(deviceModel);
        List<ThingModel> thingModelList = thingModelService.listThingModel(deviceModel);
        return new SuccessTip<>(thingModelList);
    }

    @ApiOperation("添加模型头信息")
    @PostMapping(value = "/add/thingModelHeader")
    public BaseTip addThingModelHeader(JwtAccount account, @RequestBody ThingModelHeader thingModelHeader){
        String createUser = account.getUserId();
        thingModelHeader.setCreateUser(createUser);
        thingModelHeader.setCreateTime(new Date());
        thingModelHeader.setModifyUser(createUser);
        thingModelHeader.setModifyTime(new Date());
        boolean success = thingModelService.addThingModelHeader(thingModelHeader);
        if(!success){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_ADD_ERROR);
        }
        return new SuccessTip<>("模型头信息添加成功");
    }

    @ApiOperation("删除模型信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", required = true, name = "thingModelId", value = "模型ID")})
    @DeleteMapping("/delete")
    public BaseTip deleteThingModel(String thingModelId){
        boolean success = thingModelService.deleteThingModel(thingModelId);
        if(!success){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_DELETE_ERROR);
        }
        return new SuccessTip<>("模型删除成功");
    }

    @ApiOperation("添加模型字段信息")
    @PostMapping("/add/thingModelField")
    public BaseTip addThingModelField(JwtAccount account, @RequestBody ThingModelField thingModelField){
        String createUser = account.getUserId();
        thingModelField.setCreateUser(createUser);
        thingModelField.setCreateTime(new Date());
        thingModelField.setModifyUser(createUser);
        thingModelField.setModifyTime(new Date());
        boolean success = thingModelService.addThingModelField(thingModelField);
        if(!success){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_FIELD_ADD_ERROR);
        }
        return new SuccessTip<>("模型字段添加成功");
    }

    @ApiOperation("更新模型字段信息")
    @PutMapping("/update/thingModelField")
    public BaseTip updateThingModelField(JwtAccount account, @RequestBody ThingModelField thingModelField){
        String createUser = account.getUserId();
        thingModelField.setModifyUser(createUser);
        thingModelField.setModifyTime(new Date());
        boolean success = thingModelService.updateThingModelField(thingModelField);
        if(!success){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_FIELD_UPDATE_ERROR);
        }
        return new SuccessTip<>("模型字段更新成功");
    }

    @ApiOperation("删除模型字段信息")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", required = true, name = "thingModelFieldId", value = "模型字段ID")})
    @DeleteMapping("/delete/thingModelField")
    public BaseTip deleteThingModelField(String thingModelFieldId){
        boolean success = thingModelService.deleteThingModelField(thingModelFieldId);
        if(!success){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_FIELD_DELETE_ERROR);
        }
        return new SuccessTip<>("模型字段更新成功");
    }

    @ApiOperation("获取模型类型")
    @GetMapping("/get/thingModelType")
    public BaseTip getThingModelType(){
        return new SuccessTip<>(thingModelService.getThingModelType());
    }


    @ApiOperation("获取模型存储类型")
    @GetMapping("/get/storeType")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", required = true, name = "thingModelType", value = "模型类型")})
    public BaseTip getStoreType(String thingModelType){
        return new SuccessTip<>(thingModelService.getStoreType(thingModelType));
    }

    @ApiOperation("获取模型结构体类型")
    @GetMapping("/get/structType")
    public BaseTip getStructType(){
        return new SuccessTip<>(thingModelService.getStructType());
    }

    @ApiOperation("获取字段类型")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", required = true, name = "structType", value = "模型结构体类型")})
    @GetMapping("/get/fieldCustomType")
    public BaseTip getFieldCustomType(String structType){
        return new SuccessTip<>(thingModelService.getFieldCustomType(structType));
    }

    @ApiOperation("模型作为模板")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", required = true, name = "deviceModel", value = "设备型号")})
    @GetMapping("/add/template")
    public BaseTip addThingModelTemplate(JwtAccount account, String deviceModel){
        thingModelTemplateService.addThingModelTemplate(account, deviceModel);
        return new SuccessTip("设备协议模型作为模板成功");
    }

    @ApiOperation("模型复制模板")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", required = true, name = "deviceModel", value = "设备型号"),
                        @ApiImplicitParam(paramType = "query", required = true, name = "templateId", value = "模板ID")})
    @GetMapping("/copyTemplate")
    public BaseTip copyThingModelTemplate(JwtAccount account, String deviceModel, String templateId){
        thingModelTemplateService.copyThingModelTemplate(account, deviceModel, templateId);
        return new SuccessTip("复制模板成功");
    }

    @ApiOperation("删除模型模板")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "deviceModel", value = "设备型号")})
    @DeleteMapping("/delete/template")
    public BaseTip deleteThingModelTemplate(String deviceModel){
        boolean success = thingModelTemplateService.deleteThingModelTemplate(deviceModel);
        if(!success){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_TEMPLATE_DELETE);
        }
        return new SuccessTip("删除模板成功");
    }

    @ApiOperation("获取模型模板列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "deviceModel", value = "设备型号"),
                        @ApiImplicitParam(paramType = "query", name = "deviceModelName", value = "设备型号名"),
                        @ApiImplicitParam(paramType = "query", name = "pageNum", defaultValue = "1"),
                        @ApiImplicitParam(paramType = "query", name = "pageSize", defaultValue = "10")})
    @GetMapping(value = "/list/template")
    public BaseTip listThingModelTemplate(JwtAccount account, String deviceModel, String deviceModelName, int pageNum, int pageSize){
        if(StringUtils.isNoneBlank(deviceModel)){
            deviceModelValidateManger.validateDeviceModel(deviceModel);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("deviceModel", StringUtils.trimToNull(deviceModel));
        params.put("deviceModelName", StringUtils.trimToNull(deviceModelName));
        PageHelper.startPage(pageNum, pageSize, "create_time desc");
        List<ThingModelHeader> thingModelHeaderList = thingModelTemplateService.listThingModelTemplate(account, params);
        PageInfo<ThingModelHeader> pageInfo = new PageInfo<>(thingModelHeaderList);
        return new SuccessTip<>(pageInfo);
    }

    @ApiOperation("拉取thing-model.jar")
    @GetMapping(value = "/thingModelDefinition.jar")
    public void getThingModelJar(HttpServletResponse response){
        File thingModelJar = thingModelService.getThingModelJar();

        if(!thingModelJar.exists()){
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }

        response.setContentLength((int) thingModelJar.length());
        response.setContentType("application/zip");
        response.addDateHeader("Last-Modified", thingModelJar.lastModified());

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(FileUtils.readFileToByteArray(thingModelJar));
            outputStream.flush();
        } catch (IOException e) {
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_JAR_NOT_FOUND);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }
}
