package com.classroom.service.impl;

import com.classroom.common.PageResult;
import com.classroom.dto.LoginDTO;
import com.classroom.dto.PasswordDTO;
import com.classroom.dto.UserDTO;
import com.classroom.entity.User;
import com.classroom.exception.BusinessException;
import com.classroom.mapper.UserMapper;
import com.classroom.service.UserService;
import com.classroom.util.JwtUtil;
import com.classroom.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        if (!PasswordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用，请联系管理员");
        }
        
        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("role", user.getRole());
        result.put("avatar", user.getAvatar());
        
        logger.info("用户登录成功: {}", user.getUsername());
        
        return result;
    }

    @Override
    @Transactional
    public void register(UserDTO userDTO) {
        // 检查用户名是否存在
        User existUser = userMapper.selectByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(PasswordUtil.encode(userDTO.getPassword()));
        user.setRole(2); // 默认为学生
        user.setStatus(1);
        
        userMapper.insert(user);
        logger.info("用户注册成功: {}", user.getUsername());
    }

    @Override
    public User getById(Long id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setPassword(null); // 不返回密码
        }
        return user;
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public PageResult<User> getList(String keyword, Integer status, Integer role, Integer page, Integer limit) {
        int offset = (page - 1) * limit;
        List<User> list = userMapper.selectList(keyword, status, role, offset, limit);
        Long total = userMapper.selectCount(keyword, status, role);
        
        // 清除密码
        list.forEach(user -> user.setPassword(null));
        
        return PageResult.of(page, limit, total, list);
    }

    @Override
    public List<User> getTeachers() {
        List<User> teachers = userMapper.selectTeachers();
        teachers.forEach(user -> user.setPassword(null));
        return teachers;
    }

    @Override
    @Transactional
    public void add(UserDTO userDTO) {
        // 检查用户名是否存在
        User existUser = userMapper.selectByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(PasswordUtil.encode(
            StringUtils.hasText(userDTO.getPassword()) ? userDTO.getPassword() : "123456"
        ));
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        if (user.getRole() == null) {
            user.setRole(2);
        }
        
        userMapper.insert(user);
        logger.info("新增用户成功: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void update(UserDTO userDTO) {
        User existUser = userMapper.selectById(userDTO.getId());
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户名是否重复
        User userByName = userMapper.selectByUsername(userDTO.getUsername());
        if (userByName != null && !userByName.getId().equals(userDTO.getId())) {
            throw new BusinessException("用户名已存在");
        }
        
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        
        userMapper.update(user);
        logger.info("更新用户成功: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, UserDTO userDTO) {
        User existUser = userMapper.selectById(userId);
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        User user = new User();
        user.setId(userId);
        user.setRealName(userDTO.getRealName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAvatar(userDTO.getAvatar());
        
        userMapper.update(user);
        logger.info("更新个人信息成功: {}", existUser.getUsername());
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, PasswordDTO passwordDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 验证原密码
        if (!PasswordUtil.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        
        // 验证新密码
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new BusinessException("两次输入的新密码不一致");
        }
        
        userMapper.updatePassword(userId, PasswordUtil.encode(passwordDTO.getNewPassword()));
        logger.info("修改密码成功: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void delete(Long id, Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw new BusinessException("不能删除自己的账号");
        }
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        userMapper.deleteById(id);
        logger.info("删除用户成功: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status, Long currentUserId) {
        if (id.equals(currentUserId)) {
            throw new BusinessException("不能禁用自己的账号");
        }
        
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setStatus(status);
        userMapper.update(updateUser);
        
        logger.info("更新用户状态成功: {} -> {}", user.getUsername(), status == 1 ? "启用" : "禁用");
    }
}
