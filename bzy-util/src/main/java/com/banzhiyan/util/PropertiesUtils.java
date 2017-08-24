package com.banzhiyan.util;

import com.banzhiyan.core.util.ResourceUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Properties;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class PropertiesUtils extends PropertiesLoaderUtils {
    public PropertiesUtils() {
    }

    public static Properties loadProperties(String filename) {
        Properties props = new Properties();

        try {
            Reader reader = ResourceUtils.getReader(filename);
            Throwable var3 = null;

            try {
                props.load(reader);
            } catch (Throwable var13) {
                var3 = var13;
                throw var13;
            } finally {
                if(reader != null) {
                    if(var3 != null) {
                        try {
                            reader.close();
                        } catch (Throwable var12) {
                            var3.addSuppressed(var12);
                        }
                    } else {
                        reader.close();
                    }
                }

            }
        } catch (Throwable var15) {
            ;
        }

        return props;
    }

    public static Writer toWriter(Properties properties) {
        StringBuilderWriter writer = new StringBuilderWriter();

        try {
            properties.store(writer, (String)null);
        } catch (IOException var6) {
            ;
        } finally {
            IOUtils.closeQuietly(writer);
        }

        return writer;
    }

    public static String toString(Properties properties) {
        Writer writer = toWriter(properties);
        return writer.toString();
    }

    public static Reader toReader(Properties properties) {
        String content = toString(properties);
        StringReader stringReader = new StringReader(content);
        return stringReader;
    }
}

