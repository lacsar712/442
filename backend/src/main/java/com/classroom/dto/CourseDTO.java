package com.classroom.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 课程DTO
 */
@Data
public class CourseDTO {
    
    private Long id;
    
    @NotBlank(message = "课程名称不能为空")
    @Size(max = 100, message = "课程名称长度不能超过100")
    private String courseName;
    
    @NotBlank(message = "课程代码不能为空")
    @Size(max = 50, message = "课程代码长度不能超过50")
    private String courseCode;
    
    private Integer credit;
    
    private Integer hours;
    
    @NotNull(message = "授课教师不能为空")
    private Long teacherId;
    
    @Size(max = 500, message = "描述长度不能超过500")
    private String description;
    
    private Integer status;
}
