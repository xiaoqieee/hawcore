package com.banzhiyan.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class GZIPResponseStream extends ServletOutputStream {
    private ServletOutputStream servletStream;
    private ByteArrayOutputStream byteStream;
    private GZIPOutputStream gzipStream;
    private HttpServletResponse response;
    private boolean closed = false;

    public GZIPResponseStream(HttpServletResponse response) throws IOException {
        this.response = response;
        this.servletStream = response.getOutputStream();
        this.byteStream = new ByteArrayOutputStream();
        this.gzipStream = new GZIPOutputStream(this.byteStream);
    }

    public void close() throws IOException {
        if(this.closed) {
            throw new IOException("This output stream has already been closed");
        } else {
            this.gzipStream.finish();
            byte[] bytes = this.byteStream.toByteArray();
            this.response.setContentLength(bytes.length);
            this.response.addHeader("Content-Encoding", "gzip");
            this.servletStream.write(bytes);
            this.servletStream.flush();
            this.servletStream.close();
            this.closed = true;
        }
    }

    public void flush() throws IOException {
        if(this.closed) {
            throw new IOException("Cannot flush a closed output stream");
        } else {
            this.gzipStream.flush();
        }
    }

    public void write(int b) throws IOException {
        if(this.closed) {
            throw new IOException("Cannot write to a closed output stream");
        } else {
            this.gzipStream.write((byte)b);
        }
    }

    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if(this.closed) {
            throw new IOException("Cannot write to a closed output stream");
        } else {
            this.gzipStream.write(b, off, len);
        }
    }

    public boolean closed() {
        return this.closed;
    }

    public void reset() {
    }

    public boolean isReady() {
        return false;
    }

    public void setWriteListener(WriteListener arg0) {
    }
}

