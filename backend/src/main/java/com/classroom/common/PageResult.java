package com.classroom.common;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 当前页码 */
    private Integer page;
    
    /** 每页条数 */
    private Integer limit;
    
    /** 总记录数 */
    private Long total;
    
    /** 总页数 */
    private Integer pages;
    
    /** 数据列表 */
    private List<T> list;

    public PageResult() {}

    public PageResult(Integer page, Integer limit, Long total, List<T> list) {
        this.page = page;
        this.limit = limit;
        this.total = total;
        this.pages = (int) Math.ceil((double) total / limit);
        this.list = list;
    }

    public static <T> PageResult<T> of(Integer page, Integer limit, Long total, List<T> list) {
        return new PageResult<>(page, limit, total, list);
    }
}
