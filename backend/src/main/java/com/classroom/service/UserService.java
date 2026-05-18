package com.classroom.service;

import com.classroom.common.PageResult;
import com.classroom.dto.LoginDTO;
import com.classroom.dto.PasswordDTO;
import com.classroom.dto.UserDTO;
import com.classroom.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    Map<String, Object> login(LoginDTO loginDTO);
    
    /**
     * 用户注册
     */
    void register(UserDTO userDTO);
    
    /**
     * 根据ID获取用户
     */
    User getById(Long id);
    
    /**
     * 根据用户名获取用户
     */
    User getByUsername(String username);
    
    /**
     * 分页查询用户
     */
    PageResult<User> getList(String keyword, Integer status, Integer role, Integer page, Integer limit);
    
    /**
     * 获取所有教师
     */
    List<User> getTeachers();
    
    /**
     * 新增用户
     */
    void add(UserDTO userDTO);
    
    /**
     * 更新用户
     */
    void update(UserDTO userDTO);
    
    /**
     * 更新个人信息
     */
    void updateProfile(Long userId, UserDTO userDTO);
    
    /**
     * 修改密码
     */
    void updatePassword(Long userId, PasswordDTO passwordDTO);
    
    /**
     * 删除用户
     */
    void delete(Long id, Long currentUserId);
    
    /**
     * 更新用户状态
     */
    void updateStatus(Long id, Integer status, Long currentUserId);
}
