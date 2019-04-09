/**   
 * @Title: SqlGenerator.java 
 * @Package com.cetiti.ddapv2.iotplatform.common.utils 
 * @Description: Generator sql from thingmodel 
 * @author xj7036
 * @date 2019年1月17日 上午11:39:36 
 * @version V1.0   
 */
package com.cetiti.iotp.cfgservice.common.utils;

import com.cetiti.iotp.cfgservice.domain.ThingModelDef;
import com.cetiti.iotp.itf.cfgservice.vo.ThingModelField;
import com.google.common.base.Preconditions;

import java.util.List;

public class SqlGenerator {

	public static String createTable(ThingModelDef thingModel) {
		Preconditions.checkArgument(thingModel != null);
		Preconditions.checkArgument(thingModel.getThingModelType() != null, "模型类型不能为空！");
		Preconditions.checkArgument(thingModel.getDeviceModel() != null, "设备型号不能为空！");
		Preconditions.checkArgument(thingModel.getFields() != null, "字段不能为空！");

		String thingModelType = thingModel.getThingModelType();
		String deviceModel = thingModel.getDeviceModel();
		String tableName = "t_" + thingModelType + "_" + deviceModel;

		List<ThingModelField> fieldList = thingModel.getFields();

		Object param = null;
		String column = null;
		StringBuilder sb = null;
		String sql = "";
		sb = new StringBuilder(200);

		if (thingModel.getStoreTypes().contains("HBASE")) {
			sb.append("HBASE: create '" + tableName + "', { NAME => 'INFO'};\r\n");
		}
		if (thingModel.getStoreTypes().contains("MYSQL")) {
			sb.append("MYSQL: create table ").append(tableName).append(" ( \r\n");
			System.out.println(tableName);
			sb.append(" `id` bigint(20) NOT NULL AUTO_INCREMENT, \r\n");
			sb.append(" `device_sn` varchar(50) DEFAULT NULL COMMENT '设备序列号', \r\n");
			sb.append(" `device_model` varchar(50) DEFAULT NULL COMMENT '设备型号', \r\n");
			sb.append(" `uploadTime` datetime DEFAULT NULL COMMENT '数据上报时间', \r\n");
			if (thingModel.getThingModelType().equals("event")) {
				sb.append(" `event` varchar(255) DEFAULT NULL COMMENT '事件名称', \r\n");
			}
			for (ThingModelField f : fieldList) {
				column = f.getName();
				sb.append(" `").append(column).append("` ");
				System.out.println(column + "," + f.getCustomType());
				param = f.getCustomType();
				if (param.equals("int")) {
					sb.append(" int(50) ");
				} else if (param.equals("float")) {
					sb.append(" float(50,2) ");
				} else if (param.equals("varstring") || param.equals("string")) {
					sb.append(" varchar(50) ");
				}

				sb.append(" DEFAULT NULL COMMENT '");
				param = f.getLabel();
				System.out.println("属性[" + f.getName() + "]-----的注释类型有: " + param);
				sb.append(param).append("',\r\n");
			}
			sb.append(" PRIMARY KEY (`id`) \r\n");
			sb.append(  " )ENGINE =INNODB DEFAULT  CHARSET= utf8;\r\n");
		}
		sql = sb.toString();
		return sql;
	}

}