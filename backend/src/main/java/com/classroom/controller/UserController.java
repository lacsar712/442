package com.classroom.controller;

import com.classroom.common.PageResult;
import com.classroom.common.Result;
import com.classroom.dto.PasswordDTO;
import com.classroom.dto.UserDTO;
import com.classroom.entity.User;
import com.classroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/profile")
    public Result<User> getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getById(userId);
        return Result.success(user);
    }

    /**
     * 更新个人信息
     */
    @PutMapping("/profile")
    public Result<Void> updateProfile(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateProfile(userId, userDTO);
        return Result.success("更新成功", null);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(HttpServletRequest request, 
                                       @RequestBody @Validated PasswordDTO passwordDTO) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updatePassword(userId, passwordDTO);
        return Result.success("密码修改成功", null);
    }

    /**
     * 获取用户列表
     */
    @GetMapping
    public Result<PageResult<User>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer role,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        PageResult<User> result = userService.getList(keyword, status, role, page, limit);
        return Result.success(result);
    }

    /**
     * 获取所有教师
     */
    @GetMapping("/teachers")
    public Result<List<User>> getTeachers() {
        List<User> teachers = userService.getTeachers();
        return Result.success(teachers);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Result<Void> add(@RequestBody @Validated UserDTO userDTO) {
        userService.add(userDTO);
        return Result.success("新增成功", null);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        userService.update(userDTO);
        return Result.success("更新成功", null);
    }

    /**
     * 更新用户状态
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, 
                                     @RequestParam Integer status,
                                     HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        userService.updateStatus(id, status, currentUserId);
        return Result.success("状态更新成功", null);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("userId");
        userService.delete(id, currentUserId);
        return Result.success("删除成功", null);
    }
}
