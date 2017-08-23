package com.banzhiyan.core.util;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class ResourceUtils {
    public static final String FILE_URL_PREFIX = "file:";
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    public static final String CLASSPATH_FILE_PREFIX = "classpathfile:";

    public ResourceUtils() {
    }

    public static Resource[] getResources(String locationPattern) throws IOException {
        Assert.notNull(locationPattern, "Resource location must not be null");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(ClassUtils.getDefaultClassLoader());
        if(locationPattern.startsWith("classpathfile:")) {
            String path = locationPattern.substring("classpathfile:".length());
            URL url = ClassUtils.getDefaultClassLoader().getResource("");
            locationPattern = "file:" + url.getPath() + path;
        }

        return resolver.getResources(locationPattern);
    }

    public static FileSystemResource getFileSystemResource(String locationPattern) throws IOException {
        return new FileSystemResource(org.springframework.util.ResourceUtils.getFile(locationPattern));
    }

    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        Assert.notNull(resourceUrl, "Resource URL must not be null");
        String protocol = resourceUrl.getProtocol();
        if(!"file".equals(protocol) && !"vfsfile".equals(protocol) && !"vfszip".equals(protocol)) {
            throw new FileNotFoundException(description + " cannot be resolved to absolute file path " + "because it does not reside in the file system: " + resourceUrl);
        } else {
            try {
                File file;
                if(!"vfsfile".equals(protocol) && !"vfszip".equals(protocol)) {
                    file = new File(org.springframework.util.ResourceUtils.toURI(resourceUrl).getSchemeSpecificPart());
                } else {
                    file = new File(resourceUrl.getFile());
                }

                return file;
            } catch (URISyntaxException var4) {
                return new File(resourceUrl.getFile());
            }
        }
    }

    public static InputStream getResourceAsStream(String filename) throws IOException {
        Assert.hasText(filename, "filename must not be null");
        if(!filename.startsWith("classpath:") && !filename.startsWith("file:") && !filename.startsWith("classpathfile:")) {
            String name = filename.startsWith("/")?filename:"/" + filename;
            String serverHome = System.getProperty("server.home");
            if(serverHome != null) {
                try {
                    return getInputStream("file:///" + serverHome + "/conf" + name);
                } catch (IOException var5) {
                    ;
                }
            }

            try {
                return getInputStream("classpath:" + name);
            } catch (IOException var4) {
                return getInputStream("classpath:/conf" + name);
            }
        } else {
            return getInputStream(filename);
        }
    }

    private static InputStream getInputStream(String path) throws IOException {
        return getResources(path)[0].getInputStream();
    }

    public static Reader getReader(String filename) throws IOException {
        return getReader(filename, "UTF-8");
    }

    public static Reader getReader(String filename, String encoding) throws IOException {
        return new InputStreamReader(getResourceAsStream(filename), encoding == null?"UTF-8":encoding);
    }
}

