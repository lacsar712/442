package com.classroom.service;

import com.classroom.entity.SystemConfig;

import java.util.List;

/**
 * 系统配置服务接口
 */
public interface SystemConfigService {
    
    /**
     * 获取所有配置
     */
    List<SystemConfig> getAll();
    
    /**
     * 根据Key获取配置
     */
    String getValueByKey(String key);
    
    /**
     * 更新配置
     */
    void update(List<SystemConfig> configs);
}
