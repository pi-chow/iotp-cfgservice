package com.cetiti.iotp.cfgservice.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类。
 *
 * @author weiyinglei
 */
public class BaseDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date createTime;
	private Date modifyTime;
	private String createUser;
	private String modifyUser;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
}
