package com.cetiti.iotp.cfgservice.service.impl;

import com.cetiti.ddapv2.iotplatform.common.thingModel.annotation.DBField;
import com.cetiti.ddapv2.iotplatform.common.thingModel.annotation.StoreableObject;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.CustomTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.StoreTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.StructTypeEnum;
import com.cetiti.ddapv2.iotplatform.common.thingModel.enums.ThingModelTypeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgErrorCodeEnum;
import com.cetiti.iotp.cfgservice.common.result.CfgServiceException;
import com.cetiti.iotp.cfgservice.common.utils.DeviceModelValidateManger;
import com.cetiti.iotp.cfgservice.common.utils.ThingModelValidateManager;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelField;
import com.cetiti.iotp.cfgservice.domain.entity.ThingModelHeader;
import com.cetiti.iotp.cfgservice.domain.vo.ThingModel;
import com.cetiti.iotp.cfgservice.service.ThingModelProcess;
import com.cetiti.iotp.cfgservice.service.ThingModelService;
import com.cetiti.iotp.itf.assetservice.vo.DeviceModel;
import com.google.common.collect.Lists;
import com.squareup.javapoet.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xiaojian.toolkit.bean.bytebean.annotation.ByteBeanField;

import javax.lang.model.element.Modifier;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.cetiti.ddapv2.iotplatform.common.thingModel.constant.ThingModelConstant.*;


/**
 * @author zhouliyu
 * @since 2019-04-02 16:21:16
 */
@Slf4j
@Service
public class ThingModelProcessImpl implements ThingModelProcess {

    @Autowired
    private ThingModelService thingModelService;
    @Autowired
    private ThingModelValidateManager thingModelValidateManager;
    @Autowired
    private DeviceModelValidateManger deviceModelValidateManger;


    @Override
    public String classConstructor(ThingModel thingModel) {
        thingModelValidateManager.validateThingModel(thingModel);
        TypeSpec.Builder thingModelClass = classBuilder(thingModel.getThingModelHeader());
        thingModelClass.addAnnotation(buildThingModelAnnotation(thingModel.getThingModelHeader()));
        thingModelClass.addAnnotation(buildStoreableObjectAnnotation(thingModel.getThingModelHeader()));
        buildJavadoc(thingModelClass, thingModel.getThingModelHeader().getDescription());
        List<ThingModelField> listThingModelField = thingModel.getThingModelFields();
        listThingModelField.forEach(e -> thingModelClass.addField(buildFiled(e)));
        return buildJavaSourceFile(thingModelClass,thingModel);
    }


    /**
     * 构建类名
     * @param thingModelHeader 模型头信息
     * */
    private TypeSpec.Builder classBuilder(ThingModelHeader thingModelHeader){
        TypeSpec.Builder thingModelClass = TypeSpec
                .classBuilder(String.format(CLASS_NAME,thingModelHeader.getDeviceModel(),thingModelHeader.getThingModelName()));
        thingModelClass.addModifiers(Modifier.PUBLIC);
        return thingModelClass;
    }

    /**
     * 构建注解@ThingModel
     * @param thingModelHeader 模型头信息
     *
     * */
    private AnnotationSpec buildThingModelAnnotation(ThingModelHeader thingModelHeader){

        DeviceModel deviceModel = deviceModelValidateManger.getDeviceModel(thingModelHeader.getDeviceModel());

        ThingModelTypeEnum thingModelType = ThingModelTypeEnum.getThingModelTypeEnum(thingModelHeader.getThingModelType());
        StructTypeEnum strutType = StructTypeEnum.getStructTypeEnum(thingModelHeader.getStructType());

        AnnotationSpec.Builder builder = AnnotationSpec.builder(com.cetiti.ddapv2.iotplatform.common.thingModel.annotation.ThingModel.class);
        builder.addMember("modelId", "$S", deviceModel.deviceModelId)
                .addMember("model", "$S", deviceModel.deviceModel)
                .addMember("name", "$S", thingModelHeader.getThingModelName())
                .addMember("type", "$T.$L", thingModelType.getClass(), thingModelType.name())
                .addMember("strutType", "$T.$L", strutType.getClass(), strutType.name());
        return builder.build();
    }

    /**
     * 构建注解@StoreableObject
     * @param thingModelHeader 模型头信息
     * */
    private AnnotationSpec buildStoreableObjectAnnotation(ThingModelHeader thingModelHeader){
        AnnotationSpec.Builder builder = AnnotationSpec.builder(StoreableObject.class);
        if(StringUtils.isNoneBlank(thingModelHeader.getStoreType())){
            String[] storeTypes = StringUtils.split(thingModelHeader.getStoreType(),",");
            StoreTypeEnum[] storeTypeEnums = StoreTypeEnum.getStoreTypes(storeTypes);
            String strStoreTypeEnums = Lists.newArrayList(storeTypeEnums)
                    .stream()
                    .map(e -> StoreTypeEnum.class.getName().concat(".").concat(e.getValue()))
                    .collect(Collectors.joining(","));
            builder.addMember("name", "$S", thingModelHeader.getDeviceModel())
                    .addMember("storetype", CodeBlock.of(" $1L", "{" + strStoreTypeEnums + "}"));
        }
        return builder.build();
    }

    /**
     * 构建javadoc
     * @param thingModelClass 模型类
     * @param javadoc 注释内容
     * */
    private void buildJavadoc(TypeSpec.Builder thingModelClass, String javadoc){
        thingModelClass.addJavadoc(javadoc != null ? javadoc : "");
    }

    /**
     * 字段构建
     * @param thingModelField 模型字段
     * */
    private FieldSpec buildFiled(ThingModelField thingModelField){

        FieldSpec.Builder fieldBuilder = FieldSpec.builder(CustomTypeEnum.getDataClass(thingModelField.getCustomType()),
                thingModelField.getFieldName(), Modifier.PUBLIC);
        fieldBuilder.addAnnotation(buildByteBeanFieldAnnotation(thingModelField));
        fieldBuilder.addAnnotation(buildDBFieldAnnotation(thingModelField));
        return fieldBuilder.build();
    }

    /**
     * 字段注解@ByteBeanField
     * @param thingModelField 模型字段
     *
     * */
    private AnnotationSpec buildByteBeanFieldAnnotation(ThingModelField thingModelField){
        AnnotationSpec.Builder builder = AnnotationSpec.builder(ByteBeanField.class);
        builder.addMember("index", "$L", thingModelField.getIndexNum());
        switch (CustomTypeEnum.getCustomTypeEnum(thingModelField.getCustomType())){
            case FIXED_STRING:
                builder.addMember("fixedLength", "$L", thingModelField.getFixedLength());
                break;
            case UNFIXED_STRING:
                break;
            default:
                builder.addMember("bytes", "$L", thingModelField.getFixedLength());
                break;
        }
        if (StringUtils.isNoneBlank(thingModelField.getLinkField())) {
            String fieldName = thingModelService.getThingModelField(thingModelField.getThingModelFieldId()).getFieldName();
            builder.addMember("lenField", "$S", fieldName);
        }
        if (StringUtils.isNoneBlank(thingModelField.getCharset())) {
            builder.addMember("charset", "$S", thingModelField.getCharset());
        }
        if (StringUtils.isNoneBlank(thingModelField.getDescription())) {
            builder.addMember("description", "$S", thingModelField.getDescription());
        }
        return builder.build();
    }

    /**
     * 字段注解@DBField
     * @param thingModelField 模型字段
     * */
    private AnnotationSpec buildDBFieldAnnotation(ThingModelField thingModelField){
        AnnotationSpec.Builder builder = AnnotationSpec.builder(DBField.class);
        if(StringUtils.isNotBlank(thingModelField.getColumn())){
            builder.addMember("column", "$S", thingModelField.getColumn());
            builder.addMember("label", "$S",
                    StringUtils.isBlank(thingModelField.getLabel()) ? thingModelField.getFieldName() : thingModelField.getLabel());
            if (StringUtils.isNotBlank(thingModelField.getUnit())) {
                builder.addMember("unit", "$S", thingModelField.getUnit());
            }
        }
        return builder.build();
    }

    /**
     * 生成设备模型.java文件
     * @param thingModelClass 模型类
     * @param thingModel 模型
     * */
    private String buildJavaSourceFile(TypeSpec.Builder thingModelClass,  ThingModel thingModel){
        String packageName = String.format(PACKAGE_TYPE, thingModel.getThingModelHeader().getThingModelType());
        JavaFile javaFile = JavaFile.builder(packageName, thingModelClass.build()).build();
        File dir = new File(DIRECTORY);
        String javaSourceFile;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            javaFile.writeTo(dir);
            javaSourceFile = String.format(PACKAGE_TYPE_CLASS_NAME,
                    thingModel.getThingModelHeader().getThingModelType(),
                    String.format(CLASS_NAME,
                            thingModel.getThingModelHeader().getDeviceModel(),
                            thingModel.getThingModelHeader().getThingModelName()));
        } catch (IOException e) {
            log.error("Generate java file[" + javaFile + "] fail.", e);
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_CONSTRUCTION_FAIL);
        }
        return javaSourceFile;
    }


    @Override
    public boolean classCompiler(List<String> javaSourceFile) {
        List<String> javaFullPath = javaSourceFile.stream().map(this::buildJavaFullPath).collect(Collectors.toList());
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        try {
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjectsFromStrings(javaFullPath);
            JavaCompiler.CompilationTask cTask = compiler.getTask(null, fileManager, diagnostics, null, null, fileObjects);
            return cTask.call();
        } catch (Exception e) {
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_COMPILE_FAIL);
        } finally {
            try {
                fileManager.close();
            } catch (Exception e) {
                log.error("Close fileManager[" + fileManager + "] fail.", e);
            }
        }
    }

    /**
     * 生成java资源路径
     * @param javaSourceFile 模型java文件
     * */
    private String buildJavaFullPath(String javaSourceFile){
        String javaSource = javaSourceFile.replace(".", File.separator) + ".java";
        String javaFullPath = DIRECTORY + File.separator + javaSource;
        File dir = new File(javaFullPath);
        if(!dir.exists()){
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_JAVA_EMPTY);
        }
        return javaFullPath;
    }

    @Override
    public boolean classLoader() {
        String javaPackageName = PACKAGE.replace(".", File.separator);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("jar", "-cf", JAR_NAME, javaPackageName);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            int exitValue = process.waitFor();
            if (exitValue != 0) {
                throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_PACKAGE_FAIL);
            }
            return true;
        } catch (IOException | InterruptedException e) {
            log.error("Make package fail", e);
            throw new CfgServiceException(CfgErrorCodeEnum.THING_MODEL_PACKAGE_FAIL);
        }
    }


    @Override
    public void delFailureThingModel(ThingModelHeader thingModelHeader){
        String className = String.format(
                CLASS_NAME,thingModelHeader.getDeviceModel(),thingModelHeader.getThingModelName());
        String thingModelPackage = DIRECTORY
                        + PACKAGE.replace("." , File.separator);
            String sourceTargetDir = thingModelPackage + File.separator + thingModelHeader.getThingModelType();
            File file = new File(sourceTargetDir);
            if(file.exists()){
                File sourceFiles[] = file.listFiles();
                if(sourceFiles != null && sourceFiles.length > 0){
                    for(File sourceFile:sourceFiles ){
                        if(StringUtils.containsIgnoreCase(sourceFile.getName(), className)){
                            sourceFile.delete();
                        }
                    }
                }

            }
    }

    @Override
    public File getThingModelJar() {
        return new File(DIRECTORY + File.separator + JAR_NAME);
    }
}
