package com.cetiti.iotpcfgservice.controller;

import com.cetiti.ddapv2.iotplatform.common.domain.vo.JwtAccount;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.iotpcfgservice.common.result.Result;
import com.cetiti.iotpcfgservice.domain.ThingModelDef;
import com.cetiti.iotpcfgservice.service.ThingModelProcessor;
import com.cetiti.iotpcfgservice.service.ThingModelService;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/modelFlow")
@Api("模型接口相关API")
public class ThingModelController {

	private static final Logger logger = LoggerFactory.getLogger(ThingModelController.class);

	@Autowired
	private ThingModelService modelService;

	@Autowired
	private ThingModelProcessor modelProcessor = null;

	/**
	 * 根据设备编号获取模型列表
	 * @param account TODO
	 */
	@ApiOperation(value = "根据设备编号获取模型列表")
	@GetMapping(value = "/list/{deviceModelId}")
	public Result modelList(JwtAccount account, @PathVariable("deviceModelId") String deviceId) {
		List<ThingModelDef> modelList = modelService.modelListByDeviceModelId(deviceId, account);
		return Result.ok().put("modelList", modelList);
	}

	/**
	 * 根据模型编号获取模型
	 * @param account TODO
	 * @param modelDefId
	 *
	 * @return
	 */
	@ApiOperation(value = "根据模型编号获取模型")
	@GetMapping(value = "/{thingModelId}")
	public Result modelViewById(JwtAccount account, @PathVariable("thingModelId") String modelDefId) {
		ThingModelDef model = modelService.modelViewById(modelDefId, account);
		return Result.ok().put("model", model);
	}

	/**
	 * 添加模型
	 * @param account TODO
	 * @param model
	 *
	 * @return
	 */
	@ApiOperation(value = "添加物模型")
	@PostMapping(value = "/add")
	public Result modelAdd(JwtAccount account, @RequestBody ThingModelDef model) {
		if (StringUtils.isEmpty(model.getDeviceModelId())) {
			return Result.error("设备型号ID为空");
		}
		if (StringUtils.isEmpty(model.getStoreTypes())) {
			return Result.error("存储类型为空");
		}
		if (StringUtils.isEmpty(model.getStructType())) {
			return Result.error("设备结构类型为空");
		}
		String modelId = modelService.addModel(model, account);
		return modelId != null ? Result.ok().put("modelId", modelId) : Result.error("新增模型失败，得不到设备模型ID。");
	}

	/**
	 * 添加物模型,从一个已知的物模型添加.
	 * @param account TODO
	 * @param modelTemplateId
	 *            已知的物模型
	 * 
	 * @return
	 */
	@ApiOperation(value = "从模板添加物模型")
	@GetMapping(value = "/addFromTemplate")
	public Result modelAddFromTemplate(JwtAccount account,
			@RequestParam("templateId") String modelTemplateId, @RequestParam("deviceModelId") String deviceModelId) {
		Preconditions.checkArgument(StringUtils.isNotBlank(modelTemplateId));
		Preconditions.checkArgument(StringUtils.isNotBlank(deviceModelId));

		modelService.addModelFromTemplate(modelTemplateId, deviceModelId, account);
		return Result.ok("模板创建成功。");
	}

	/**
	 * 获取消息类型
	 *
	 * @return
	 */
	@ApiOperation(value = "获取消息结构类型")
	@GetMapping(value = "/get/structType")
	public Result strutType() {
		return Result.ok().put("structTypeList", modelService.strutType());
	}

	/**
	 * 获取消息类型
	 *
	 * @return
	 */
	@ApiOperation(value = "获取消息存储类型")
	@GetMapping(value = "/get/storeType")
	public Result storeType() {
		return Result.ok().put("structTypeList", modelService.storeTypes());
	}
	
	/**
	 * 获取结构体类型
	 *
	 * @return
	 */
	@ApiOperation(value = "获取消息存储类型")
	@GetMapping(value = "/get/strutType")
	public Result userDefStrutType() {
		return Result.ok().put("structTypeList", modelService.storeTypes());
	}

	/**
	 * 获取消息类型
	 *
	 * @return
	 */
	@ApiOperation(value = "获取消息存储类型")
	@GetMapping(value = "/get/availableStoreType/{dataType}")
	public Result availableStoreType(@PathVariable("dataType") String dataType) {
		return Result.ok().put("availableStoreTypeList", modelService.getUsableStoreType(dataType));
	}

	/**
	 * 得到物模型模板列表.
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GetMapping(value = "/template/get")
	public Result templateModelView(JwtAccount account,
									@RequestParam(required = false) String deviceModel,
                                    @RequestParam(required = false) String deviceModelName,
                                    @RequestParam(required = false, defaultValue = "1") int pageNum,
                                    @RequestParam(required = false, defaultValue = "10") int pageSize) {
		PageInfo<ThingModelDef> pageInfo = modelService.templateModelView(account, deviceModel, deviceModelName, pageNum, pageSize);

		return Result.ok().put("total", pageInfo.getTotal()).put("lst", pageInfo.getList());
	}
	
	/**
	 * 已经的设备模型，作为模板。
	 * @param account
	 * 
	 * @return
	 */
	@GetMapping(value = "/template/add/{deviceModelId}")
	public Result templateModelAddd(JwtAccount account, @PathVariable("deviceModelId") String deviceModelId) {
		modelService.makeTemplate(deviceModelId, account);

		return Result.ok("设备模型作为模板成功。");
	}

	/**
	 * 删除一个模板。templateId
	 * 
	 * @param templateId
	 * @return
	 */
	@DeleteMapping(value = "/template/del/{templateId}")
	public Result templateModelDelete(JwtAccount account, @PathVariable("templateId") String templateId) {
		boolean success = modelService.deleteTemplate(account, templateId);
		return success ? Result.ok("删除模板成功") : Result.error("删除模板失败");

	}

	/**
	 * 更新模型
	 *
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "更新模型")
	@PutMapping(value = "/update")
	public Result modelUpdate(JwtAccount account, @RequestBody ThingModelDef model) {
		if (StringUtils.isEmpty(model.getDeviceModelId())) {
			return Result.error("设备型号ID为空");
		}
		if (StringUtils.isEmpty(model.getStoreTypes())) {
			return Result.error("存储类型为空");
		}
		if (StringUtils.isEmpty(model.getStructType())) {
			return Result.error("设备结构类型为空");
		}
		// if (model.getFields() == null || model.getFields().size() == 0) {
		// return Result.error("需要配置模型包含的字段/属性。");
		// }

		boolean success = modelService.updateModel(account, model);
		return success ? Result.ok("更新模型成功") : Result.error("更新模型失败");
	}

	/**
	 * 删除模型
	 * @param account TODO
	 */
	@ApiOperation(value = "删除模型")
	@DeleteMapping(value = "/delete/{thingModelId}")
	public Result modelDelete(JwtAccount account, @PathVariable("thingModelId") String modelId) {
		modelProcessor.deleteModelFile(modelId);
		boolean success = modelService.deleteModel(account, modelId);
		return success ? Result.ok("删除模型成功") : Result.error("删除模型失败");
	}

	/**
	 * 发布所有已知的模型。
	 * @param account TODO
	 */
	@ApiOperation(value = "发布所有已经配置的模型")
	@GetMapping(value = "/publish")
	public Result modelPublish(JwtAccount account) {
		List<ThingModelDef> failure = Lists.newArrayList();
		try {
			failure = modelService.allModelPublish(account);
		} catch (BizLocaleException exception) {
			return Result.error(exception.getErrorCode());
		}

		if (failure == null || failure.size() == 0) {
			return Result.ok("所有模型发布成功");
		}

		return Result.error("以下型号的模型发布失败[" + failure.stream().map(e -> {
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
			logger.error("Get jar fail.", e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

}
