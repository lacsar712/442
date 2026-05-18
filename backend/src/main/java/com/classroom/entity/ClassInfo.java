package com.classroom.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 班级实体类
 */
@Data
public class ClassInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 班级ID */
    private Long id;
    
    /** 班级名称 */
    private String className;
    
    /** 年级 */
    private String grade;
    
    /** 专业 */
    private String major;
    
    /** 学生人数 */
    private Integer studentCount;
    
    /** 班主任ID */
    private Long teacherId;
    
    /** 班主任姓名（非数据库字段） */
    private String teacherName;
    
    /** 描述 */
    private String description;
    
    /** 状态：0-禁用 1-正常 */
    private Integer status;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
}
