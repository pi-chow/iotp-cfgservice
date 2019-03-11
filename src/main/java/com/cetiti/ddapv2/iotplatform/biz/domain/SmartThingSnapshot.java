/**
 * @SmartThingSnapshot.java  COPYRIGHT xiaojian
 * 
 * Created on 2018年12月4日 下午3:07:48
 */
package com.cetiti.ddapv2.iotplatform.biz.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 快照数据。
 * 
 * @author <a href="mailto:cxj2000@gmail.com">xiaojian.cao</a>
 *
 */
public class SmartThingSnapshot implements Serializable {

	private static final long serialVersionUID = 6012270610896314651L;

	public String attr;
	
	public String value;
	
	public Date uploadTime;
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
