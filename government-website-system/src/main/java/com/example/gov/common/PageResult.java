package com.example.gov.common;

import lombok.Data;

import java.util.List;

/**
 * 通用分页结果封装。
 *
 * @param <T> 列表元素类型
 */
@Data
public class PageResult<T> {
    private List<T> list;
    private long total;
    private int page;
    private int pageSize;
    private int totalPage;

    public PageResult(List<T> list, long total, int page, int pageSize) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPage = pageSize == 0 ? 0 : (int) Math.ceil((double) total / pageSize);
    }
}
