package com.cetiti.iotpcfgservice.service;


import com.cetiti.iotpcfgservice.domain.ThingModelDef;

import java.io.File;

/**
 * 物模型构建处理器
 */
public interface ThingModelProcessor {

    /**
     * 模型校验
     */
    boolean validate(ThingModelDef message);

    /**
     * 模型构造
     */
    String constructor(ThingModelDef message);

    /**
     * 删除一个模型对应的文件。
     *
     * @param modelId
     */
    void deleteModelFile(String modelId);

    /**
     * 清理所有的输出物。
     */
     void clearAllTarget();

    /**
     * 打包所有的class。
     */
     int packageAll();

    /**
     * 得到模型对应的文件。
     */
    File getModelJar();
}
