package com.classroom.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程实体类
 */
@Data
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 课程ID */
    private Long id;
    
    /** 课程名称 */
    private String courseName;
    
    /** 课程代码 */
    private String courseCode;
    
    /** 学分 */
    private Integer credit;
    
    /** 学时 */
    private Integer hours;
    
    /** 授课教师ID */
    private Long teacherId;
    
    /** 授课教师姓名（非数据库字段） */
    private String teacherName;
    
    /** 课程描述 */
    private String description;
    
    /** 状态：0-禁用 1-正常 */
    private Integer status;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
}
