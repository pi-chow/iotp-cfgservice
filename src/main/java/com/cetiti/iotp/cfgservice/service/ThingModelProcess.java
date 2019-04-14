package com.cetiti.iotp.cfgservice.service;

import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;
import com.cetiti.iotp.cfgservice.domain.vo.ThingModel;

import java.io.File;
import java.util.List;


/**
 * 设备协议模型构建器
 * @author zhouliyu
 * @since 2019-04-02 16:17:08
 */
public interface ThingModelProcess {

    /**
     * 类构造器.java
     * @param thingModel 设备协议模型
     * */
    String classConstructor(ThingModel thingModel);

    /**
     * 类编译器.class
     * @param javaSourceFiles 模型.java地址
     * */
    boolean classCompiler(List<String> javaSourceFiles);

    /**
     * 类加载器 jar
     * */
    boolean classLoader();

    /***
     * 删除打包失败的文件
     * @param thingModelHeader 设备型号头信息
     * */
    void delFailureThingModel(ThingModelHeader thingModelHeader);

    /**
     * 获取thing-model.jar
     * */
    File getThingModelJar();

}
