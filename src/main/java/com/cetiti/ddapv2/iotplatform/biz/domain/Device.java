package com.cetiti.ddapv2.iotplatform.biz.domain;

import com.cetiti.iotpcfgservice.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * 传感器实体类。
 * 
 * @author weiyinglei
 *
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Device extends BaseDomain implements Serializable {

	private static final long serialVersionUID = -678723426662595619L;
	/**
	 * 自增id。
	 */
	private Long id;
	/**
	 * 传感器id。
	 */
	private String deviceId;

	/**
	 * 传感器图片编号。
	 */
	private int imageNum;
	/**
	 * 序列号。
	 */
	private String sn;
	/**
	 * 名称。
	 */
	private String name;
	/**
	 * 网关
	 */
	private String gatewayId;

	/**
	 * MAC
	 */
	private String mac;

	/**
	 * 设备类型id
	 */
	private String deviceModelId;
	/**
	 * 生产商
	 */
	private String manufacturer;
	/**
	 * 设备类型
	 */
	private Integer deviceType;
	/**
	 * 协议类型
	 */
	private String protocolType;
	/**
	 * 传感器型号。
	 */
	private String deviceModel;
	/**
	 * 传感器类型名称。
	 */
	private String deviceModelName;
	/**
	 * 
	 * 应用id。
	 */
	private String applicationId;
	/**
	 * 设备地址
	 */
	private String deviceAddr;

	/**
	 * 省
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;

	/**
	 * 区县
	 */

	private String district;

	/**
	 * 地址
	 */
	private String detailAddress;

	/**
	 * 行政区域编码
	 */
	private String cityCode;

	/**
	 * 行政区域编码
	 */
	private boolean online;

	/**
	 * 部门编号

	 */
	private Long departmentId;

	/**
	 * 部门名称

	 */
	private String departmentName;

	/**
	 * 设备状态
	 */
	private int status;
	
	/**
     * 设备通信协议
     */
    private String protocol;
    
    /**
     * 设备连接地址
     */
    private String connInfo;
    
    /**
     * 设备连接世界
     */
    private String connTime;
    
    /**
     * 设备最后一次数据上报时间
     */
    private String lastUploadTime;
    
    /**
     * 设备连接实例id
     */
    private String instanceId;
	/**
	 * 设备退役时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date retireTime;

	/**
	 * 设备退役提醒时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date warnTime;
    
    private String deviceGroupId;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getDeviceModelId() {
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId) {
		this.deviceModelId = deviceModelId;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getDeviceModelName() {
		return deviceModelName;
	}

	public void setDeviceModelName(String deviceModelName) {
		this.deviceModelName = deviceModelName;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public int getImageNum() {
		return imageNum;
	}

	public void setImageNum(int imageNum) {
		this.imageNum = imageNum;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceAddr() {
		return deviceAddr;
	}

	public void setDeviceAddr(String deviceAddr) {
		this.deviceAddr = deviceAddr;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getConnInfo() {
        return connInfo;
    }

    public void setConnInfo(String connInfo) {
        this.connInfo = connInfo;
    }

    public String getConnTime() {
        return connTime;
    }

    public void setConnTime(String connTime) {
        this.connTime = connTime;
    }

    public String getLastUploadTime() {
        return lastUploadTime;
    }

    public void setLastUploadTime(String lastUploadTime) {
        this.lastUploadTime = lastUploadTime;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Date getRetireTime() {
		return retireTime;
	}

	public void setRetireTime(Date retireTime) {
		this.retireTime = retireTime;
	}

	public Date getWarnTime() {
		return warnTime;
	}

	public void setWarnTime(Date warnTime) {
		this.warnTime = warnTime;
	}
	
    public String getDeviceGroupId() {
		return deviceGroupId;
	}

	public void setDeviceGroupId(String deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Override
	public String toString() {
		return "Device [id=" + id + ", deviceId=" + deviceId + ", imageNum="
				+ imageNum + ", sn=" + sn + ", name=" + name + ", gatewayId="
				+ gatewayId + ", mac=" + mac + ", deviceModelId="
				+ deviceModelId + ", manufacturer=" + manufacturer
				+ ", deviceType=" + deviceType + ", protocolType="
				+ protocolType + ", deviceModel=" + deviceModel
				+ ", deviceModelName=" + deviceModelName + ", applicationId="
				+ applicationId + ", deviceAddr=" + deviceAddr + ", province="
				+ province + ", city=" + city + ", district=" + district
				+ ", detailAddress=" + detailAddress + ", cityCode=" + cityCode
				+ ", online=" + online + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Device device = (Device) o;

		if (sn != null ? !sn.equals(device.sn) : device.sn != null) return false;
		return deviceModel != null ? deviceModel.equals(device.deviceModel) : device.deviceModel == null;
	}

	@Override
	public int hashCode() {
		int result = sn != null ? sn.hashCode() : 0;
		result = 31 * result + (deviceModel != null ? deviceModel.hashCode() : 0);
		return result;
	}
}
