package com.classroom.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void encodeShouldReturnNonNullHashDifferentFromRaw() {
        String rawPassword = "password123";
        String encoded = PasswordUtil.encode(rawPassword);
        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
    }

    @Test
    void matchesShouldReturnTrueWhenPasswordMatches() {
        String rawPassword = "password123";
        String encoded = PasswordUtil.encode(rawPassword);
        assertTrue(PasswordUtil.matches(rawPassword, encoded));
    }

    @Test
    void matchesShouldReturnFalseWhenPasswordDoesNotMatch() {
        String rawPassword = "password123";
        String wrongPassword = "wrongpassword";
        String encoded = PasswordUtil.encode(rawPassword);
        assertFalse(PasswordUtil.matches(wrongPassword, encoded));
    }

    @Test
    void encodeShouldGenerateDifferentHashesForSamePassword() {
        String rawPassword = "password123";
        String encoded1 = PasswordUtil.encode(rawPassword);
        String encoded2 = PasswordUtil.encode(rawPassword);
        assertNotEquals(encoded1, encoded2);
    }

    @Test
    void encodeShouldHandleLongPassword() {
        String longPassword = "a".repeat(100);
        String encoded = PasswordUtil.encode(longPassword);
        assertNotNull(encoded);
        assertTrue(PasswordUtil.matches(longPassword, encoded));
    }

    @Test
    void matchesShouldHandleEmptyPassword() {
        String emptyPassword = "";
        String encoded = PasswordUtil.encode(emptyPassword);
        assertNotNull(encoded);
        assertTrue(PasswordUtil.matches(emptyPassword, encoded));
        assertFalse(PasswordUtil.matches("nonempty", encoded));
    }
}
