package com.classroom.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void encode_shouldReturnEncodedPassword() {
        String rawPassword = "password123";
        String encodedPassword = PasswordUtil.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encodedPassword.length() > 0);
    }

    @Test
    void encode_samePasswordShouldReturnDifferentHashes() {
        String rawPassword = "password123";
        String encodedPassword1 = PasswordUtil.encode(rawPassword);
        String encodedPassword2 = PasswordUtil.encode(rawPassword);
        
        assertNotEquals(encodedPassword1, encodedPassword2);
    }

    @Test
    void matches_correctPasswordShouldReturnTrue() {
        String rawPassword = "password123";
        String encodedPassword = PasswordUtil.encode(rawPassword);
        
        assertTrue(PasswordUtil.matches(rawPassword, encodedPassword));
    }

    @Test
    void matches_wrongPasswordShouldReturnFalse() {
        String rawPassword = "password123";
        String wrongPassword = "wrongpassword";
        String encodedPassword = PasswordUtil.encode(rawPassword);
        
        assertFalse(PasswordUtil.matches(wrongPassword, encodedPassword));
    }

    @Test
    void matches_emptyPasswordShouldReturnFalse() {
        String rawPassword = "password123";
        String emptyPassword = "";
        String encodedPassword = PasswordUtil.encode(rawPassword);
        
        assertFalse(PasswordUtil.matches(emptyPassword, encodedPassword));
    }

    @Test
    void matches_nullPasswordShouldThrowException() {
        String rawPassword = "password123";
        String encodedPassword = PasswordUtil.encode(rawPassword);
        
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtil.matches(null, encodedPassword);
        });
    }

    @Test
    void encode_nullPasswordShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtil.encode(null);
        });
    }

    @Test
    void encode_emptyPasswordShouldWork() {
        String rawPassword = "";
        String encodedPassword = PasswordUtil.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertTrue(PasswordUtil.matches(rawPassword, encodedPassword));
    }

    @Test
    void encode_longPasswordShouldWork() {
        String rawPassword = "a".repeat(72);
        String encodedPassword = PasswordUtil.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertTrue(PasswordUtil.matches(rawPassword, encodedPassword));
    }

    @Test
    void matches_specialCharactersPasswordShouldWork() {
        String rawPassword = "P@ssw0rd!#$%^&*()";
        String encodedPassword = PasswordUtil.encode(rawPassword);
        
        assertTrue(PasswordUtil.matches(rawPassword, encodedPassword));
    }
}
