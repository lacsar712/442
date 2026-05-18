package com.classroom.mapper;

import com.classroom.entity.ClassActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 课堂活动Mapper接口
 */
@Mapper
public interface ClassActivityMapper {
    
    /**
     * 根据ID查询活动
     */
    ClassActivity selectById(@Param("id") Long id);
    
    /**
     * 分页查询活动列表
     */
    List<ClassActivity> selectList(@Param("keyword") String keyword,
                                   @Param("classId") Long classId,
                                   @Param("courseId") Long courseId,
                                   @Param("activityType") String activityType,
                                   @Param("status") Integer status,
                                   @Param("offset") Integer offset, 
                                   @Param("limit") Integer limit);
    
    /**
     * 查询活动总数
     */
    Long selectCount(@Param("keyword") String keyword,
                     @Param("classId") Long classId,
                     @Param("courseId") Long courseId,
                     @Param("activityType") String activityType,
                     @Param("status") Integer status);
    
    /**
     * 新增活动
     */
    int insert(ClassActivity activity);
    
    /**
     * 更新活动
     */
    int update(ClassActivity activity);
    
    /**
     * 删除活动
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 统计各类型活动数量
     */
    List<ClassActivity> countByType();
    
    /**
     * 查询最近活动
     */
    List<ClassActivity> selectRecent(@Param("limit") Integer limit);
}
