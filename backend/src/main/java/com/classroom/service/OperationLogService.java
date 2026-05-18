package com.classroom.service;

import com.classroom.common.PageResult;
import com.classroom.entity.OperationLog;

/**
 * 操作日志服务接口
 */
public interface OperationLogService {
    
    /**
     * 分页查询日志
     */
    PageResult<OperationLog> getList(String keyword, String module, Integer page, Integer limit);
    
    /**
     * 清理日志
     */
    void cleanLogs(Integer days);
}
