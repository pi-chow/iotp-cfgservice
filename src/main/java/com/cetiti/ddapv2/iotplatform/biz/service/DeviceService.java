package com.cetiti.ddapv2.iotplatform.biz.service;

import com.cetiti.ddapv2.iotplatform.biz.domain.Device;
import com.cetiti.ddapv2.iotplatform.biz.domain.DeviceAttr;
import com.cetiti.ddapv2.iotplatform.biz.domain.SmartThingSnapshot;
import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 传感器服务。
 *
 * @author weiyinglei
 */
public interface DeviceService {

    int totalNum();

    List<Map<String, Object>> topTen();

    /**
     * 添加传感器。
     *
     * @param device
     * @return
     */
    int insert(JwtAccount account, Device device);

    /**
     * 添加传感器(外部调用)。
     *
     * @param device
     * @return
     */
    int insert(String userId, Device device);

    /**
     * 修改传感器。
     *
     * @param device
     * @return
     */
    boolean update(JwtAccount account, Device device);

    /**
     * 修改传感器。(外部调用)
     *
     * @param device
     * @return
     */
    boolean update(String userId, Device device);

    /**
     * 根据应用编号获取设备数据。
     *
     * @param applicationId 应用编号
     * @return
     */
    List<Map<String, Object>> statisticDeviceType(String applicationId);

    /**
     * 查看传感器。
     *
     * @param sn
     * @return
     */
    Device view(String deviceModel, String sn);

    /**
     * 传感器搜索。
     *
     * @param account token信息
     * @param sn 设备序列号
     * @param deviceModel 型号
     * @param applicationId 型号id
     * @param pageNum 页数
     * @param pageSize
     * @return
     */
    List<Device> search(JwtAccount account, String sn, String deviceModel, String applicationId, int pageNum, int pageSize, Integer deviceType);

    /**
     * 传感器搜索(供外部调用）。
     *
     * @param userId 用户id
     * @param sn 设备序列号
     * @param deviceModel 型号
     * @param applicationId 型号id
     * @param pageNum 页数
     * @param pageSize
     * @return
     */
    PageInfo<Device> search(String userId, String sn, String deviceModel, String applicationId, int pageNum, int pageSize);

    /**
     * 传感器搜索。
     *
     * @param sn 设备序列号
     * @param deviceModel 型号
     * @param applicationId 型号id
     * @return
     */
    List<Device> search(Long departmentId, String sn, String deviceModel, String applicationId, Integer deviceType);

    /**
     * 传感器搜索。
     *
     * @param account token信息
     * @param pageSize
     * @param condition
     * @return
     */
    List<Device> search(JwtAccount account, int pageNum, int pageSize, Map<String, Object> condition);
     /**
     * 获取所有设备
     *
     * @return
     */
    List<Device> allDevices(JwtAccount account);
    /**
     * 传感器搜索总数。
     *
     * @param map
     * @return
     */
    Integer searchCount(Map<String, Object> map);

    /**
     * 传感器地址。
     *
     * @param account token信息
     * @param type 输入类型
     * @return
     */
    List<Device> addr(JwtAccount account, int type, int pageNum, int pageSize, String input, List<String> deviceTypes);

    /**
     * 根据输入进行提示
     *
     * @param type    输入类型
     * @param account
     * @return
     */
    List<Map<String, String>> suggest(JwtAccount account, int type, String input);

    /**
     * 查看传感器属性。
     *
     * @param sn
     * @return
     */
    List<DeviceAttr> viewAttr(String sn);

    /**
     * 向传感器发送数据流。
     *
     * @param deviceId
     * @param dataStream
     * @return
     */
    public boolean sendData(JwtAccount account, String deviceModel, String deviceId, String dataStream);

    /**
     * 获取设备实时数据。
     *
     * @param deviceSn
     * @param deviceModel
     * @param attr
     * @return
     */
    Set<SmartThingSnapshot> getDeviceData(String deviceSn, String deviceModel, String attr);

    /**
     * 获取设备工单数据。
     *
     * @param sn
     * @param deviceModel
     * @return
     */
    Device deviceInfoForWorkSheet(String sn, String deviceModel);

    /**
     * 应用关联的传感器
     *
     * @return
     */
    List<Device> applicationLinkedDevices(Long departmentId, String sn, String deviceModel, String applicationId, Integer deviceType);

    /**
    * 强制下线设备。
    *
    * @param sn
    * @return boolean
    */
    boolean delete(JwtAccount account, String deviceModel, String sn);

    /**
     * 强制下线设备。(外部调用)
     *
     * @param sn
     * @return boolean
     */
    boolean delete(String userId, String deviceModel, String sn);

    /**
     * 设备批量上传
     *
     * @param fileName 文件名称
     *
     * @return boolean
     */
    Map<String, Object> batchImport(String fileName, MultipartFile file);
}
