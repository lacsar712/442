package com.classroom.mapper;

import com.classroom.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 系统配置Mapper接口
 */
@Mapper
public interface SystemConfigMapper {
    
    /**
     * 查询所有配置
     */
    List<SystemConfig> selectAll();
    
    /**
     * 根据Key查询配置
     */
    SystemConfig selectByKey(@Param("configKey") String configKey);
    
    /**
     * 插入配置
     */
    int insert(SystemConfig config);
    
    /**
     * 更新配置
     */
    int update(SystemConfig config);
    
    /**
     * 根据Key更新配置值
     */
    int updateValueByKey(@Param("configKey") String configKey, @Param("configValue") String configValue);
}
