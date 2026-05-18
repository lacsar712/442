package com.classroom.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统配置实体类
 */
@Data
public class SystemConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 配置ID */
    private Long id;
    
    /** 配置键 */
    private String configKey;
    
    /** 配置值 */
    private String configValue;
    
    /** 配置描述 */
    private String description;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
}
