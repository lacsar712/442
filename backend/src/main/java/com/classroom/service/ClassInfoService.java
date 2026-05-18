package com.classroom.service;

import com.classroom.common.PageResult;
import com.classroom.dto.ClassInfoDTO;
import com.classroom.entity.ClassInfo;

import java.util.List;

/**
 * 班级服务接口
 */
public interface ClassInfoService {
    
    /**
     * 根据ID获取班级
     */
    ClassInfo getById(Long id);
    
    /**
     * 分页查询班级
     */
    PageResult<ClassInfo> getList(String keyword, Integer status, Integer page, Integer limit);
    
    /**
     * 获取所有班级
     */
    List<ClassInfo> getAll();
    
    /**
     * 新增班级
     */
    void add(ClassInfoDTO classInfoDTO);
    
    /**
     * 更新班级
     */
    void update(ClassInfoDTO classInfoDTO);
    
    /**
     * 删除班级
     */
    void delete(Long id);
}
