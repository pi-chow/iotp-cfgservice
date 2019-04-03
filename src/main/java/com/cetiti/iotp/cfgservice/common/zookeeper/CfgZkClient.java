package com.cetiti.iotp.cfgservice.common.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CfgZkClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CfgZkClient.class);
    private ZooKeeper zooKeeper;
    private static final int DEFAULT_TIME_OUT = 2000;

    @Value("${iotp.cfg.zkClient.watcher.path}")
    private String PATH;
    @Value("${iotp.cfg.zkClient.address}")
    private String address;

    @PostConstruct
    public void init(){
        if(zooKeeper == null){
            try {
                zooKeeper = new ZooKeeper(address, DEFAULT_TIME_OUT, new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
                            try {
                                if(exists(PATH) == null){
                                    createNode(PATH);
                                }
                            }catch (KeeperException | InterruptedException exception) {
                                LOGGER.error("zookeeper error: " + exception);
                            }
                        }
                    }
                });
            } catch (IOException e) {
                LOGGER.error("zooKeeper create error" + e);
            }
        }
    }


    /**
     * 创建多级节点
     * */
    public void createNode(String path) throws KeeperException, InterruptedException {

        List<String> nodeList = Arrays.asList(path.split("/"));
        nodeList = nodeList.stream().filter(e -> !e.equals("")).collect(Collectors.toList());
        String nodeTemp = "/";
        for (String node : nodeList){
            nodeTemp  = nodeTemp + node ;
            if(zooKeeper.exists(nodeTemp, false)  == null){
                zooKeeper.create(nodeTemp,null,ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                nodeTemp = nodeTemp + "/";
            }else {
                nodeTemp = nodeTemp + "/";
            }
        }

    }



    /**
     * 创建节点
     * */
    public String createNode(String path, byte[] data) throws KeeperException, InterruptedException {
        return this.zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

    }

    /**
     * 删除节点
     * */
    public void deleteNode(String path) throws KeeperException, InterruptedException {
        zooKeeper.delete(path, -1);
    }

    /**
     * 获取节点
     *
     * */
    public Stat exists(String path) throws KeeperException, InterruptedException {
        return zooKeeper.exists(path, false);
    }

    /**
     * 获取节点数据
     * */
    public byte[] getData(String path) throws KeeperException, InterruptedException {
        return zooKeeper.getData(path, false, null);
    }

    /**
     * 更新节点数据
     * */
    public Stat setData(String path, byte[] data) throws KeeperException, InterruptedException {
        return zooKeeper.setData(path, data, -1);

    }

    /**
     * 关闭连接
     * */
    public void closeConnection() throws InterruptedException {
        if(zooKeeper != null){
            zooKeeper.close();
        }
    }

}
