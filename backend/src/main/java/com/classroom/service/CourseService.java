package com.classroom.service;

import com.classroom.common.PageResult;
import com.classroom.dto.CourseDTO;
import com.classroom.entity.Course;

import java.util.List;

/**
 * 课程服务接口
 */
public interface CourseService {
    
    /**
     * 根据ID获取课程
     */
    Course getById(Long id);
    
    /**
     * 分页查询课程
     */
    PageResult<Course> getList(String keyword, Integer status, Integer page, Integer limit);
    
    /**
     * 获取所有课程
     */
    List<Course> getAll();
    
    /**
     * 新增课程
     */
    void add(CourseDTO courseDTO);
    
    /**
     * 更新课程
     */
    void update(CourseDTO courseDTO);
    
    /**
     * 删除课程
     */
    void delete(Long id);
}
