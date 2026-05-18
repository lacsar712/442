package com.classroom.service.impl;

import com.classroom.entity.SystemConfig;
import com.classroom.mapper.SystemConfigMapper;
import com.classroom.service.SystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统配置服务实现
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    private static final Logger logger = LoggerFactory.getLogger(SystemConfigServiceImpl.class);

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public List<SystemConfig> getAll() {
        return systemConfigMapper.selectAll();
    }

    @Override
    public String getValueByKey(String key) {
        SystemConfig config = systemConfigMapper.selectByKey(key);
        return config != null ? config.getConfigValue() : null;
    }

    @Override
    @Transactional
    public void update(List<SystemConfig> configs) {
        for (SystemConfig config : configs) {
            if (config.getId() != null) {
                systemConfigMapper.update(config);
            } else if (config.getConfigKey() != null) {
                systemConfigMapper.updateValueByKey(config.getConfigKey(), config.getConfigValue());
            }
        }
        logger.info("更新系统配置成功，共{}条", configs.size());
    }
}
