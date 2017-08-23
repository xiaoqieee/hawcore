package com.banzhiyan.core.dto;

import com.banzhiyan.core.model.BaseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class PageResponseDTO<T> extends BaseObject {
    private static final long serialVersionUID = 3571606415879693166L;
    private List<T> data;
    private int totalRow;

    public PageResponseDTO() {
    }

    public List<T> getData() {
        if(this.data == null) {
            this.data = new ArrayList(0);
        }

        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalRow() {
        return this.totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }
}

