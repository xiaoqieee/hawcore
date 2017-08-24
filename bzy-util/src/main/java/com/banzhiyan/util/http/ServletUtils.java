package com.banzhiyan.util.http;

import com.banzhiyan.core.util.ResourceUtils;
import com.banzhiyan.logging.Logger;
import com.banzhiyan.logging.LoggerFactory;
import com.banzhiyan.util.GZIPResponseStream;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by xn025665 on 2017/8/24.
 */

public final class ServletUtils {
    private static final Logger logger = LoggerFactory.getLogger(ServletUtils.class);
    private static final String HTTP_CONTENT_LENGTH_HEADER = "Content-Length";
    private static final String HTTP_LAST_MODIFIED_HEADER = "Last-Modified";
    private static final String HTTP_EXPIRES_HEADER = "Expires";
    private static final String HTTP_CACHE_CONTROL_HEADER = "Cache-Control";
    private static final int DEFAULT_CACHE_TIMEOUT = 31556926;
    private static final Map<String, String> defaultMimeTypes = new HashMap(7, 1.0F);
    private static final Set<String> compressedMimeTypes = new HashSet(1);

    private ServletUtils() {
    }

    public static void writeBytesToResponse(byte[] data, boolean gzipEnabled, HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream out = selectOutputStream(request, response, gzipEnabled);
        Throwable var5 = null;

        try {
            FileCopyUtils.copy(data, out);
        } catch (Throwable var14) {
            var5 = var14;
            throw var14;
        } finally {
            if(out != null) {
                if(var5 != null) {
                    try {
                        out.close();
                    } catch (Throwable var13) {
                        var5.addSuppressed(var13);
                    }
                } else {
                    out.close();
                }
            }

        }

    }

    public static void writeResourcesToResponse(Set<String> filePaths, Integer cacheTimeout, boolean gzipEnabled, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(logger.isDebugEnabled()) {
            logger.debug("Attempting to GET resource...");
        }

        Resource[] resources = getRequestResources(filePaths);
        if(resources != null && resources.length != 0) {
            writeResourcesToResponse(resources, cacheTimeout, gzipEnabled, request, response);
        } else {
            if(logger.isDebugEnabled()) {
                logger.debug("Resources not found..");
            }

            response.sendError(404);
        }
    }

    public static void writeResourcesToResponse(Resource[] resources, Integer cacheTimeout, boolean gzipEnabled, HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepareResponse(request, response, resources, cacheTimeout);
        OutputStream out = selectOutputStream(request, response, gzipEnabled);
        Throwable var6 = null;

        try {
            Resource[] arr$ = resources;
            int len$ = resources.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Resource resource = arr$[i$];
                FileCopyUtils.copy(resource.getInputStream(), out);
            }
        } catch (Throwable var18) {
            var6 = var18;
            throw var18;
        } finally {
            if(out != null) {
                if(var6 != null) {
                    try {
                        out.close();
                    } catch (Throwable var17) {
                        var6.addSuppressed(var17);
                    }
                } else {
                    out.close();
                }
            }

        }

    }

    private static Resource[] getRequestResources(Set<String> filePaths) throws IOException {
        List<Resource> resources = new ArrayList(filePaths.size());
        Iterator i$ = filePaths.iterator();

        while(i$.hasNext()) {
            String path = (String)i$.next();
            Resource[] re = ResourceUtils.getResources(path);
            Resource[] arr$ = re;
            int len = re.length;

            for(int i = 0; i < len; ++i) {
                Resource r = arr$[i];
                resources.add(r);
            }
        }

        return (Resource[])resources.toArray(new Resource[resources.size()]);
    }

    private static void prepareResponse(HttpServletRequest request, HttpServletResponse response, Resource[] resources, Integer cacheTimeout) throws IOException {
        long lastModified = -1L;
        int contentLength = 0;
        String mimeType = null;
        String currentMimeType = null;
        Resource[] arr$ = resources;
        int len$ = resources.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Resource resource = arr$[i$];
            if(resource.lastModified() > lastModified) {
                lastModified = resource.lastModified();
            }

            String path = resource.getURL().getPath();
            HttpSession session = request.getSession(false);
            if(session != null) {
                currentMimeType = session.getServletContext().getMimeType(path);
            }

            if(currentMimeType == null) {
                String extension = path.substring(path.lastIndexOf(46));
                currentMimeType = (String)defaultMimeTypes.get(extension);
            }

            if(mimeType == null) {
                mimeType = currentMimeType;
            } else if(!mimeType.equals(currentMimeType)) {
                throw new MalformedURLException("Can't combine resources. All resources must be of the same mime type.");
            }

            contentLength = (int)((long)contentLength + resource.contentLength());
        }

        response.setContentType(mimeType);
        response.setHeader("Content-Length", Long.toString((long)contentLength));
        response.setDateHeader("Last-Modified", lastModified);
        if((cacheTimeout == null?31556926:cacheTimeout.intValue()) > 0) {
            configureCaching(response, cacheTimeout.intValue());
        }

    }

    public static OutputStream selectOutputStream(HttpServletRequest request, HttpServletResponse response, boolean gzipEnabled) throws IOException {
        String acceptEncoding = request.getHeader("Accept-Encoding");
        String mimeType = response.getContentType();
        if(gzipEnabled && org.apache.commons.lang3.StringUtils.isNotEmpty(acceptEncoding) && acceptEncoding.indexOf("gzip") > -1 && matchesCompressedMimeTypes(mimeType)) {
            if(logger.isDebugEnabled()) {
                logger.debug("Enabling GZIP compression for the current response.");
            }

            return new GZIPResponseStream(response);
        } else {
            return response.getOutputStream();
        }
    }

    public static boolean matchesCompressedMimeTypes(String mimeType) {
        PathMatcher pathMatcher = new AntPathMatcher();
        Iterator compressedMimeTypesIt = compressedMimeTypes.iterator();

        String compressedMimeType;
        do {
            if(!compressedMimeTypesIt.hasNext()) {
                return false;
            }

            compressedMimeType = (String)compressedMimeTypesIt.next();
        } while(!pathMatcher.match(compressedMimeType, mimeType));

        return true;
    }

    private static void configureCaching(HttpServletResponse response, int seconds) {
        response.setDateHeader("Expires", System.currentTimeMillis() + (long)seconds * 1000L);
        response.setHeader("Cache-Control", "max-age=" + seconds);
    }

    static {
        defaultMimeTypes.put(".css", "text/css");
        defaultMimeTypes.put(".gif", "image/gif");
        defaultMimeTypes.put(".ico", "image/vnd.microsoft.icon");
        defaultMimeTypes.put(".jpeg", "image/jpeg");
        defaultMimeTypes.put(".jpg", "image/jpeg");
        defaultMimeTypes.put(".js", "text/javascript");
        defaultMimeTypes.put(".png", "image/png");
        compressedMimeTypes.add("text/*");
    }
}

