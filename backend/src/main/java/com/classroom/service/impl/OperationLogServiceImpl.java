package com.classroom.service.impl;

import com.classroom.common.PageResult;
import com.classroom.entity.OperationLog;
import com.classroom.mapper.OperationLogMapper;
import com.classroom.service.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 操作日志服务实现
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public PageResult<OperationLog> getList(String keyword, String module, Integer page, Integer limit) {
        int offset = (page - 1) * limit;
        List<OperationLog> list = operationLogMapper.selectList(keyword, module, offset, limit);
        Long total = operationLogMapper.selectCount(keyword, module);
        return PageResult.of(page, limit, total, list);
    }

    @Override
    @Transactional
    public void cleanLogs(Integer days) {
        int deleted = operationLogMapper.deleteByDays(days);
        logger.info("清理{}天前的日志，共删除{}条", days, deleted);
    }
}
