package com.banzhiyan.logging.util;

/**
 * Created by xn025665 on 2017/8/23.
 */
public class FormattingTuple {
    public static FormattingTuple NULL = new FormattingTuple((String)null);
    private String message;
    private Throwable throwable;
    private Object[] argArray;

    public FormattingTuple(String message) {
        this(message, (Object[])null, (Throwable)null);
    }

    public FormattingTuple(String message, Object[] argArray, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
        if(throwable == null) {
            this.argArray = argArray;
        } else {
            this.argArray = trimmedCopy(argArray);
        }

    }

    static Object[] trimmedCopy(Object[] argArray) {
        if(argArray != null && argArray.length != 0) {
            int trimemdLen = argArray.length - 1;
            Object[] trimmed = new Object[trimemdLen];
            System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
            return trimmed;
        } else {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
    }

    public String getMessage() {
        return this.message;
    }

    public Object[] getArgArray() {
        return this.argArray;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }
}
