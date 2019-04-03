package com.cetiti.iotp.cfgservice;

import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.iotp.cfgservice.common.result.CfgResultCode;
import com.cetiti.iotp.cfgservice.common.zookeeper.CfgZkClient;
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
	private CfgZkClient cfgZkClient;

	@Before
	public void test(){
		String currentTime = String.valueOf(System.currentTimeMillis());
		try {
			if(!cfgZkClient.isConnect()){
				throw new BizLocaleException(CfgResultCode.ZOOKEEPER_ERROR);
			}
			if(cfgZkClient.exists(PATH) == null){
				cfgZkClient.createNode(PATH);
				cfgZkClient.setData(PATH, currentTime.getBytes());
			}else {
				cfgZkClient.setData(PATH,currentTime.getBytes());
			}
		} catch (KeeperException | InterruptedException exception) {
			exception.printStackTrace();
		}
	}

	@Test
	public void contextLoads() throws KeeperException, InterruptedException {

		System.out.println(cfgZkClient.getData(PATH));
	}

}
