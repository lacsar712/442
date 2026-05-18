package com.classroom.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户DTO
 */
@Data
public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50之间")
    private String username;
    
    @Size(min = 6, max = 100, message = "密码长度必须在6-100之间")
    private String password;
    
    @Size(max = 50, message = "真实姓名长度不能超过50")
    private String realName;
    
    // 允许空字符串或有效邮箱格式
    @Pattern(regexp = "^$|^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    private String email;
    
    // 允许空字符串或有效手机号格式
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    private String avatar;
    
    /** 角色：0-管理员 1-教师 2-学生 */
    private Integer role;
    
    /** 状态：0-禁用 1-正常 */
    private Integer status;
}
