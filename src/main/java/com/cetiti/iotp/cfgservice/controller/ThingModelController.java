package com.cetiti.iotp.cfgservice.controller;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.ddapv2.iotplatform.common.tip.BaseTip;
import com.cetiti.ddapv2.iotplatform.common.tip.ErrorTip;
import com.cetiti.ddapv2.iotplatform.common.tip.SuccessTip;
import com.cetiti.iotp.cfgservice.common.result.CfgResultCode;
import com.cetiti.iotp.cfgservice.domain.ThingModelDef;
import com.cetiti.iotp.cfgservice.service.ThingModelProcessor;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/modelFlow")
@Api("模型接口相关API")
public class ThingModelController {

	private static final Logger logger = LoggerFactory.getLogger(ThingModelController.class);

	@Autowired
	private ThingModelService modelService;

	@Autowired
	private ThingModelProcessor modelProcessor;

	/**
	 * 根据设备编号获取模型列表
	 */
	@ApiOperation(value = "根据设备编号获取模型列表")
	@GetMapping(value = "/list/{deviceModelId}")
	public BaseTip modelList(@PathVariable("deviceModelId") String deviceId) {
		List<ThingModelDef> modelList = modelService.modelListByDeviceModelId(deviceId);
		return new SuccessTip<>(modelList);
	}

	/**
	 * 根据模型编号获取模型
	 * @param modelDefId
	 *
	 * @return
	 */
	@ApiOperation(value = "根据模型编号获取模型")
	@GetMapping(value = "/{thingModelId}")
	public BaseTip modelViewById(@PathVariable("thingModelId") String modelDefId) {
		ThingModelDef model = modelService.modelViewById(modelDefId);
		return new SuccessTip<>(model);
	}

	/**
	 * 添加模型
	 * @param account
	 * @param model
	 *
	 * @return
	 */
	@ApiOperation(value = "添加物模型")
	@PostMapping(value = "/add")
	public BaseTip modelAdd(JwtAccount account, @RequestBody ThingModelDef model) {
		if (StringUtils.isEmpty(model.getDeviceModelId())) {
			return new ErrorTip(CfgResultCode.DEVICE_MODEL_EMPTY);
		}
		if (StringUtils.isEmpty(model.getStoreTypes())) {
			return new ErrorTip(CfgResultCode.THING_MODEL_STORE_TYPE_EMPTY);
		}
		if (StringUtils.isEmpty(model.getStructType())) {
			return new ErrorTip(CfgResultCode.THING_MODEL_STRUCT_TYPE_EMPTY);
		}
		String modelId = modelService.addModel(model, account);
		return modelId != null ? new SuccessTip(modelId) :new ErrorTip(CfgResultCode.THING_MODEL_ADD);
	}

	/**
	 * 添加物模型,从一个已知的物模型添加.
	 * @param account
	 * @param modelTemplateId
	 *            已知的物模型
	 * 
	 * @return
	 */
	@ApiOperation(value = "从模板添加物模型")
	@GetMapping(value = "/addFromTemplate")
	public BaseTip modelAddFromTemplate(JwtAccount account,
			@RequestParam("templateId") String modelTemplateId,
			@RequestParam("deviceModelId") String deviceModelId) {
		Preconditions.checkArgument(StringUtils.isNotBlank(modelTemplateId));
		Preconditions.checkArgument(StringUtils.isNotBlank(deviceModelId));

		try {
			modelService.addModelFromTemplate(modelTemplateId, deviceModelId, account);
		}catch (BizLocaleException e){
			return new ErrorTip(e.getErrorCode());
		}
		return new SuccessTip("模板创建成功");
	}

	/**
	 * 获取消息类型
	 *
	 * @return
	 */
	@ApiOperation(value = "获取消息结构类型")
	@GetMapping(value = "/get/structType")
	public BaseTip strutType() {
		return new SuccessTip<>(modelService.strutType());
	}

	/**
	 * 获取消息类型
	 *
	 * @return
	 */
	@ApiOperation(value = "获取消息存储类型")
	@GetMapping(value = "/get/storeType")
	public BaseTip storeType() {
		return new SuccessTip<>(modelService.storeTypes());
	}
	
	/**
	 * 获取结构体类型
	 *
	 * @return
	 */
	@ApiOperation(value = "获取消息存储类型")
	@GetMapping(value = "/get/strutType")
	public BaseTip userDefStrutType() {
		return new SuccessTip<>(modelService.storeTypes());
	}

	/**
	 * 获取消息类型
	 *
	 * @return
	 */
	@ApiOperation(value = "获取消息存储类型")
	@GetMapping(value = "/get/availableStoreType/{dataType}")
	public BaseTip availableStoreType(@PathVariable("dataType") String dataType) {
		return new SuccessTip<>(modelService.getUsableStoreType(dataType));
	}

	/**
	 * 得到物模型模板列表.
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GetMapping(value = "/template/get")
	public BaseTip templateModelView(JwtAccount account,
									@RequestParam(required = false) String deviceModel,
                                    @RequestParam(required = false) String deviceModelName,
                                    @RequestParam(required = false, defaultValue = "1") int pageNum,
                                    @RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageInfo<ThingModelDef> pageInfo = modelService.templateModelView(account, deviceModel, deviceModelName, pageNum, pageSize);

		Map<String, Object> result = new HashMap<>();
		result.put("total", pageInfo.getTotal());
		result.put("templateList", pageInfo.getList());

		return new SuccessTip<>(result);
	}
	
	/**
	 * 已经的设备模型，作为模板。
	 * @param account
	 * 
	 * @return
	 */
	@GetMapping(value = "/template/add/{deviceModelId}")
	public BaseTip templateModelAdd(JwtAccount account, @PathVariable("deviceModelId") String deviceModelId) {
		modelService.makeTemplate(deviceModelId, account);
		return new SuccessTip("设备模型作为模板成功");
	}

	/**
	 * 删除一个模板
	 * 
	 * @param templateId 模板id
	 * @return
	 */
	@DeleteMapping(value = "/template/del/{templateId}")
	public BaseTip templateModelDelete(@PathVariable("templateId") String templateId) {
		boolean success = modelService.deleteTemplate(templateId);
		return success ? new SuccessTip("删除模板成功") : new ErrorTip(CfgResultCode.THING_MODEL_TEMPLATE_DELETE);
	}

	/**
	 * 更新模型
	 *
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "更新模型")
	@PutMapping(value = "/update")
	public BaseTip modelUpdate(JwtAccount account, @RequestBody ThingModelDef model) {
		if (StringUtils.isEmpty(model.getDeviceModelId())) {
			return new ErrorTip(CfgResultCode.DEVICE_MODEL_EMPTY);
		}
		if (StringUtils.isEmpty(model.getStoreTypes())) {
			return new ErrorTip(CfgResultCode.THING_MODEL_STORE_TYPE_EMPTY);
		}
		if (StringUtils.isEmpty(model.getStructType())) {
			return new ErrorTip(CfgResultCode.THING_MODEL_STRUCT_TYPE_EMPTY);
		}

		boolean success = modelService.updateModel(account, model);
		return success ? new SuccessTip("更新模型成功") : new ErrorTip(CfgResultCode.THING_MODEL_UPDATE);
	}

	/**
	 * 删除模型

	 */
	@ApiOperation(value = "删除模型")
	@DeleteMapping(value = "/delete/{thingModelId}")
	public BaseTip modelDelete(@PathVariable("thingModelId") String modelId) {
		modelProcessor.deleteModelFile(modelId);
		boolean success = modelService.deleteModel(modelId);
		return success ? new SuccessTip("删除模型成功") : new ErrorTip(CfgResultCode.THING_MODEL_DELETE);
	}

	/**
	 * 发布所有已知的模型。
	 * @param account
	 */
	@ApiOperation(value = "发布所有已经配置的模型")
	@GetMapping(value = "/publish")
	public BaseTip modelPublish(JwtAccount account) {
		List<ThingModelDef> failure = new ArrayList<>();
		failure = modelService.allModelPublish(account);

		if (failure == null || failure.size() == 0) {
			return new SuccessTip("所有模型发布成功");
		}

		return new ErrorTip(CfgResultCode.THING_MODEL_PUBLISH.getResultCode(),
				"以下型号的模型发布失败[" + failure.stream().map(e -> {
						return e.getDeviceModel();
		}).collect(Collectors.joining(",")) + "]");
	}

	/**
	 * 输出已经打包好的jar。
	 */
	@ApiOperation(value = "输出已经打包好的jar")
	@GetMapping(value = "/thingModelDefinition.jar")
	public void modelJar(HttpServletResponse response) {
		File file = modelProcessor.getModelJar();

		if (!file.exists()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return;
		}

		response.setContentLength((int) file.length());
		response.setContentType("application/zip");
		response.addDateHeader("Last-Modified", file.lastModified());

		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			outputStream.write(FileUtils.readFileToByteArray(file));
			outputStream.flush();
		} catch (IOException e) {
			throw new BizLocaleException(CfgResultCode.THING_JAR_NOT_FOUND);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

}
