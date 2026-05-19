package com.classroom.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    @DisplayName("encode应对同一密码生成不同的哈希值(随机盐)")
    void encodeShouldGenerateDifferentHashesForSamePassword() {
        String password = "password123";
        String hash1 = PasswordUtil.encode(password);
        String hash2 = PasswordUtil.encode(password);
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("encode应返回非空且非明文的哈希值")
    void encodeShouldReturnNonNullHashDifferentFromRaw() {
        String password = "mypassword";
        String hash = PasswordUtil.encode(password);
        assertNotNull(hash);
        assertNotEquals(password, hash);
    }

    @Test
    @DisplayName("matches应在原始密码与编码密码匹配时返回true")
    void matchesShouldReturnTrueWhenPasswordMatches() {
        String password = "test123456";
        String encoded = PasswordUtil.encode(password);
        assertTrue(PasswordUtil.matches(password, encoded));
    }

    @Test
    @DisplayName("matches应在密码不匹配时返回false")
    void matchesShouldReturnFalseWhenPasswordDoesNotMatch() {
        String password = "correctPwd";
        String encoded = PasswordUtil.encode(password);
        assertFalse(PasswordUtil.matches("wrongPwd", encoded));
    }

    @Test
    @DisplayName("matches应对空字符串密码正常工作")
    void matchesShouldHandleEmptyPassword() {
        String empty = "";
        String encoded = PasswordUtil.encode(empty);
        assertTrue(PasswordUtil.matches(empty, encoded));
        assertFalse(PasswordUtil.matches("nonempty", encoded));
    }

    @Test
    @DisplayName("encode应对长密码正常工作")
    void encodeShouldHandleLongPassword() {
        String longPassword = "a".repeat(72);
        String encoded = PasswordUtil.encode(longPassword);
        assertTrue(PasswordUtil.matches(longPassword, encoded));
    }
}
