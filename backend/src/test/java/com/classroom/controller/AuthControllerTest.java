package com.classroom.controller;

import com.classroom.dto.LoginDTO;
import com.classroom.dto.UserDTO;
import com.classroom.filter.JwtAuthFilter;
import com.classroom.mapper.ClassActivityMapper;
import com.classroom.mapper.ClassInfoMapper;
import com.classroom.mapper.CourseMapper;
import com.classroom.mapper.OperationLogMapper;
import com.classroom.mapper.SystemConfigMapper;
import com.classroom.mapper.UserMapper;
import com.classroom.service.SystemConfigService;
import com.classroom.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private SystemConfigService systemConfigService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private ClassActivityMapper classActivityMapper;

    @MockBean
    private ClassInfoMapper classInfoMapper;

    @MockBean
    private CourseMapper courseMapper;

    @MockBean
    private OperationLogMapper operationLogMapper;

    @MockBean
    private SystemConfigMapper systemConfigMapper;

    @Test
    @DisplayName("登录成功时应返回code 200且data中含token")
    void loginShouldReturn200WithToken() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("123456");

        Map<String, Object> loginResult = new HashMap<>();
        loginResult.put("token", "jwt-token-value");
        loginResult.put("userId", 1L);
        loginResult.put("username", "admin");
        loginResult.put("realName", "管理员");
        loginResult.put("role", 0);

        when(userService.login(any(LoginDTO.class))).thenReturn(loginResult);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("登录成功"))
                .andExpect(jsonPath("$.data.token").value("jwt-token-value"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.username").value("admin"));

        verify(userService).login(any(LoginDTO.class));
    }

    @Test
    @DisplayName("allow_register为false时注册应返回403且message为系统已禁止用户注册")
    void registerShouldReturn403WhenNotAllowed() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setPassword("123456");

        when(systemConfigService.getValueByKey(eq("allow_register"))).thenReturn("false");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value("系统已禁止用户注册"));
    }

    @Test
    @DisplayName("allow_register为true时注册应正常成功")
    void registerShouldSucceedWhenAllowed() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setPassword("123456");

        when(systemConfigService.getValueByKey(eq("allow_register"))).thenReturn("true");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("注册成功"));

        verify(userService).register(any(UserDTO.class));
    }

    @Test
    @DisplayName("allow_register配置为null时注册应正常成功")
    void registerShouldSucceedWhenConfigIsNull() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newuser2");
        userDTO.setPassword("123456");

        when(systemConfigService.getValueByKey(eq("allow_register"))).thenReturn(null);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("注册成功"));

        verify(userService).register(any(UserDTO.class));
    }
}
