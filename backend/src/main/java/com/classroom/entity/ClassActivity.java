package com.classroom.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课堂活动实体类
 */
@Data
public class ClassActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    private Long id;
    
    /** 活动标题 */
    private String title;
    
    /** 活动内容 */
    private String content;
    
    /** 班级ID */
    private Long classId;
    
    /** 班级名称（非数据库字段） */
    private String className;
    
    /** 课程ID */
    private Long courseId;
    
    /** 课程名称（非数据库字段） */
    private String courseName;
    
    /** 创建者ID */
    private Long creatorId;
    
    /** 创建者姓名（非数据库字段） */
    private String creatorName;
    
    /** 活动类型：lecture-讲座 experiment-实验 discussion-讨论 exam-考试 other-其他 */
    private String activityType;
    
    /** 活动日期 */
    private LocalDateTime activityDate;
    
    /** 附件路径 */
    private String attachment;
    
    /** 附件原始文件名 */
    private String attachmentName;
    
    /** 状态：0-禁用 1-正常 */
    private Integer status;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
}
