package com.banzhiyan.util;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class ResourceTracker {
    private static final ResourceTracker.Cleanup<ResultSet> CLEAN_JAVA_SQL_RESULTSET = new ResourceTracker.Cleanup<ResultSet>() {
        public void close(ResultSet rs) throws SQLException {
            rs.close();
        }
    };
    private static final ResourceTracker.Cleanup<Statement> CLEAN_JAVA_SQL_STATEMENT = new ResourceTracker.Cleanup<Statement>() {
        public void close(Statement s) throws SQLException {
            s.close();
        }
    };
    private static final ResourceTracker.Cleanup<Connection> CLEAN_JAVA_SQL_CONNECTION = new ResourceTracker.Cleanup<Connection>() {
        public void close(Connection c) throws SQLException {
            c.close();
        }
    };
    private static final ResourceTracker.Cleanup<Reader> CLEAN_JAVA_IO_READER = new ResourceTracker.Cleanup<Reader>() {
        public void close(Reader reader) throws IOException {
            reader.close();
        }
    };
    private static final ResourceTracker.Cleanup<Writer> CLEAN_JAVA_IO_WRITER = new ResourceTracker.Cleanup<Writer>() {
        public void close(Writer writer) throws IOException {
            writer.close();
        }
    };
    private static final ResourceTracker.Cleanup<InputStream> CLEAN_JAVA_INPUT_STREAM = new ResourceTracker.Cleanup<InputStream>() {
        public void close(InputStream in) throws IOException {
            in.close();
        }
    };
    private static final ResourceTracker.Cleanup<OutputStream> CLEAN_JAVA_OUTPUT_STREAM = new ResourceTracker.Cleanup<OutputStream>() {
        public void close(OutputStream out) throws IOException {
            out.close();
        }
    };
    private final String description;
    private final ArrayList<Object> objects = new ArrayList();

    public ResourceTracker(String desc) {
        this.description = desc;
    }

    private void attach(Object object, ResourceTracker.Cleanup<?> cleaner) {
        this.objects.add(object);
        this.objects.add(cleaner);
    }

    public void attach(Statement stmt) {
        this.attach(stmt, CLEAN_JAVA_SQL_STATEMENT);
    }

    public void attach(ResultSet rs) {
        this.attach(rs, CLEAN_JAVA_SQL_RESULTSET);
    }

    public void attach(Connection conn) {
        this.attach(conn, CLEAN_JAVA_SQL_CONNECTION);
    }

    public void attach(Reader r) {
        this.attach(r, CLEAN_JAVA_IO_READER);
    }

    public void attach(Writer w) {
        this.attach(w, CLEAN_JAVA_IO_WRITER);
    }

    public void attach(InputStream in) {
        this.attach(in, CLEAN_JAVA_INPUT_STREAM);
    }

    public void attach(OutputStream out) {
        this.attach(out, CLEAN_JAVA_OUTPUT_STREAM);
    }

    public void clear() {
        while(!this.objects.isEmpty()) {
            try {
                this.close();
            } catch (Exception var2) {
                ;
            }
        }

    }

    private void close() throws Exception {
        while(!this.objects.isEmpty()) {
            this.closeLastResource();
        }

    }

    private void closeLastResource() throws Exception {
        int index = this.objects.size() - 1;
        ResourceTracker.Cleanup cleaner = (ResourceTracker.Cleanup)this.objects.remove(index--);
        Object resource = this.objects.remove(index);
        if(resource != null) {
            cleaner.close(resource);
        }

    }

    public int size() {
        return this.objects.size() / 2;
    }

    public String toString() {
        return "ResourceTracker[" + this.description + ']';
    }

    private interface Cleanup<T> {
        void close(T var1) throws Exception;
    }
}
