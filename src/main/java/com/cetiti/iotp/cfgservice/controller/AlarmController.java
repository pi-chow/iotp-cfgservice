package com.cetiti.iotp.cfgservice.controller;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.tip.BaseTip;
import com.cetiti.ddapv2.iotplatform.common.tip.SuccessTip;
import com.cetiti.ddapv2.iotplatform.common.utils.DateUtil;
import com.cetiti.iotp.cfgservice.common.result.CfgErrorCodeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgServiceException;
import com.cetiti.iotp.cfgservice.common.utils.DeviceModelValidateManger;
import com.cetiti.iotp.cfgservice.domain.entity.DeviceAlarmConfig;
import com.cetiti.iotp.cfgservice.domain.entity.DeviceAlarmException;
import com.cetiti.iotp.cfgservice.common.enums.AlarmTypeEnum;
import com.cetiti.iotp.cfgservice.service.AlarmService;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import com.cetiti.iotp.itf.cfgservice.vo.ThingModelFieldVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备告警配置控制器
 * @author zhouliyu
 * @since 2019-03-29 17:07:07
 */
@Api("设备告警配置控制器")
@RequestMapping("/alarm")
@RestController
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private ThingModelService thingModelService;

    @Autowired
    private DeviceModelValidateManger deviceModelValidateManger;

    @ApiOperation("获取告警配置列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "deviceModel", value = "设备型号"),
                        @ApiImplicitParam(paramType = "query", name = "pageNum", defaultValue = "1"),
                        @ApiImplicitParam(paramType = "query", name = "pageSize", defaultValue = "10")})
    @GetMapping(value = "/list")
    public BaseTip listDeviceAlarmConfig(JwtAccount account, int pageNum, int pageSize, String deviceModel) {
        if(StringUtils.isNoneBlank(deviceModel)){
            deviceModelValidateManger.validateDeviceModel(deviceModel);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("deviceModel", StringUtils.trimToNull(deviceModel));
        PageHelper.startPage(pageNum, pageSize, "create_time desc");
        List<DeviceAlarmConfig> listDeviceAlarmConfig = alarmService.listDeviceAlarmConfig(account, params);
        PageInfo<DeviceAlarmConfig> pageInfo = new PageInfo<>(listDeviceAlarmConfig);
        return new SuccessTip<>(pageInfo);

    }

    @ApiOperation("获取设备异常信息列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "deviceModel", value = "设备型号"),
            @ApiImplicitParam(paramType = "query", name = "deviceSn", value = "设备序号"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", name = "conditions", value = "告警条件"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", defaultValue = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", defaultValue = "10")})
    @GetMapping(value = "/deviceAlarmList")
    public BaseTip listDeviceAlarmException(JwtAccount account, int pageNum, int pageSize,
                                            String deviceModel, String deviceSn,
                                            String startTime, String endTime, String conditions){
        Map<String, Object> params = new HashMap<>();
        if(StringUtils.isNoneBlank(deviceModel)){
            deviceModelValidateManger.validateDeviceModel(deviceModel);
        }
        params.put("deviceModel", deviceModel);
        params.put("deviceSn", deviceSn);
        if(StringUtils.isNoneBlank(startTime)){
            params.put("startTime", DateUtil.parseTime(startTime));
        }
        if(StringUtils.isNoneBlank(endTime)){
            params.put("endTime", DateUtil.parseTime(endTime));
        }
        params.put("conditions", conditions);
        PageHelper.startPage(pageNum, pageSize, "create_time desc");
        List<DeviceAlarmException> listDeviceAlarmException = alarmService.listDeviceAlarmException(account, params);
        PageInfo<DeviceAlarmException> pageInfo = new PageInfo<>(listDeviceAlarmException);
        return new SuccessTip<>(pageInfo);
    }

    @ApiOperation("新增告警配置")
    @PostMapping(value = "/add")
    public BaseTip addDeviceAlarmConfig(JwtAccount account, @RequestBody DeviceAlarmConfig alarmConfig) {
        deviceModelValidateManger.validateDeviceModel(alarmConfig.getDeviceModel());
        if(alarmConfig.getDescription() != null && alarmConfig.getDescription().trim().length()==0){
            throw new CfgServiceException(CfgErrorCodeEnum.ALARM_DESCRIPTION_EMPTY);
        }
        boolean success = alarmService.addDeviceAlarmConfig(account, alarmConfig);
        if(!success){
            throw new CfgServiceException(CfgErrorCodeEnum.ALARM_ADD_ERROR);
        }
        return new SuccessTip("告警配置添加成功");
    }

    @ApiOperation("更新告警配置")
    @PostMapping(value = "/update")
    public BaseTip updateDeviceAlarmConfig(JwtAccount account, @RequestBody DeviceAlarmConfig alarmConfig) {
        deviceModelValidateManger.validateDeviceModel(alarmConfig.getDeviceModel());
        if(alarmConfig.getDescription() != null && alarmConfig.getDescription().trim().length()==0){
            throw new CfgServiceException(CfgErrorCodeEnum.ALARM_DESCRIPTION_EMPTY);
        }
        boolean success = alarmService.updateDeviceAlarmConfig(account, alarmConfig);
        if(!success){
            throw new CfgServiceException(CfgErrorCodeEnum.ALARM_UPDATE_ERROR);
        }
        return new SuccessTip("更新配置添加成功");
    }

    @ApiOperation("删除告警配置")
    @ApiImplicitParam(paramType = "query", required = true, name = "alarmId", value = "告警配置ID")
    @DeleteMapping(value = "/delete")
    public BaseTip deleteAlarm(String alarmId) {
        boolean success = alarmService.deleteDeviceAlarmConfig(alarmId);
        if(!success){
            throw new CfgServiceException(CfgErrorCodeEnum.ALARM_DELETE_ERROR);
        }
        return new SuccessTip("删除告警配置成功");
    }

    @ApiOperation(value = "获取最新告警配置", notes = "应用于设备异常处理")
    @GetMapping(value = "/getAlarmCfg")
    public BaseTip getAlarmCfgInfo() {
        return new SuccessTip<>(alarmService.uploadListAlarmConfig(AlarmTypeEnum.SENSOR.getValue()));
    }

    @ApiOperation("获取设备感知数据属性")
    @ApiImplicitParam(paramType = "query", name = "deviceModel", required = true, value = "设备型号")
    @GetMapping(value = "/getAttributeListByType")
    public BaseTip getAttributeList(JwtAccount account, String deviceModel) {
        deviceModelValidateManger.validateDeviceModel(deviceModel);
        List<ThingModelFieldVo> attributeList = thingModelService.listSensorFieldByDeviceModel(deviceModel);
        return new SuccessTip<>(attributeList);

    }

    @ApiOperation("告警类型列表")
    @GetMapping(value = "/alarmTypeList")
    public BaseTip listAlarmType(){
        return new SuccessTip<>(alarmService.listAlarmType());
    }





}
