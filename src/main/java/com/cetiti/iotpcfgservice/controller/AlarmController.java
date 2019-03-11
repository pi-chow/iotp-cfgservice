package com.cetiti.iotpcfgservice.controller;


import com.cetiti.ddapv2.iotplatform.biz.service.DeviceService;
import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.iotpcfgservice.common.result.Result;
import com.cetiti.iotpcfgservice.domain.DeviceAlarmConfig;
import com.cetiti.iotpcfgservice.domain.ExceptionAlarm;
import com.cetiti.iotpcfgservice.service.AlarmService;
import com.cetiti.iotpcfgservice.service.impl.AlarmServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
@RequestMapping("/alarm")
public class AlarmController {

    private static final Logger logger = LoggerFactory.getLogger(AlarmController.class);

    @Autowired
    private AlarmService alarmService;

    @Reference
    private DeviceService deviceService;


    /**
     * 获取告警配置列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result alarmList(JwtAccount account,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            String deviceModel, String deviceSn) {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceModel", StringUtils.trimToNull(deviceModel));
        params.put("deviceSn", StringUtils.trimToNull(deviceSn));
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
    @PostMapping(value = "/add")
    public Result addAlarm(JwtAccount account, @RequestBody DeviceAlarmConfig alarmConfig) {
        alarmConfig.setDeviceSn(StringUtils.trimToNull(alarmConfig.getDeviceSn()));
        if (alarmConfig.getDeviceSn() != null && deviceService.view(alarmConfig.getDeviceModel(), alarmConfig.getDeviceSn()) == null) {
            return Result.error("设备sn不存在");
        }
        String alarmId = alarmService.addAlarmConfig(account, alarmConfig);
        return alarmId != null ? Result.ok().put("alarmId", alarmId) : Result
                .error("新增告警失败！");
    }

    /**
     * 编辑告警配置
     *
     * @return
     */
    @PutMapping(value = "/update")
    public Result updateAlarm(JwtAccount account, @RequestBody DeviceAlarmConfig deviceAlarmConfig) {
        deviceAlarmConfig.setDeviceSn(StringUtils.trimToNull(deviceAlarmConfig.getDeviceSn()));
        if (deviceAlarmConfig.getDeviceSn() != null && deviceService.view(deviceAlarmConfig.getDeviceModel(), deviceAlarmConfig.getDeviceSn()) == null) {
            return Result.error("设备sn不存在");
        }
        boolean success = alarmService.updateAlarmConfig(account, deviceAlarmConfig);
        return success ? Result.ok() : Result.error("告警配置更新失败");
    }

    /**
     * 删除告警配置
     *
     * @return
     */
    @DeleteMapping(value = "/delete/{alarmId}")
    public Result deleteAlarm(JwtAccount account, @PathVariable("alarmId") String alarmId) {

        boolean success = alarmService.deleteAlarmConfig(alarmId);
        return success ? Result.ok() : Result.error("删除告警失败！");
    }


    /**
     * 获取最新告警配置
     *
     * @return
     */
    @GetMapping(value = "/getAlarmCfg")
    public Result getAlarmCfgInfo(HttpServletResponse response) {
        response.addDateHeader("Last-Modified", AlarmServiceImpl.getLastModified());
        return Result.ok().put("guard", alarmService.getAlarmConfig(new HashMap<>()));
    }

}
