package com.classroom.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 课堂活动DTO
 */
@Data
public class ClassActivityDTO {
    
    private Long id;
    
    @NotBlank(message = "活动标题不能为空")
    @Size(max = 200, message = "活动标题长度不能超过200")
    private String title;
    
    @Size(max = 5000, message = "活动内容长度不能超过5000")
    private String content;
    
    @NotNull(message = "班级不能为空")
    private Long classId;
    
    @NotNull(message = "课程不能为空")
    private Long courseId;
    
    @NotBlank(message = "活动类型不能为空")
    private String activityType;
    
    @NotNull(message = "活动日期不能为空")
    private LocalDateTime activityDate;
    
    private String attachment;
    
    private String attachmentName;
    
    private Integer status;
}
