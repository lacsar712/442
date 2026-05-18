package com.classroom.mapper;

import com.classroom.entity.ClassInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 班级Mapper接口
 */
@Mapper
public interface ClassInfoMapper {
    
    /**
     * 根据ID查询班级
     */
    ClassInfo selectById(@Param("id") Long id);
    
    /**
     * 分页查询班级列表
     */
    List<ClassInfo> selectList(@Param("keyword") String keyword, 
                               @Param("status") Integer status,
                               @Param("offset") Integer offset, 
                               @Param("limit") Integer limit);
    
    /**
     * 查询班级总数
     */
    Long selectCount(@Param("keyword") String keyword, @Param("status") Integer status);
    
    /**
     * 查询所有班级（用于下拉选择）
     */
    List<ClassInfo> selectAll();
    
    /**
     * 新增班级
     */
    int insert(ClassInfo classInfo);
    
    /**
     * 更新班级
     */
    int update(ClassInfo classInfo);
    
    /**
     * 删除班级
     */
    int deleteById(@Param("id") Long id);
}
