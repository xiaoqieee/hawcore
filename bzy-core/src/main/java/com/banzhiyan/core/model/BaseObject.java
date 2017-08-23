package com.banzhiyan.core.model;

import com.banzhiyan.core.builder.ToStringBuilder;
import com.banzhiyan.core.builder.ToStringStyle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class BaseObject implements Serializable {
    private static final long serialVersionUID = 4539343756018673380L;

    public BaseObject() {
    }

    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other, new String[0]);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, new String[0]);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

