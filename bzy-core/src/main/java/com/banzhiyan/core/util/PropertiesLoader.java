package com.banzhiyan.core.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xn025665 on 2017/8/23.
 */

public final class PropertiesLoader {
    private static final String WEB_RES_LOCATION = "classpath:/";
    private static final String APP_TEST_RES_LOCATION = "classpath:/conf/";
    private static final String APP_PRD_RES_LOCATION = "file:///" + System.getProperty("server.home") + "/conf/";
    private static final List<String> LOCATIONS = new ArrayList();
    private static final Properties PROPERTIES = new Properties();
    private static final AtomicBoolean WEB_PROJECT = new AtomicBoolean(false);

    private PropertiesLoader() {
    }

    private static void isWebProject() {
        try {
            Class.forName("ooh.bravo.web.base.controller.BaseController");
            WEB_PROJECT.set(true);
        } catch (Throwable var1) {
            ;
        }

    }

    private static void initializeLocations() {
        String location;
        if(WEB_PROJECT.get()) {
            location = "classpath:/";
        } else if(System.getProperty("server.home") == null) {
            location = "classpath:/conf/";
        } else {
            location = APP_PRD_RES_LOCATION;
        }

        String[] filenames = loadFilenames(location);
        String[] arr$ = filenames;
        int len$ = filenames.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String filename = arr$[i$];
            LOCATIONS.add(location + filename);
        }

        System.out.println("Loading properties file: " + LOCATIONS);
    }

    private static String[] loadFilenames(String resourceLocation) {
        return getDirectory(resourceLocation).list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String filename = name.trim().toLowerCase();
                return filename.endsWith(".properties") && !filename.contains("-filter");
            }
        });
    }

    private static File getDirectory(String resourceLocation) {
        if(resourceLocation.startsWith("classpath:")) {
            try {
                return ResourceUtils.getResources(resourceLocation)[0].getFile();
            } catch (IOException var2) {
                throw new RuntimeException(var2);
            }
        } else {
            return new File(resourceLocation.substring("file:".length()));
        }
    }

    private static void initializeProperties() {
        mergePropertiesIntoMap(System.getProperties(), PROPERTIES);
        Iterator i$ = LOCATIONS.iterator();

        while(i$.hasNext()) {
            String location = (String)i$.next();
            mergePropertiesIntoMap(load(location), PROPERTIES);
        }

    }

    private static Properties load(String filename) {
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

    private static  void mergePropertiesIntoMap(Properties props, Map<Object, Object> map) {
        String key;
        Object value;
        for(Enumeration en = props.propertyNames(); en.hasMoreElements(); map.put(key, value)) {
            key = (String)en.nextElement();
            value = props.getProperty(key);
            if(value == null) {
                value = props.get(key);
            }
        }

    }

    public static List<String> getLocations() {
        return Collections.unmodifiableList(LOCATIONS);
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    static {
        isWebProject();
        initializeLocations();
        initializeProperties();
    }
}
