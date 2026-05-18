package com.classroom.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 班级DTO
 */
@Data
public class ClassInfoDTO {
    
    private Long id;
    
    @NotBlank(message = "班级名称不能为空")
    @Size(max = 100, message = "班级名称长度不能超过100")
    private String className;
    
    @NotBlank(message = "年级不能为空")
    @Size(max = 50, message = "年级长度不能超过50")
    private String grade;
    
    @Size(max = 100, message = "专业长度不能超过100")
    private String major;
    
    private Integer studentCount;
    
    @NotNull(message = "班主任不能为空")
    private Long teacherId;
    
    @Size(max = 500, message = "描述长度不能超过500")
    private String description;
    
    private Integer status;
}
