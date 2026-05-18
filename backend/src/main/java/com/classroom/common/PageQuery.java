package com.classroom.common;

import lombok.Data;

/**
 * 分页查询参数
 */
@Data
public class PageQuery {
    
    /** 当前页码，默认1 */
    private Integer page = 1;
    
    /** 每页条数，默认10 */
    private Integer limit = 10;
    
    /** 搜索关键词 */
    private String keyword;
    
    /** 状态筛选 */
    private Integer status;

    /** 获取偏移量 */
    public Integer getOffset() {
        return (page - 1) * limit;
    }
}
