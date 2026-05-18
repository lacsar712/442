package com.classroom.mapper;

import com.classroom.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据ID查询用户
     */
    User selectById(@Param("id") Long id);
    
    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);
    
    /**
     * 分页查询用户列表
     */
    List<User> selectList(@Param("keyword") String keyword, 
                          @Param("status") Integer status,
                          @Param("role") Integer role,
                          @Param("offset") Integer offset, 
                          @Param("limit") Integer limit);
    
    /**
     * 查询用户总数
     */
    Long selectCount(@Param("keyword") String keyword, 
                     @Param("status") Integer status,
                     @Param("role") Integer role);
    
    /**
     * 查询所有教师
     */
    List<User> selectTeachers();
    
    /**
     * 新增用户
     */
    int insert(User user);
    
    /**
     * 更新用户
     */
    int update(User user);
    
    /**
     * 更新密码
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);
    
    /**
     * 删除用户
     */
    int deleteById(@Param("id") Long id);
}
