package com.cetiti.iotp.cfgservice.controller;


import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotp.cfgservice.common.result.Result;
import com.cetiti.iotp.cfgservice.domain.DeviceAlarmConfig;
import com.cetiti.iotp.cfgservice.domain.ExceptionAlarm;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import com.cetiti.iotp.cfgservice.service.impl.AlarmServiceImpl;
import com.cetiti.iotp.cfgservice.service.AlarmService;
import com.cetiti.iotp.itf.cfgservice.vo.ThingModelField;
import com.cetiti.iotp.itf.coreservice.OfflineCheckService;
import com.cetiti.iotp.itf.platformservice.DeviceModelService;
import com.cetiti.iotp.itf.platformservice.vo.DeviceModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
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

    @Reference
    private OfflineCheckService offlineCheckService;

    private static final String EVENT_NAME = "offline";
    /**
     * 获取告警配置列表
     *
     * @return
     */
    @ApiOperation("获取告警配置列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result alarmList(JwtAccount account,
                            @RequestParam(required = false, defaultValue = "1") int pageNum,
                            @RequestParam(required = false, defaultValue = "10") int pageSize,
                            String deviceModel) {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceModel", StringUtils.trimToNull(deviceModel));
        Page<Object> pageInfo = PageHelper.startPage(pageNum, pageSize, "create_time desc");
        List<DeviceAlarmConfig> deviceAlarmConfigs = alarmService.getAlarmConfig(account, params);
        return Result.ok().put("alarms", deviceAlarmConfigs)
                .put("totalNum", pageInfo.getTotal());

    }

    /**
     * 获取设备告警列表
     *
     * @return
     */
    @ApiOperation("获取设备告警列表")
    @RequestMapping(value = "/deviceAlarmList", method = RequestMethod.GET)
    public Result deviceAlarmList(JwtAccount account,
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
            return Result.error("错误的起止时间");
        }
        params.put("deviceModel", StringUtils.trimToNull(deviceModel));
        params.put("deviceSn", StringUtils.trimToNull(deviceSn));
        params.put("conditions", StringUtils.trimToNull(conditions));
        Page<Object> pageInfo = PageHelper.startPage(pageNum, pageSize, "create_time desc");
        List<ExceptionAlarm> deviceAlarmList = alarmService.deviceAlarmList(account, params);
        return Result.ok().put("deviceAlarmList", deviceAlarmList)
                .put("totalNum", pageInfo.getTotal());
    }


    /**
     * 新增告警配置
     *
     * @return
     */
    @ApiOperation("新增告警配置")
    @PostMapping(value = "/add")
    public Result addAlarm(JwtAccount account, @RequestBody DeviceAlarmConfig alarmConfig) {
        alarmConfig.setDeviceSn(StringUtils.trimToNull(alarmConfig.getDeviceSn()));
        if(alarmConfig.getDeviceModel() != null){
            DeviceModel deviceModel = deviceModelService.viewDetail(alarmConfig.getDeviceModel());
            if(deviceModel == null){
                return Result.error("设备型号：" + alarmConfig.getDeviceModel() + "不存在");
            }
        }

        if(alarmConfig.getDescription() != null && alarmConfig.getDescription().trim().length()==0){
            return Result.error("描述字段包含空格");
        }
        String alarmId = alarmService.addAlarmConfig(account, alarmConfig);
        if(alarmConfig.getField().equals(EVENT_NAME)){
            int offlineTimeInterval = Integer.parseInt(alarmConfig.getConditions().substring(7));
            offlineCheckService.addNeedCheckModel(alarmConfig.getDeviceModel(), offlineTimeInterval);
        }
        return alarmId != null ? Result.ok().put("alarmId", alarmId) : Result
                .error("新增告警失败！");
    }

    /**
     * 编辑告警配置
     *
     * @return
     */
    @ApiOperation("更新告警配置")
    @PutMapping(value = "/update")
    public Result updateAlarm(JwtAccount account, @RequestBody DeviceAlarmConfig alarmConfig) {
        if(alarmConfig.getDeviceModel() != null){
            DeviceModel deviceModel = deviceModelService.viewDetail(alarmConfig.getDeviceModel());
            if(deviceModel == null){
                return Result.error("设备型号：" + alarmConfig.getDeviceModel() + "不存在");
            }
        }
        if(alarmConfig.getDescription() != null && alarmConfig.getDescription().trim().length()==0){
            return Result.error("描述字段包含空格");
        }
        if(alarmConfig.getField().equals(EVENT_NAME)){
            int offlineTimeInterval = Integer.parseInt(alarmConfig.getConditions().substring(7));
            offlineCheckService.addNeedCheckModel(alarmConfig.getDeviceModel(), offlineTimeInterval);
        }
        boolean success = alarmService.updateAlarmConfig(account, alarmConfig);
        return success ? Result.ok() : Result.error("告警配置更新失败");
    }

    /**
     * 删除告警配置
     *
     * @return
     */
    @ApiOperation("删除告警配置")
    @DeleteMapping(value = "/delete/{alarmId}")
    public Result deleteAlarm(@PathVariable("alarmId") String alarmId) {

        boolean success = alarmService.deleteAlarmConfig(alarmId);

        DeviceAlarmConfig alarmConfig = alarmService.getAlarmConfig(alarmId);
        if(alarmConfig.getField().equals(EVENT_NAME)){
            offlineCheckService.removeNeedCheckModel(alarmConfig.getDeviceModel());
        }

        return success ? Result.ok() : Result.error("删除告警失败！");
    }


    /**
     * 获取最新告警配置
     *
     * @return
     */
    @ApiOperation("获取最新告警配置")
    @GetMapping(value = "/getAlarmCfg")
    public Result getAlarmCfgInfo(HttpServletResponse response) {
        response.addDateHeader("Last-Modified", AlarmServiceImpl.getLastModified());
        return Result.ok().put("guard", alarmService.getAlarmConfig(null, new HashMap<>()));
    }


    /**
     * 根据设备类型获取告警属性
     *
     * @return Result
     */
    @ApiOperation("根据设备类型获取告警属性")
    @GetMapping(value = "/getAttributeListByType/{category}")
    public Result getAttributeListByCategory(JwtAccount account, @PathVariable String category) {
        if(StringUtils.isEmpty(category)){
            return Result.error("设备型号不能为空");
        }
        List<ThingModelField> attributeList = thingModelService.listSensoryThingModelFieldByDeviceModel(account, category);
        attributeList.add(alarmService.getDeviceModelStatus());
        return Result.ok().put("attributeList", attributeList);

    }
}
