package com.banzhiyan.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class UnsafeByteArrayInputStream extends InputStream {
    private byte[] mData;
    private int mPosition;
    private int mLimit;
    private int mMark;

    public UnsafeByteArrayInputStream(byte[] buf) {
        this(buf, 0, buf.length);
    }

    public UnsafeByteArrayInputStream(byte[] buf, int offset) {
        this(buf, offset, buf.length - offset);
    }

    public UnsafeByteArrayInputStream(byte[] buf, int offset, int length) {
        this.mMark = 0;
        this.mData = buf;
        this.mPosition = this.mMark = offset;
        this.mLimit = Math.min(offset + length, buf.length);
    }

    public int read() {
        return this.mPosition < this.mLimit?this.mData[this.mPosition++] & 255:-1;
    }

    public int read(byte[] b, int off, int len) {
        if(b == null) {
            throw new NullPointerException();
        } else if(off >= 0 && len >= 0 && len <= b.length - off) {
            if(this.mPosition >= this.mLimit) {
                return -1;
            } else {
                if(this.mPosition + len > this.mLimit) {
                    len = this.mLimit - this.mPosition;
                }

                if(len <= 0) {
                    return 0;
                } else {
                    System.arraycopy(this.mData, this.mPosition, b, off, len);
                    this.mPosition += len;
                    return len;
                }
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public long skip(long len) {
        if((long)this.mPosition + len > (long)this.mLimit) {
            len = (long)(this.mLimit - this.mPosition);
        }

        if(len <= 0L) {
            return 0L;
        } else {
            this.mPosition = (int)((long)this.mPosition + len);
            return len;
        }
    }

    public int available() {
        return this.mLimit - this.mPosition;
    }

    public boolean markSupported() {
        return true;
    }

    public void mark(int readAheadLimit) {
        this.mMark = this.mPosition;
    }

    public void reset() {
        this.mPosition = this.mMark;
    }

    public void close() throws IOException {
    }

    public int position() {
        return this.mPosition;
    }

    public void position(int newPosition) {
        this.mPosition = newPosition;
    }

    public int size() {
        return this.mData == null?0:this.mData.length;
    }
}

