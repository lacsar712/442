package com.classroom.aspect;

import com.classroom.entity.OperationLog;
import com.classroom.mapper.OperationLogMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 操作日志切面
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private OperationLogMapper operationLogMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.classroom.controller.*.*(..)) && " +
              "!execution(* com.classroom.controller.HealthController.*(..))")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        OperationLog log = new OperationLog();
        
        try {
            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                // 设置用户信息
                Object userId = request.getAttribute("userId");
                Object username = request.getAttribute("username");
                
                if (userId != null) {
                    log.setUserId((Long) userId);
                }
                if (username != null) {
                    log.setUsername((String) username);
                }
                
                log.setIp(getClientIp(request));
            }
            
            // 获取方法信息
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            
            String className = point.getTarget().getClass().getSimpleName();
            String methodName = method.getName();
            
            log.setModule(getModuleName(className));
            log.setOperation(getOperationName(methodName));
            log.setMethod(className + "." + methodName);
            
            // 获取参数
            try {
                Object[] args = point.getArgs();
                if (args != null && args.length > 0) {
                    String params = objectMapper.writeValueAsString(args);
                    if (params.length() > 2000) {
                        params = params.substring(0, 2000) + "...";
                    }
                    log.setParams(params);
                }
            } catch (Exception e) {
                log.setParams("参数序列化失败");
            }
            
            // 执行方法
            Object result = point.proceed();
            
            // 记录成功
            log.setStatus(1);
            log.setDuration(System.currentTimeMillis() - startTime);
            
            // 异步保存日志
            saveLogAsync(log);
            
            return result;
            
        } catch (Throwable e) {
            // 记录失败
            log.setStatus(0);
            log.setDuration(System.currentTimeMillis() - startTime);
            log.setErrorMsg(e.getMessage() != null ? 
                (e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage()) 
                : "未知错误");
            
            saveLogAsync(log);
            
            throw e;
        }
    }

    private void saveLogAsync(OperationLog log) {
        try {
            operationLogMapper.insert(log);
        } catch (Exception e) {
            logger.error("保存操作日志失败: {}", e.getMessage());
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String getModuleName(String className) {
        if (className.contains("User") || className.contains("Auth")) {
            return "用户管理";
        } else if (className.contains("ClassInfo")) {
            return "班级管理";
        } else if (className.contains("Course")) {
            return "课程管理";
        } else if (className.contains("Activity")) {
            return "课堂活动";
        } else if (className.contains("Config")) {
            return "系统配置";
        } else if (className.contains("File")) {
            return "文件管理";
        }
        return "其他";
    }

    private String getOperationName(String methodName) {
        if (methodName.startsWith("add") || methodName.startsWith("create") || methodName.startsWith("insert")) {
            return "新增";
        } else if (methodName.startsWith("update") || methodName.startsWith("edit") || methodName.startsWith("modify")) {
            return "修改";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "删除";
        } else if (methodName.startsWith("get") || methodName.startsWith("list") || methodName.startsWith("query") || methodName.startsWith("select")) {
            return "查询";
        } else if (methodName.startsWith("login")) {
            return "登录";
        } else if (methodName.startsWith("logout")) {
            return "登出";
        } else if (methodName.startsWith("export")) {
            return "导出";
        } else if (methodName.startsWith("upload")) {
            return "上传";
        } else if (methodName.startsWith("download")) {
            return "下载";
        }
        return "操作";
    }
}
