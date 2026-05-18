package com.classroom.mapper;

import com.classroom.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 操作日志Mapper接口
 */
@Mapper
public interface OperationLogMapper {
    
    /**
     * 分页查询日志
     */
    List<OperationLog> selectList(@Param("keyword") String keyword,
                                  @Param("module") String module,
                                  @Param("offset") Integer offset, 
                                  @Param("limit") Integer limit);
    
    /**
     * 查询日志总数
     */
    Long selectCount(@Param("keyword") String keyword, @Param("module") String module);
    
    /**
     * 插入日志
     */
    int insert(OperationLog log);
    
    /**
     * 清理指定天数前的日志
     */
    int deleteByDays(@Param("days") Integer days);
}
