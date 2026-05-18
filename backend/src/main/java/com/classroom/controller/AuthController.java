package com.classroom.controller;

import com.classroom.common.Result;
import com.classroom.dto.LoginDTO;
import com.classroom.dto.UserDTO;
import com.classroom.service.UserService;
import com.classroom.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody @Validated LoginDTO loginDTO) {
        Map<String, Object> result = userService.login(loginDTO);
        return Result.success("登录成功", result);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Validated UserDTO userDTO) {
        // 检查是否允许注册
        String allowRegister = systemConfigService.getValueByKey("allow_register");
        if ("false".equalsIgnoreCase(allowRegister)) {
            return Result.error(403, "系统已禁止用户注册");
        }
        userService.register(userDTO);
        return Result.success("注册成功", null);
    }
}
