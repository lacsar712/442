package com.classroom.service;

import com.classroom.common.PageResult;
import com.classroom.dto.ClassActivityDTO;
import com.classroom.entity.ClassActivity;

import java.util.List;
import java.util.Map;

/**
 * 课堂活动服务接口
 */
public interface ClassActivityService {
    
    /**
     * 根据ID获取活动
     */
    ClassActivity getById(Long id);
    
    /**
     * 分页查询活动
     */
    PageResult<ClassActivity> getList(String keyword, Long classId, Long courseId, 
                                       String activityType, Integer status, 
                                       Integer page, Integer limit);
    
    /**
     * 获取最近活动
     */
    List<ClassActivity> getRecent(Integer limit);
    
    /**
     * 新增活动
     */
    void add(ClassActivityDTO activityDTO, Long creatorId);
    
    /**
     * 更新活动
     */
    void update(ClassActivityDTO activityDTO);
    
    /**
     * 删除活动
     */
    void delete(Long id);
    
    /**
     * 获取统计数据
     */
    Map<String, Object> getStatistics();
}
