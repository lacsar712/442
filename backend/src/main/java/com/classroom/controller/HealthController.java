package com.classroom.controller;

import com.classroom.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查控制器
 */
@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Result<String> health() {
        return Result.success("服务运行正常");
    }
}
