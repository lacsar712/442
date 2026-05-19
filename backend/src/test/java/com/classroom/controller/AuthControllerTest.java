package com.classroom.controller;

import com.classroom.dto.LoginDTO;
import com.classroom.dto.UserDTO;
import com.classroom.service.SystemConfigService;
import com.classroom.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private SystemConfigService systemConfigService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("注册 - 系统禁止注册时返回403")
    void registerShouldReturn403WhenNotAllowed() throws Exception {
        when(systemConfigService.getValueByKey("allow_register")).thenReturn("false");

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value("系统已禁止用户注册"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("注册 - 允许注册时成功")
    void registerShouldSucceedWhenAllowed() throws Exception {
        when(systemConfigService.getValueByKey("allow_register")).thenReturn("true");
        doNothing().when(userService).register(any(UserDTO.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("注册成功"));
    }

    @Test
    @DisplayName("注册 - 配置为空时允许注册")
    void registerShouldSucceedWhenConfigIsNull() throws Exception {
        when(systemConfigService.getValueByKey("allow_register")).thenReturn(null);
        doNothing().when(userService).register(any(UserDTO.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("注册成功"));
    }

    @Test
    @DisplayName("登录成功返回code200且data中包含token")
    void loginShouldReturn200WithToken() throws Exception {
        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("token", "test-jwt-token-abc123");
        loginResult.put("userId", 1L);
        loginResult.put("username", "testuser");
        loginResult.put("realName", "Test User");
        loginResult.put("role", 2);
        loginResult.put("avatar", null);

        when(userService.login(any(LoginDTO.class))).thenReturn(loginResult);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("登录成功"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.token").value("test-jwt-token-abc123"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.role").value(2));
    }
}
