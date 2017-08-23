package com.banzhiyan.core.dto;

import com.banzhiyan.core.model.BaseObject;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class PageRequestDTO<T> extends BaseObject {
    private static final long serialVersionUID = 4480303445151998440L;
    private T argument;
    private int pageNum;
    private int pageSize;

    public PageRequestDTO() {
    }

    public T getArgument() {
        return this.argument;
    }

    public void setArgument(T argument) {
        this.argument = argument;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return (this.pageNum < 0?0:this.pageNum) * this.pageSize;
    }
}

