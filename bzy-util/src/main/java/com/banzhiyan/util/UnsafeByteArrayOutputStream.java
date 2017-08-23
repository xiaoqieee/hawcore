package com.banzhiyan.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class UnsafeByteArrayOutputStream extends OutputStream {
    private byte[] mBuffer;
    private int mCount;

    public UnsafeByteArrayOutputStream() {
        this(32);
    }

    public UnsafeByteArrayOutputStream(int size) {
        if(size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        } else {
            this.mBuffer = new byte[size];
        }
    }

    public void write(int b) {
        int newcount = this.mCount + 1;
        if(newcount > this.mBuffer.length) {
            this.mBuffer = Bytes.copyOf(this.mBuffer, Math.max(this.mBuffer.length << 1, newcount));
        }

        this.mBuffer[this.mCount] = (byte)b;
        this.mCount = newcount;
    }

    public void write(byte[] b, int off, int len) {
        if(off >= 0 && off <= b.length && len >= 0 && off + len <= b.length && off + len >= 0) {
            if(len != 0) {
                int newcount = this.mCount + len;
                if(newcount > this.mBuffer.length) {
                    this.mBuffer = Bytes.copyOf(this.mBuffer, Math.max(this.mBuffer.length << 1, newcount));
                }

                System.arraycopy(b, off, this.mBuffer, this.mCount, len);
                this.mCount = newcount;
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int size() {
        return this.mCount;
    }

    public void reset() {
        this.mCount = 0;
    }

    public byte[] toByteArray() {
        return Bytes.copyOf(this.mBuffer, this.mCount);
    }

    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(this.mBuffer, 0, this.mCount);
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.mBuffer, 0, this.mCount);
    }

    public String toString() {
        return new String(this.mBuffer, 0, this.mCount);
    }

    public String toString(String charset) throws UnsupportedEncodingException {
        return new String(this.mBuffer, 0, this.mCount, charset);
    }

    public void close() throws IOException {
    }
}
