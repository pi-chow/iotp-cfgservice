package com.cetiti.iotp.cfgservice.controller;


import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.tip.BaseTip;
import com.cetiti.ddapv2.iotplatform.common.tip.ErrorTip;
import com.cetiti.ddapv2.iotplatform.common.tip.SuccessTip;
import com.cetiti.iotp.cfgservice.common.result.CfgResultCode;
import com.cetiti.iotp.cfgservice.domain.DeviceAlarmConfig;
import com.cetiti.iotp.cfgservice.domain.ExceptionAlarm;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import com.cetiti.iotp.cfgservice.service.impl.AlarmServiceImpl;
import com.cetiti.iotp.cfgservice.service.AlarmService;
import com.cetiti.iotp.itf.cfgservice.vo.ThingModelField;
import com.cetiti.iotp.itf.assetservice.DeviceModelService;
import com.cetiti.iotp.itf.assetservice.vo.DeviceModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 告警controller。
 *
 * @author yangshutian
 */
@Api("告警管理接口")
@RestController
@RequestMapping("/alarm")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @Reference
    private DeviceModelService deviceModelService;

    @Autowired
    private ThingModelService thingModelService;

    /**
     * 获取告警配置列表
     *
     * @return
     */
    @ApiOperation("获取告警配置列表")
    @GetMapping(value = "/list")
    public BaseTip alarmList(JwtAccount account,
                             @RequestParam(required = false, defaultValue = "1") int pageNum,
                             @RequestParam(required = false, defaultValue = "10") int pageSize,
                             String deviceModel) {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceModel", StringUtils.trimToNull(deviceModel));
        Page<Object> pageInfo = PageHelper.startPage(pageNum, pageSize, "create_time desc");
        List<DeviceAlarmConfig> deviceAlarmConfigs = alarmService.getAlarmConfig(account, params);
        Map<String, Object> result = new HashMap<>();
        result.put("alarms", deviceAlarmConfigs);
        result.put("totalNum", pageInfo.getTotal());
        return new SuccessTip<>(result);

    }

    /**
     * 获取设备告警列表
     *
     * @return
     */
    @ApiOperation("获取设备告警列表")
    @GetMapping(value = "/deviceAlarmList")
    public BaseTip deviceAlarmList(JwtAccount account,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            String deviceModel, String deviceSn, String startTime, String endTime, String conditions) {
        Map<String, Object> params = new HashMap<>();
        try {
            if (startTime != null) {
                params.put("startTime", DateUtils.parseDateStrictly(startTime, "yyyy-MM-dd HH:mm:ss"));
            }
            if (endTime != null) {
                params.put("endTime", DateUtils.parseDateStrictly(endTime, "yyyy-MM-dd HH:mm:ss"));
            }

        } catch (ParseException e) {
            return new ErrorTip(CfgResultCode.ALARM_START_AND_END);
        }
        params.put("deviceModel", StringUtils.trimToNull(deviceModel));
        params.put("deviceSn", StringUtils.trimToNull(deviceSn));
        params.put("conditions", StringUtils.trimToNull(conditions));
        Page<Object> pageInfo = PageHelper.startPage(pageNum, pageSize, "create_time desc");
        List<ExceptionAlarm> deviceAlarmList = alarmService.deviceAlarmList(account, params);
        Map<String, Object> result = new HashMap<>();
        result.put("deviceAlarmList", deviceAlarmList);
        result.put("totalNum", pageInfo.getTotal());
        return new SuccessTip<>(result);
    }


    /**
     * 新增告警配置
     *
     * @return
     */
    @ApiOperation("新增告警配置")
    @PostMapping(value = "/add")
    public BaseTip addAlarm(JwtAccount account, @RequestBody DeviceAlarmConfig alarmConfig) {
        if(alarmConfig.getDeviceModel() != null){
            DeviceModel deviceModel = deviceModelService.viewDetail(alarmConfig.getDeviceModel());
            if(deviceModel == null){
                return new ErrorTip(CfgResultCode.DEVICE_MODEL_NOT_EXIST);
            }
        }

        if(alarmConfig.getDescription() != null && alarmConfig.getDescription().trim().length()==0){
            return new ErrorTip(CfgResultCode.ALARM_DESCRIPTION_EMPTY);
        }
        String alarmId = alarmService.addAlarmConfig(account, alarmConfig);
        return alarmId != null ? new SuccessTip(alarmId) : new ErrorTip(CfgResultCode.ALARM_CFG_ADD);
    }

    /**
     * 编辑告警配置
     *
     * @return
     */
    @ApiOperation("更新告警配置")
    @PutMapping(value = "/update")
    public BaseTip updateAlarm(JwtAccount account, @RequestBody DeviceAlarmConfig alarmConfig) {
        if(alarmConfig.getDeviceModel() != null){
            DeviceModel deviceModel = deviceModelService.viewDetail(alarmConfig.getDeviceModel());
            if(deviceModel == null){
                return new ErrorTip(CfgResultCode.DEVICE_MODEL_NOT_EXIST);
            }
        }
        if(alarmConfig.getDescription() != null && alarmConfig.getDescription().trim().length()==0){
            return new ErrorTip(CfgResultCode.ALARM_DESCRIPTION_EMPTY);
        }
        boolean success = alarmService.updateAlarmConfig(account, alarmConfig);
        return success ? new SuccessTip() : new ErrorTip(CfgResultCode.ALARM_CFG_UPDATE);
    }

    /**
     * 删除告警配置
     *
     * @return
     */
    @ApiOperation("删除告警配置")
    @DeleteMapping(value = "/delete/{alarmId}")
    public BaseTip deleteAlarm(@PathVariable("alarmId") String alarmId) {

        boolean success = alarmService.deleteAlarmConfig(alarmId);

        return success ? new SuccessTip() : new ErrorTip(CfgResultCode.ALARM_CFG_DELETE);
    }


    /**
     * 获取最新告警配置
     *
     * @return
     */
    @ApiOperation("获取最新告警配置")
    @GetMapping(value = "/getAlarmCfg")
    public BaseTip getAlarmCfgInfo(HttpServletResponse response) {
        response.addDateHeader("Last-Modified", AlarmServiceImpl.getLastModified());
        return new SuccessTip<>(alarmService.getAlarmConfig(null, new HashMap<>()));
    }


    /**
     * 根据设备类型获取告警属性
     *
     * @return BaseTip
     */
    @ApiOperation("获取告警属性-感知")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "deviceModel", value = "设备型号", required = true)
    })
    @GetMapping(value = "/getAttributeListByType")
    public BaseTip getAttributeListByCategory(JwtAccount account, @RequestParam("deviceModel") String deviceModel) {
        if(StringUtils.isEmpty(deviceModel)){
            return new ErrorTip(CfgResultCode.DEVICE_MODEL_EMPTY);
        }
        List<ThingModelField> attributeList = thingModelService.listSensoryThingModelFieldByDeviceModel(account, deviceModel);
        return new SuccessTip<>(attributeList);

    }

    /**
     * 告警类型
     * */
    @ApiOperation("告警类型")
    @GetMapping(value = "/alarmTypeList")
    public BaseTip alarmTypeList(){
        return new SuccessTip<>(alarmService.alarmTypeList());
    }
}
