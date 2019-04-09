package com.cetiti.iotp.cfgservice;

import com.cetiti.iotp.cfgservice.common.zookeeper.CfgZkClient;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import org.apache.zookeeper.KeeperException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CfgServiceApplicationTests {

	@Value("${iotp.cfg.zkClient.watcher.paths}")
	private String paths;

	@Autowired
	private ThingModelService thingModelService;

	@Autowired
	private CfgZkClient cfgZkClient;

	@Before
	public void test(){
		updateNodeData();
	}

	@Test
	public void contextLoads() throws KeeperException, InterruptedException {
		//cfgZkClient.deleteNode(PATH);
		System.out.println(cfgZkClient.getData(paths.split(",")[0]));
	}

	/**
	 * zookeeper 更新publish/time节点
	 * */
	private void updateNodeData() {
		String currentTime = String.valueOf(System.currentTimeMillis());
		try {
			cfgZkClient.setData(paths.split(",")[1],currentTime.getBytes());

		} catch (KeeperException | InterruptedException exception) {
			exception.printStackTrace();
		}
	}


}
