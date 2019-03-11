package com.cetiti.iotpcfgservice.common.id;

import org.springframework.stereotype.Repository;

/**
 * 分布式id生成器。
 *
 * @author weiyinglei
 */
@Repository
public class UniqueIdGenerator extends IdGenerator {

    /**
     * 生成一个userId。
     *
     * @return userId。
     */
    public String generateUserId() {
        long epoch = System.currentTimeMillis() / 1000;

        String type = ModuleEnum.getPrefix(ModuleEnum.CLOUD.moduleName)
                + IdModuleEnum.getPrefix(IdModuleEnum.USER.moduleName);

        return type + epoch + super.generateSeqString(type) + super.generateRandomString();
    }

    /**
     * 生成一个gatewayId。
     *
     * @return gatewayId。
     */
    public String generateGatewayId() {
        long epoch = System.currentTimeMillis() / 1000;

        String type = ModuleEnum.getPrefix(ModuleEnum.CLOUD.moduleName)
                + IdModuleEnum.getPrefix(IdModuleEnum.GATEWAY.moduleName);

        return type + epoch + super.generateSeqString(type) + super.generateRandomString();
    }

    /**
     * 生成一个applicationId。
     *
     * @return applicationId。
     */
    public String generateApplicationId() {
        long epoch = System.currentTimeMillis() / 1000;

        String type = ModuleEnum.getPrefix(ModuleEnum.CLOUD.moduleName)
                + IdModuleEnum.getPrefix(IdModuleEnum.APPLICATION.moduleName);

        return type + epoch + super.generateSeqString(type) + super.generateRandomString();
    }

    /**
     * 生成一个devicetypeId。
     *
     * @return devicetypeId。
     */
    public String generateDevicetypeId() {
        long epoch = System.currentTimeMillis() / 1000;

        String type = ModuleEnum.getPrefix(ModuleEnum.CLOUD.moduleName)
                + IdModuleEnum.getPrefix(IdModuleEnum.DEVICETYPE.moduleName);

        return type + epoch + super.generateSeqString(type) + super.generateRandomString();
    }

    /**
     * 生成一个deviceId。
     *
     * @return deviceId。
     */
    public String generateDeviceId() {
        long epoch = System.currentTimeMillis() / 1000;

        String type = ModuleEnum.getPrefix(ModuleEnum.CLOUD.moduleName)
                + IdModuleEnum.getPrefix(IdModuleEnum.DEVICE.moduleName);

        return type + epoch + super.generateSeqString(type) + super.generateRandomString();
    }

    /**
     * 生成一个alarmId。
     *
     * @return alarmId。
     */
    public String generateAlarmId() {
        long epoch = System.currentTimeMillis() / 1000;

        String type = ModuleEnum.getPrefix(ModuleEnum.CLOUD.moduleName)
                + IdModuleEnum.getPrefix(IdModuleEnum.ALARM.moduleName);

        return type + epoch + super.generateSeqString(type) + super.generateRandomString();
    }

    /**
     * 生成一个modelId。
     *
     * @return model。
     */
    public String generateModelId() {
        long epoch = System.currentTimeMillis() / 1000;

        String type = ModuleEnum.getPrefix(ModuleEnum.CLOUD.moduleName)
                + IdModuleEnum.getPrefix(IdModuleEnum.MODEL.moduleName);

        return type + epoch + super.generateSeqString(type) + super.generateRandomString();
    }

    /**
     * 生成一个设备组id
     *
     * @return model。
     */
    public String generateDeviceGroupId() {
        long epoch = System.currentTimeMillis() / 1000;

        String type = ModuleEnum.getPrefix(ModuleEnum.CLOUD.moduleName)
                + IdModuleEnum.getPrefix(IdModuleEnum.GROUP.moduleName);

        return type + epoch + super.generateSeqString(type) + super.generateRandomString();
    }
}