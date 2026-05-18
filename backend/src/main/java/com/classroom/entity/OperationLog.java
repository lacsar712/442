package com.classroom.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
public class OperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 用户名 */
    private String username;
    
    /** 模块 */
    private String module;
    
    /** 操作 */
    private String operation;
    
    /** 方法 */
    private String method;
    
    /** 参数 */
    private String params;
    
    /** IP地址 */
    private String ip;
    
    /** 耗时(ms) */
    private Long duration;
    
    /** 状态：0-失败 1-成功 */
    private Integer status;
    
    /** 错误信息 */
    private String errorMsg;
    
    /** 创建时间 */
    private LocalDateTime createTime;
}
