package com.classroom.mapper;

import com.classroom.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 课程Mapper接口
 */
@Mapper
public interface CourseMapper {
    
    /**
     * 根据ID查询课程
     */
    Course selectById(@Param("id") Long id);
    
    /**
     * 根据课程代码查询
     */
    Course selectByCode(@Param("courseCode") String courseCode);
    
    /**
     * 分页查询课程列表
     */
    List<Course> selectList(@Param("keyword") String keyword, 
                            @Param("status") Integer status,
                            @Param("offset") Integer offset, 
                            @Param("limit") Integer limit);
    
    /**
     * 查询课程总数
     */
    Long selectCount(@Param("keyword") String keyword, @Param("status") Integer status);
    
    /**
     * 查询所有课程（用于下拉选择）
     */
    List<Course> selectAll();
    
    /**
     * 新增课程
     */
    int insert(Course course);
    
    /**
     * 更新课程
     */
    int update(Course course);
    
    /**
     * 删除课程
     */
    int deleteById(@Param("id") Long id);
}
