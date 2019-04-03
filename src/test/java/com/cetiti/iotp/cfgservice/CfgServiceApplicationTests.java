package com.cetiti.iotp.cfgservice;

import com.cetiti.ddapv2.iotplatform.common.ThingModel;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.iotp.cfgservice.common.result.CfgResultCode;
import com.cetiti.iotp.cfgservice.common.zookeeper.CfgZkClient;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import org.apache.zookeeper.KeeperException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CfgServiceApplicationTests {

	private String PATH = "/iotp/cfg/thingmodel/publish/time";

	@Autowired
	private ThingModelService thingModelService;

	@Autowired
	private CfgZkClient cfgZkClient;

	@Before
	public void test(){
		thingModelService.allModelPublish(null);
	}

	@Test
	public void contextLoads() throws KeeperException, InterruptedException {
		//cfgZkClient.deleteNode(PATH);
		System.out.println(cfgZkClient.getData(PATH));
	}

}
