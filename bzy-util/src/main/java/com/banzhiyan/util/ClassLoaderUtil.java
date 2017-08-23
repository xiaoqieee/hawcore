package com.banzhiyan.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xn025665 on 2017/8/23.
 */

public abstract class ClassLoaderUtil {
    public static final String CLASS_FILE_EXTENSION = ".class";

    public ClassLoaderUtil() {
    }

    public static URL getResource(ClassLoader classLoader, String resourcePath) {
        URL resourceURL = null;
        String resolvedName = normalizeResourcePath(resourcePath);
        if(resolvedName != null) {
            resourceURL = classLoader.getResource(resolvedName);
        }

        return resourceURL;
    }

    public static List<URL> getResourcesList(String resourcePath) throws IOException {
        return getResourcesList(currentClassLoader(), resourcePath);
    }

    public static List<URL> getResourcesList(ClassLoader classLoader, String resourcePath) throws IOException {
        String resolvedName = normalizeResourcePath(resourcePath);
        if(resolvedName == null) {
            return Collections.emptyList();
        } else {
            Enumeration<URL> resources = classLoader.getResources(resolvedName);
            LinkedList resourcesList = Lists.newLinkedList();

            while(resources.hasMoreElements()) {
                URL resource = (URL)resources.nextElement();
                resourcesList.add(resource);
            }

            return Collections.unmodifiableList(resourcesList);
        }
    }

    public static URL getResource(String resourcePath) {
        return getResource(currentClassLoader(), resourcePath);
    }

    public static String normalizeResourcePath(String resourcePath) {
        return normalizae(resourcePath, 1);
    }

    public static String normalizeClassName(String classCanonicalName) {
        return normalizae(classCanonicalName, 2);
    }

    public static String normalizePackageName(String packageName) {
        return normalizae(packageName, 3);
    }

    static String normalizae(String name, int mode) {
        if(name == null) {
            return name;
        } else {
            String normalizedName;
            for(normalizedName = name.trim(); normalizedName.contains("\\"); normalizedName = normalizedName.replace('\\', '/')) {
                ;
            }

            while(normalizedName.contains("//")) {
                normalizedName = org.apache.commons.lang3.StringUtils.replace(normalizedName, "//", "/");
            }

            while(normalizedName.startsWith("/")) {
                normalizedName = normalizedName.substring(1);
            }

            switch(mode) {
                case 1:
                default:
                    break;
                case 2:
                    if(normalizedName.endsWith(".class")) {
                        normalizedName = org.apache.commons.lang3.StringUtils.substringBefore(normalizedName, ".class");
                    }
                    break;
                case 3:
                    normalizedName = org.apache.commons.lang3.StringUtils.replace(normalizedName, "/", ".");
                    if(normalizedName.endsWith(".")) {
                        normalizedName = normalizedName.substring(0, normalizedName.length() - 1);
                    }
            }

            return normalizedName;
        }
    }

    public static ClassLoader currentClassLoader() {
        ClassLoader classLoader = null;

        try {
            classLoader = getThreadContextClassLoader();
        } catch (Exception var2) {
            ;
        }

        if(classLoader == null) {
            Class<?> currentClass = ClassLoaderUtil.class;
            classLoader = currentClass.getClassLoader();
        }

        if(classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        return classLoader;
    }

    public static ClassLoader getThreadContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Package getPackage(String packageName) throws RuntimeException {
        String name = normalizePackageName(packageName);
        return Package.getPackage(name);
    }

    public static File findClassFile(Class<?> sourceClass) {
        ClassLoader classLoader = sourceClass.getClassLoader();
        String classFileRelativePath = getClassFileRelativePath(sourceClass);
        if(classLoader == null) {
            return null;
        } else {
            URL resourceURL = getResource(classLoader, classFileRelativePath);
            return !"file".equals(resourceURL.getProtocol())?null:new File(resourceURL.getPath());
        }
    }

    public static String findClassFileAbsolutePath(Class<?> sourceClass) {
        File classFile = findClassFile(sourceClass);
        return classFile == null?null:classFile.getAbsolutePath();
    }

    public static String getClassFileRelativePath(Class<?> sourceClass) {
        String className = sourceClass.getName();
        String relativePath = "/" + className.replace('.', '/') + ".class";
        return relativePath;
    }

    public static Class<?> loadClass(ClassLoader classLoader, String className) {
        String resolvedClassName = normalizeClassName(className);

        try {
            return classLoader.loadClass(resolvedClassName);
        } catch (Throwable var4) {
            return null;
        }
    }

    public static Class<?> findLoadedClass(ClassLoader classLoader, String className) {
        String normalizedClassName = normalizeClassName(className);
        Method method = ReflectionUtil.getMethod(ClassLoader.class, "findLoadedClass", new Class[]{String.class});
        Class loadedClass = null;

        try {
            method.setAccessible(true);
            loadedClass = (Class)method.invoke(classLoader, new Object[]{normalizedClassName});
        } catch (Exception var6) {
            ;
        }

        return loadedClass;
    }

    public static Class<?> findClass(ClassLoader classLoader, File classFile) {
        Class<?> klass = null;

        for(ClassLoader targetClassLoader = classLoader; targetClassLoader != null; targetClassLoader = targetClassLoader.getParent()) {
            URL classPath = getResource(targetClassLoader, "/");
            if(classPath != null) {
                String protocol = classPath.getProtocol();
                if("file".equals(protocol)) {
                    String classFileAbsolutePath = classFile.getAbsolutePath();
                    String classPathAbsolutePath = (new File(classPath.getFile())).getAbsolutePath();
                    if(classFileAbsolutePath.contains(classPathAbsolutePath)) {
                        String classFileRelativePath = classFileAbsolutePath.replace(classPathAbsolutePath, "");
                        String className = normalizeClassName(classFileRelativePath);

                        try {
                            klass = classLoader.loadClass(className);
                        } catch (ClassNotFoundException var11) {
                            ;
                        }
                    }
                }
            }

            if(klass != null) {
                break;
            }
        }

        return klass;
    }

    public static String asPackageResourceName(String packageName) {
        String normalizedPackageName = normalizePackageName(packageName);
        String packagePathInJar = normalizedPackageName.replace('.', '/').concat("/");
        return packagePathInJar;
    }
}
