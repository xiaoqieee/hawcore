package com.banzhiyan.core.dto;

import com.banzhiyan.core.constant.ResponseResult;
import com.banzhiyan.core.model.BaseObject;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class ResponseDTO<E> extends BaseObject {
    private static final long serialVersionUID = 7094474287531232051L;
    // 返回数据
    private E data;
    // 是否成功
    private ResponseResult result;
    private String errorCode;
    private String errorDetails;

    public ResponseDTO() {
    }

    public ResponseDTO(ResponseResult result) {
        this.result = result;
    }

    public ResponseResult getResult() {
        return result;
    }

    public void setResult(ResponseResult result) {
        this.result = result;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDetails() {
        return this.errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public E getData() {
        return this.data;
    }

    public void setData(E data) {
        this.data = data;
    }
}

