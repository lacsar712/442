package com.classroom.util;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 密码工具类
 */
public class PasswordUtil {

    /**
     * 加密密码
     */
    public static String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 验证密码
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
