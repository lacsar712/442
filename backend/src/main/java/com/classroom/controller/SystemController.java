package com.classroom.controller;

import com.classroom.common.PageResult;
import com.classroom.common.Result;
import com.classroom.entity.OperationLog;
import com.classroom.entity.SystemConfig;
import com.classroom.service.OperationLogService;
import com.classroom.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置控制器
 */
@RestController
@RequestMapping("/api/system")
public class SystemController {

    @Autowired
    private SystemConfigService configService;

    @Autowired
    private OperationLogService logService;

    /**
     * 获取所有配置
     */
    @GetMapping("/configs")
    public Result<List<SystemConfig>> getConfigs() {
        List<SystemConfig> configs = configService.getAll();
        return Result.success(configs);
    }

    /**
     * 更新配置
     */
    @PutMapping("/configs")
    public Result<Void> updateConfigs(@RequestBody List<SystemConfig> configs) {
        configService.update(configs);
        return Result.success("配置更新成功", null);
    }

    /**
     * 获取操作日志
     */
    @GetMapping("/logs")
    public Result<PageResult<OperationLog>> getLogs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String module,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        PageResult<OperationLog> result = logService.getList(keyword, module, page, limit);
        return Result.success(result);
    }

    /**
     * 清理日志
     */
    @DeleteMapping("/logs")
    public Result<Void> cleanLogs(@RequestParam(defaultValue = "30") Integer days) {
        logService.cleanLogs(days);
        return Result.success("日志清理成功", null);
    }
}
