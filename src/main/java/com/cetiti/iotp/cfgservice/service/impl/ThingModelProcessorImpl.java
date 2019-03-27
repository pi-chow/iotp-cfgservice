package com.cetiti.iotp.cfgservice.service.impl;

import com.cetiti.ddapv2.iotplatform.common.*;
import com.cetiti.ddapv2.iotplatform.common.exception.BizLocaleException;
import com.cetiti.iotp.cfgservice.service.ThingModelProcessor;
import com.cetiti.iotp.itf.cfgservice.vo.ThingModelField;
import com.cetiti.iotp.cfgservice.common.result.CfgResultCode;
import com.cetiti.iotp.cfgservice.domain.ThingModelDef;
import com.cetiti.iotp.cfgservice.enums.CustomTypeEnum;
import com.cetiti.iotp.cfgservice.mapper.ThingModelDefMapper;
import com.cetiti.iotp.itf.platformservice.DeviceModelService;
import com.cetiti.iotp.itf.platformservice.vo.DeviceModel;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.squareup.javapoet.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaojian.toolkit.bean.bytebean.annotation.ByteBeanField;

import javax.annotation.PostConstruct;
import javax.lang.model.element.Modifier;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息编译(编译期生成代码) 1.生成.java 2.生成.class 3.打包生成.jar
 *
 * @author zhouliyu
 */
@Service
public class ThingModelProcessorImpl implements ThingModelProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThingModelProcessorImpl.class);

	@Reference
	private DeviceModelService deviceModelService;

	@Autowired
	private ThingModelDefMapper thingModelMapper;

	private final String targetDirectory = System.getProperty("user.dir") + File.separator;

	private final String buildPath = "build-libs";

	private final String jarName = "thing-model.jar";

	private String classpath;

	/**
	 * 物模型类名前缀。
	 * 
	 */
	private static final String CLASSNAME = "$TM_%s_%s";


	@PostConstruct
	private void init(){
        StringBuilder sb = new StringBuilder();
        File[] files = new File(targetDirectory + buildPath).listFiles();
        if (files != null) {
            for (File file : files) {
                sb.append(file.getAbsolutePath()).append(File.pathSeparator);
            }
            this.classpath = sb.toString();
        }
    }

	/**
	 * 消息校验
	 */
	@Override
	public boolean validate(ThingModelDef model) {
		if (model == null) {
			return false;
		}

		if (StringUtils.isBlank(model.getDeviceModel())) {
			return false;
		}

		if (StringUtils.isBlank(model.getDeviceModelId())) {
			return false;
		}

		if (StringUtils.isBlank(model.getName())) {
			return false;
		}

		if (ThingDataTypeEnum.check(model.getThingModelType())) {
			return false;
		}

		if (ThingDataStrutTypeEnum.check(model.getStructType())) {
			return false;
		}
		return true;
	}

	/**
	 * 删除一个模型文件 java/class。
	 */
	@Override
	public void deleteModelFile(String modelId) {
		Preconditions.checkArgument(modelId != null);

		ThingModelDef thingModel = thingModelMapper.modelViewById(modelId);
		if (thingModel == null) {
			LOGGER.warn("Cannot found thingModel with id[{}].", modelId);
			return;
		}

        deleteModelFile(thingModel);

	}

	/**
	 * 删除一个模型文件。
	 *
	 * @param thingModel
	 */
	private void deleteModelFile(ThingModelDef thingModel) {
		Preconditions.checkArgument(thingModel != null);

		String packageName = ThingDefinitionConstants.MODEL_DEF_PACKAGE + "." + thingModel.getThingModelType();
		String javaOrClassName = String.format(CLASSNAME, thingModel.getDeviceModel(), thingModel.getName());

		File dir = new File(targetDirectory + packageName.replace(".", File.separator));

		File javaFile = new File(dir, javaOrClassName + ".java");
		if (javaFile.exists()) {
			LOGGER.debug("Delete model java file[{}].", javaFile);
			javaFile.delete();
		} else {
			LOGGER.warn("Cannot found model java file[{}].", javaFile);
		}

		File classFile = new File(dir, javaOrClassName + ".class");
		if (classFile.exists()) {
			LOGGER.debug("Delete model class file[{}].", classFile);
			classFile.delete();
		} else {
			LOGGER.warn("Cannot found model class file[{}].", classFile);
		}
	}

	/**
	 * 清理所有的输出物。
	 */
	@Override
	public void clearAllTarget() {
		String sourceTargetDir = targetDirectory
				+ ThingDefinitionConstants.MODEL_DEF_PACKAGE.replace(".", File.separator);
		LOGGER.info("Clear all file with dir[{}].", sourceTargetDir);

		FileUtils.deleteQuietly(new File(sourceTargetDir));

		String jarTarget = targetDirectory + jarName;
		LOGGER.info("Delete jar[{}].", jarTarget);
		FileUtils.deleteQuietly(new File(jarTarget));
	}

	// ------------ start constructor -------------------

	/**
	 * 根据物模型构造java文件，并且编译文件。
	 *
	 * @param model
	 *            物模型。
	 * @return
	 */
	@Override
	public String constructor(ThingModelDef model) {
		String packageName = ThingDefinitionConstants.MODEL_DEF_PACKAGE + "." + model.getThingModelType();
		TypeSpec.Builder modelClass = TypeSpec.classBuilder(String.format(CLASSNAME, model.getDeviceModel(), model.getName()));
		modelClass.addModifiers(Modifier.PUBLIC);

		DeviceModel deviceModel = deviceModelService.deviceModelById(model.getDeviceModelId());

		if (deviceModel == null) {
			LOGGER.error("Cannot get deviceModel by modelId[" + model.getDeviceModelId() + "], model["
					+ model.getDeviceModel() + "]");
			throw new BizLocaleException(CfgResultCode.THING_MODEL_MISS);
		}

		List<ThingModelField> fields = model.getFields();
		if (fields == null || fields.size() == 0) {
			LOGGER.warn("Cannot found thingModelField, modelDef[{}].", model);
			throw new BizLocaleException(CfgResultCode.THING_MODEL_MISS_FIELD);
		}

		ThingDataTypeEnum typeEnum = ThingDataTypeEnum.getDataType(model.getThingModelType());
		ThingDataStrutTypeEnum layoutEnum = ThingDataStrutTypeEnum.getDataLayoutType(model.getStructType());

		modelClass.addAnnotation(AnnotationSpec.builder(com.cetiti.ddapv2.iotplatform.common.ThingModel.class)
				.addMember("modelId", "$S", deviceModel.deviceModelId)
                .addMember("model", "$S", deviceModel.deviceModel)
				.addMember("name", "$S", model.getName())
				.addMember("type", "$T.$L", typeEnum.getClass(), typeEnum.name())
				.addMember("strutType", "$T.$L", layoutEnum.getClass(), layoutEnum.name()).build());

		if (StringUtils.isNotBlank(model.getStoreTypes())) {

			StoreTypeEnum[] storeTypeEnums = StoreTypeEnum.getStoreTypes(StringUtils.split(model.getStoreTypes(), ","));
			String strStoreTypeEnums = Lists.newArrayList(storeTypeEnums).stream().map(e -> {
				return StoreTypeEnum.class.getName() + "." + e.getValue();
			}).collect(Collectors.joining(","));

			modelClass.addAnnotation(AnnotationSpec.builder(com.cetiti.ddapv2.iotplatform.common.StoreableObject.class)
					.addMember("name", "$S", deviceModel.deviceModel)
					.addMember("storetype", CodeBlock.of(" $1L", "{" + strStoreTypeEnums + "}")).build());
		}

		modelClass.addJavadoc(model.getDescription() != null ? model.getDescription() : "");

		for (int i = 0; i < fields.size(); i++) {
			modelClass.addField(makeFieldSpec(fields.get(i), layoutEnum));

		}
		modelClass.addMethod(makeToString());
		JavaFile javaFile = JavaFile.builder(packageName, modelClass.build()).build();

		// -- 写入到文件。
		File dir = new File(targetDirectory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			javaFile.writeTo(dir);
		} catch (IOException e) {
			LOGGER.error("Generate java file[" + javaFile + "] fail.", e);
			throw new BizLocaleException(CfgResultCode.THING_MODEL_CONSTRUCTION_FAIL);
		}

		String javaSourceFile = String.format(ThingDefinitionConstants.MODEL_DEF_NAME_TEMPLATE,
				model.getThingModelType(), String.format(CLASSNAME, model.getDeviceModel(), model.getName()));

		if (!compile(javaSourceFile)) {
			LOGGER.error("Compile java file[" + javaFile + "] fail.");
			throw new BizLocaleException(CfgResultCode.THING_MODEL_COMPILE_FAIL);
		}

		return javaSourceFile;
	}

	/**
	 * 字段初始化
	 */
	private FieldSpec makeFieldSpec(ThingModelField field, ThingDataStrutTypeEnum layoutEnum) {
		field.setName(field.getName().trim());
		Preconditions.checkArgument(StringUtils.isNoneBlank(field.getName()), "字段名不能为空！");
		Preconditions.checkArgument(StringUtils.isNoneBlank(field.getCustomType()), "字段类型不能为空！");
		FieldSpec.Builder filedSpecBuild = FieldSpec.builder(CustomTypeEnum.getValueByKey(field.getCustomType()),
				field.getName(), Modifier.PUBLIC);

		// 注解
		List<AnnotationSpec> result = makeAnnotationSpec(field, layoutEnum);
		for (AnnotationSpec spec : result) {
			filedSpecBuild.addAnnotation(spec);
		}

		return filedSpecBuild.build();
	}

	/**
	 * 构建注解。
	 */
	private List<AnnotationSpec> makeAnnotationSpec(ThingModelField field, ThingDataStrutTypeEnum layoutEnum) {
		List<AnnotationSpec> result = Lists.newArrayList();

		//二进制流
		if (layoutEnum.equals(ThingDataStrutTypeEnum.LV_BYTES)) {
			result.add(makeByteBeanFieldAnnotationSpec(field));
		}

		// 如果需要存储的话。
		if (StringUtils.isNotBlank(field.getColumn())) {
			AnnotationSpec.Builder dbBuilder = AnnotationSpec.builder(DBField.class);
			// index
			dbBuilder.addMember("column", "$S", field.getColumn());
			dbBuilder.addMember("label", "$S",
                    StringUtils.isBlank(field.getLabel()) ? field.getName() : field.getLabel());
			if (StringUtils.isNotBlank(field.getUnit())) {
				dbBuilder.addMember("unit", "$S", field.getUnit());
			}

			result.add(dbBuilder.build());
		}

		return result;
	}

	/**
	 * 构建ByteBeanField注解。
	 *
	 * @param field
	 * @return
	 */
	private AnnotationSpec makeByteBeanFieldAnnotationSpec(ThingModelField field) {
		AnnotationSpec.Builder builder = AnnotationSpec.builder(ByteBeanField.class);
		// index
		builder.addMember("index", "$L", field.getIndexNum());
		// bytes || fixedLength
		switch (getTypeStatus(field.getCustomType())) {
		case 0:
			builder.addMember("fixedLength", "$L", field.getValueLength());
			break;
		case 1:
			break;
		case 2:
			builder.addMember("bytes", "$L", field.getValueLength());
			break;
		default:
			break;
		}
		// lenField
		if (StringUtils.isNoneBlank(field.getLenField())) {
			builder.addMember("lenField", "$S", field.getLenField());
		}
		// charset
		if (StringUtils.isNoneBlank(field.getCharset())) {
			builder.addMember("charset", "$S", field.getCharset());
		}
		// description
		if (StringUtils.isNoneBlank(field.getDescription())) {
			builder.addMember("description", "$S", field.getDescription());
		}

		return builder.build();
	}

	/**
	 * set初始化
	 */
	@Deprecated
	private MethodSpec makeSetMethod(ThingModelField field) {
		MethodSpec.Builder setMethodSpecBuilder = MethodSpec
				.methodBuilder("set" + toUpperCaseFirstOne(field.getName()));
		setMethodSpecBuilder.addModifiers(Modifier.PUBLIC);
		setMethodSpecBuilder.returns(TypeName.VOID);
		setMethodSpecBuilder.addParameter(CustomTypeEnum.getValueByKey(field.getCustomType()), field.getName());
		setMethodSpecBuilder.addStatement("this.$N = $N", field.getName(), field.getName());
		return setMethodSpecBuilder.build();
	}

	/**
	 * get初始化
	 */
	@Deprecated
	private MethodSpec makeGetMethod(ThingModelField field) {
		MethodSpec.Builder getMethodSpecBuilder = MethodSpec
				.methodBuilder("get" + toUpperCaseFirstOne(field.getName()));
		getMethodSpecBuilder.addModifiers(Modifier.PUBLIC);
		getMethodSpecBuilder.returns(CustomTypeEnum.getValueByKey(field.getCustomType()));
		getMethodSpecBuilder.addStatement("return $N", field.getName());
		return getMethodSpecBuilder.build();
	}

	/**
	 * toString
	 */
	private MethodSpec makeToString() {
		MethodSpec.Builder toStringBuilder = MethodSpec.methodBuilder("toString");
		toStringBuilder.addModifiers(Modifier.PUBLIC);
		toStringBuilder.returns(TypeName.get(String.class));
		ClassName toString = ClassName.get(ToStringBuilder.class);
		toStringBuilder.addStatement("return $T.reflectionToString(this)", toString);
		return toStringBuilder.build();
	}

	/**
	 * 编译Java文件。
	 *
	 * @param JavaModelName
	 *            实体类
	 * @return
	 */
	private boolean compile(String JavaModelName) {
		if (!isJavaExist(JavaModelName)) {
			LOGGER.error("Cannot found java file[" + JavaModelName + "].");
			return false;
		}

		String javaFileName = JavaModelName.replace(".", File.separator) + ".java";
		String javaFullPath = targetDirectory + javaFileName;
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

		try {
			Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(javaFullPath);
			Iterable<String> options = Arrays.asList("-encoding", "utf-8", "-cp", classpath);
			CompilationTask cTask = compiler.getTask(null, fileManager, null, options, null, fileObjects);
			return cTask.call();
		} catch (Exception e) {
			LOGGER.error("Compile file[" + JavaModelName + "] fail.", e);
			return false;
		} finally {
			try {
				fileManager.close();
			} catch (Exception e) {
				LOGGER.error("Close fileManager[" + fileManager + "] fail.", e);
			}
		}

	}

	/**
	 * 首字母大写转换
	 */
	private String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	/**
	 * 类型判断
	 */
	private int getTypeStatus(String customType) {
		if (customType.equals("string") || customType.equals("array")) {
			return 0;
		} else if (customType.equals("varstring") || customType.equals("variant")) {
			return 1;
		} else {
			return 2;
		}
	}

	// ------------- end constructor/compile.--------------

	/**
	 * 判断指定的java是否存在
     * @param name
	 */
	private boolean isJavaExist(String name) {
		String classPackageName = name.replace(".", File.separator) + ".java";
		File dir = new File(targetDirectory + File.separator + classPackageName);
		if (!dir.exists()) {
			return false;
		}
		return true;
	}

	// ---------------- start package --------------------------

	/**
	 * 消息打包
	 */
	@Override
	public int packageAll() {
        int exitValue;
		String javaPackageName = ThingDefinitionConstants.MODEL_DEF_PACKAGE.replace(".", File.separator);
		try {
			ProcessBuilder pb = new ProcessBuilder("jar", "-cf", jarName, javaPackageName);
			pb.redirectErrorStream(true);
			Process process = pb.start();
			exitValue = process.waitFor();
			if (exitValue != 0) {
				throw new BizLocaleException(CfgResultCode.THING_MODEL_PACKAGE);
			}
		} catch (IOException | InterruptedException e) {
			LOGGER.error("Make package fail", e);
			throw new BizLocaleException(CfgResultCode.THING_MODEL_PACKAGE);
		}
		return exitValue;
	}

	@Override
	public File getModelJar() {
		return new File(targetDirectory + File.separator + jarName);
	}

	// -------------- end package ------------------------------
}
