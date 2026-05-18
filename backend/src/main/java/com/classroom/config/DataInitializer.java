package com.classroom.config;

import com.classroom.entity.User;
import com.classroom.mapper.UserMapper;
import com.classroom.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器
 * 确保管理员密码使用正确的BCrypt格式
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public void run(String... args) {
        try {
            // 检查admin用户的密码
            User admin = userMapper.selectByUsername("admin");
            if (admin != null) {
                // 验证密码是否正确
                if (!PasswordUtil.matches("123456", admin.getPassword())) {
                    // 重新加密密码
                    String newPassword = PasswordUtil.encode("123456");
                    userMapper.updatePassword(admin.getId(), newPassword);
                    logger.info("管理员密码已重新加密");
                }
            }
            
            // 检查teacher用户的密码
            User teacher = userMapper.selectByUsername("teacher");
            if (teacher != null && !PasswordUtil.matches("123456", teacher.getPassword())) {
                String newPassword = PasswordUtil.encode("123456");
                userMapper.updatePassword(teacher.getId(), newPassword);
                logger.info("教师用户密码已重新加密");
            }
            
            // 检查普通用户的密码
            User user = userMapper.selectByUsername("user");
            if (user != null && !PasswordUtil.matches("123456", user.getPassword())) {
                String newPassword = PasswordUtil.encode("123456");
                userMapper.updatePassword(user.getId(), newPassword);
                logger.info("普通用户密码已重新加密");
            }
            
            logger.info("数据初始化检查完成");
        } catch (Exception e) {
            logger.error("数据初始化失败: {}", e.getMessage());
        }
    }
}
