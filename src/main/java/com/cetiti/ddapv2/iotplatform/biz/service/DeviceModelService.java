package com.cetiti.ddapv2.iotplatform.biz.service;

import com.cetiti.ddapv2.iotplatform.biz.domain.DeviceModel;
import com.cetiti.ddapv2.iotplatform.biz.domain.IdModelName;
import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;

import java.util.List;

/**
 * 传感器类型服务。
 *
 * @author yangshutian
 */
public interface DeviceModelService {

    /**
     * 分页获取传感器类型列表
     *
     * @param pageNum  页码
     * @param pageSize 每页多少
     * @return
     */
    public List<DeviceModel> deviceModelList(int pageNum, int pageSize);

    /**
     * 分页搜索传感器类型列表
     *
     * @param pageNum  页码
     * @param pageSize 每页多少
     * @return
     */
    public List<DeviceModel> search(String deviceModel, String deviceModelName, int pageNum, int pageSize);

    /**
     * 查看型号详情
     *
     * @param deviceModel 设备型号
     * @return
     */
    public DeviceModel view(String deviceModel);

    /**
     * 通过设备型号获取上报报文
     *
     * @param deviceModel 设备型号
     * @return
     */
    public int inpackagesByModel(String deviceModel);

    /**
     * 通过id获取传感器类型
     *
     * @param deviceModelId
     * @return
     */
    public DeviceModel deviceModelById(String deviceModelId);

    /**
     * 通过Model和name获取传感器类型
     *
     * @param Model
     * @return
     */
    public DeviceModel deviceModelByModel(String Model);

    /**
     * 获取传感器类型编号列表
     *
     * @return
     */
    public List<IdModelName> deviceModelIdList();

    /**
     * 通过应用编号获取传感器类型编号列表
     *
     * @return
     */
    public List<IdModelName> labvalByAppId(String applicationId);

    /**
     * 通过应用编号传感器型号列表
     *
     * @return
     */
    public List<DeviceModel> listDeviceModelAll();

    /**
     * 新增传感器类型
     *
     * @param deviceModel 传感器类型
     * @return
     */
    public String deviceModelAdd(JwtAccount account, DeviceModel deviceModel);

    /**
     * 更新传感器类型
     *
     * @param deviceModel 更新传感器类型类
     * @return
     */
    public boolean deviceModelUpdate(JwtAccount account, DeviceModel deviceModel);

    /**
     * 删除传感器类型
     *
     * @param deviceModel 传感器类型
     * @return
     */
    public boolean deviceModelDelete(String deviceModel);

    /**
     * 通过型号获取型号名称
     *
     * @param deviceModel
     * @return
     */
    public String deviceModelNameByDeviceModel(String deviceModel);

    /**
     * 通过型号获取型号id
     *
     * @param deviceModel
     * @return
     */
    public String deviceModelIdByDeviceModel(String deviceModel);

    /**
     * 暴露给配置服务获取传感器类型列表
     *
     * @param
     * @return
     */
    public List<DeviceModel>  listDeviceModel();

}
