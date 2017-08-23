package com.banzhiyan.util;

import com.google.common.collect.Lists;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class ReflectionUtil {
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    private static final ConcurrentMap<MethodDescriptor, WeakReference<Method>> methodCache = new ConcurrentHashMap();
    private static final int CACHE_THRESHOLD = 30;

    public ReflectionUtil() {
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] methodArgumentTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, methodArgumentTypes);
        } catch (Exception var4) {
            return null;
        }
    }

    private static Method findMethod(Class<?> type, String methodName, Object[] arguments) {
        Object[] args = arguments != null?arguments:new Object[0];
        List<Class<?>> methodArgumentTypesList = Lists.newArrayListWithCapacity(args.length);
        Object[] arr$ = args;
        int len$ = args.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Object argument = arr$[i$];
            methodArgumentTypesList.add(argument.getClass());
        }

        return findMethod(type, methodName, (Class[])methodArgumentTypesList.toArray(new Class[args.length]));
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>[] methodArgumentTypes) {
        Method method = null;
        Class classToFind = clazz;

        while(classToFind != null) {
            try {
                method = classToFind.getDeclaredMethod(methodName, methodArgumentTypes);
            } catch (Exception var6) {
                method = null;
                classToFind = classToFind.getSuperclass();
            }

            if(method != null) {
                break;
            }
        }

        return method;
    }

    public static <T> T invokeMethod(Object source, String methodName, Object[] arguments) {
        Class<?> type = source.getClass();
        Method method = findMethod(type, methodName, arguments);
        return (T) (method == null? null:invokeMethod(source, method, arguments));
    }

    public static <T> T invokeStaticMethod(Class<?> type, String methodName, Object[] arguments) {
        Method method = findMethod(type, methodName, arguments);
        return (T)(method == null?null:invokeMethod((Object)null, (Method)method, arguments));
    }

    private static Object invokeMethod(Object source, Method method, Object[] arguments) {
        boolean accessible = method.isAccessible();

        Object var4;
        try {
            method.setAccessible(true);
            var4 = method.invoke(source, arguments);
        } catch (Exception var8) {
            throw new IllegalStateException(var8);
        } finally {
            method.setAccessible(accessible);
        }

        return var4;
    }

    public static Map<String, Object> describe(Object object) {
        if(object == null) {
            return new HashMap(0);
        } else {
            Field[] declaredFields = object.getClass().getDeclaredFields();
            Map<String, Object> props = new HashMap(declaredFields.length);
            Field[] arr$ = declaredFields;
            int len$ = declaredFields.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Field field = arr$[i$];
                ReflectionUtils.makeAccessible(field);

                try {
                    props.put(field.getName(), field.get(object));
                } catch (IllegalAccessException var8) {
                    ;
                }
            }

            return props;
        }
    }

    public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
        Assert.notNull(object);
        Assert.hasText(propertyName);
        return getDeclaredField(object.getClass(), propertyName);
    }

    public static Field getDeclaredField(Class<?> clazz, String propertyName) throws NoSuchFieldException {
        Assert.notNull(clazz);
        Assert.hasText(propertyName);
        Class currentClz = clazz;

        while(currentClz != null) {
            try {
                return currentClz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException var4) {
                currentClz = currentClz.getSuperclass();
            }
        }

        throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
    }

    public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {
        Assert.notNull(object);
        Assert.hasText(propertyName);
        Field field = getDeclaredField(object, propertyName);
        ReflectionUtils.makeAccessible(field);

        try {
            return field.get(object);
        } catch (IllegalAccessException var4) {
            return null;
        }
    }

    public static void forceSetProperty(Object object, String propertyName, Object newValue) throws NoSuchFieldException {
        Assert.notNull(object);
        Assert.hasText(propertyName);
        Field field = getDeclaredField(object, propertyName);
        ReflectionUtils.makeAccessible(field);

        try {
            field.set(object, newValue);
        } catch (IllegalAccessException var5) {
            ;
        }

    }

    public static void forceSetProperties(Object object, Map<String, Object> props) throws NoSuchFieldException {
        Iterator i$ = props.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, Object> prop = (Map.Entry)i$.next();
            forceSetProperty(object, (String)prop.getKey(), prop.getValue());
        }

    }

    public static Object invokeMethod(Object obj, String methodName, Object[] args, Class<?>[] argTypes) throws NoSuchMethodException, IllegalArgumentException {
        Assert.notNull(obj);
        Assert.hasText(methodName);
        argumentSanityCheck(args, argTypes);
        Class<?> clazz = obj.getClass();
        Method method = findMatchingMethod(clazz, methodName, argTypes);
        if(method == null) {
            throw new NoSuchMethodException(String.format("No such method: %s.%s(%s)", new Object[]{clazz.getSimpleName(), methodName, org.apache.commons.lang3.StringUtils.join(argTypes, ", ")}));
        } else {
            ReflectionUtils.makeAccessible(method);

            try {
                return method.invoke(obj, args);
            } catch (Exception var7) {
                ReflectionUtils.handleReflectionException(var7);
                return null;
            }
        }
    }

    private static void argumentSanityCheck(Object[] args, Class<?>[] argTypes) throws IllegalArgumentException {
        Assert.notNull(args, "args should not be null");
        Assert.notNull(argTypes, "argTypes should not be null");
        Assert.isTrue(args.length == argTypes.length, "the arrays of arguments and types should have the same length");

        for(int i = 0; i < args.length; ++i) {
            Class<?> argType = argTypes[i];
            Object arg = args[i];
            if(argType == null) {
                Assert.isTrue(arg == null || !arg.getClass().isPrimitive());
            } else {
                Assert.isTrue(arg == null || argType.isInstance(arg));
            }
        }

    }

    public static Object invokeMethod(Class<?> clazz, String methodName, Object[] args) throws NoSuchMethodException {
        Class<?>[] types = null;
        if(args != null) {
            types = new Class[args.length];

            for(int i = 0; i < args.length; ++i) {
                if(args[i] == null) {
                    types[i] = null;
                } else {
                    types[i] = args[i].getClass();
                }
            }
        }

        return invokeMethod(clazz, methodName, args, types);
    }

    public static Object invokeMethod(Class<?> clazz, String methodName, Object[] args, Class<?>[] argTypes) throws NoSuchMethodException {
        Assert.hasText(methodName);
        argumentSanityCheck(args, argTypes);
        Method method = findMatchingMethod(clazz, methodName, argTypes);
        if(method == null) {
            throw new NoSuchMethodException(String.format("No such method: %s.%s(%s)", new Object[]{clazz.getSimpleName(), methodName, org.apache.commons.lang3.StringUtils.join(argTypes, ", ")}));
        } else {
            Object obj = null;
            if(!Modifier.isStatic(method.getModifiers())) {
                try {
                    obj = clazz.newInstance();
                } catch (Exception var8) {
                    ReflectionUtils.handleReflectionException(var8);
                }
            }

            ReflectionUtils.makeAccessible(method);

            try {
                return method.invoke(obj, args);
            } catch (Exception var7) {
                ReflectionUtils.handleReflectionException(var7);
                return null;
            }
        }
    }

    private static Method findMatchingMethod(Class<?> clazz, String methodName, Class<?>[] argTypes) {
        Assert.notNull(clazz);
        Assert.hasText(methodName);
        if(argTypes == null) {
            argTypes = EMPTY_CLASS_ARRAY;
        }

        try {
            return clazz.getDeclaredMethod(methodName, argTypes);
        } catch (NoSuchMethodException var14) {
            ReflectionUtil.MethodDescriptor md = new ReflectionUtil.MethodDescriptor(clazz, methodName, argTypes, false);
            Method theMethod = getCachedMethod(md);
            if(theMethod != null) {
                return theMethod;
            } else {
                for(int minMatchCost = 2147483647; clazz != null; clazz = clazz.getSuperclass()) {
                    Method[] methods = clazz.getDeclaredMethods();
                    Method[] arr$ = methods;
                    int len$ = methods.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        Method method = arr$[i$];
                        if(method.getName().equals(methodName)) {
                            Class<?>[] paramTypes = method.getParameterTypes();
                            if(paramTypes.length == argTypes.length) {
                                int currentCost = getTypeMatchCost(paramTypes, argTypes);
                                if(currentCost < minMatchCost) {
                                    minMatchCost = currentCost;
                                    theMethod = method;
                                }
                            }
                        }
                    }
                }

                if(theMethod != null) {
                    cacheMethod(md, theMethod);
                }

                return theMethod;
            }
        }
    }

    public static int getTypeMatchCost(Class<?>[] paramTypes, Class<?>[] argTypes) {
        int cost = 0;

        for(int i = 0; i < paramTypes.length; ++i) {
            if(!isAssignmentCompatible(paramTypes[i], argTypes[i])) {
                return 2147483647;
            }

            if(argTypes[i] != null) {
                Class<?> paramType = paramTypes[i];
                Class superClass = argTypes[i].getSuperclass();

                while(superClass != null) {
                    if(paramType.equals(superClass)) {
                        cost += 2;
                        superClass = null;
                    } else if(isAssignmentCompatible(paramType, superClass)) {
                        cost += 2;
                        superClass = superClass.getSuperclass();
                    } else {
                        superClass = null;
                    }
                }

                if(paramType.isInterface()) {
                    ++cost;
                }
            }
        }

        return cost;
    }

    public static final boolean isAssignmentCompatible(Class<?> destType, Class<?> srcType) {
        Assert.notNull(destType);
        if(srcType == null) {
            return !destType.isPrimitive();
        } else if(destType.isAssignableFrom(srcType)) {
            return true;
        } else {
            if(destType.isPrimitive()) {
                Class<?> destWrapperClazz = getPrimitiveWrapper(destType);
                if(destWrapperClazz != null) {
                    return destWrapperClazz.equals(srcType);
                }
            }

            return false;
        }
    }

    public static Class<?> getPrimitiveType(Class<?> wrapperType) {
        return Integer.class.equals(wrapperType)?Integer.TYPE:(Long.class.equals(wrapperType)?Long.TYPE:(Float.class.equals(wrapperType)?Float.TYPE:(Double.class.equals(wrapperType)?Double.TYPE:(Short.class.equals(wrapperType)?Short.TYPE:(Boolean.class.equals(wrapperType)?Boolean.TYPE:(Character.class.equals(wrapperType)?Character.TYPE:(Byte.class.equals(wrapperType)?Byte.TYPE:null)))))));
    }

    public static Class<?> getPrimitiveWrapper(Class<?> primitiveType) {
        return Boolean.TYPE.equals(primitiveType)?Boolean.class:(Float.TYPE.equals(primitiveType)?Float.class:(Long.TYPE.equals(primitiveType)?Long.class:(Integer.TYPE.equals(primitiveType)?Integer.class:(Short.TYPE.equals(primitiveType)?Short.class:(Byte.TYPE.equals(primitiveType)?Byte.class:(Double.TYPE.equals(primitiveType)?Double.class:(Character.TYPE.equals(primitiveType)?Character.class:null)))))));
    }

    public static List<Field> getFieldsByType(Object object, Class<?> type) {
        List<Field> list = new ArrayList();
        Field[] fields = object.getClass().getDeclaredFields();
        Field[] arr$ = fields;
        int len$ = fields.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Field field = arr$[i$];
            if(field.getType().isAssignableFrom(type)) {
                list.add(field);
            }
        }

        return list;
    }

    public static Class<?> getPropertyType(Class<?> type, String name) throws NoSuchFieldException {
        return getDeclaredField(type, name).getType();
    }

    public static String getGetterName(Class<?> type, String fieldName) {
        Assert.notNull(type, "Type required");
        Assert.hasText(fieldName, "FieldName required");
        return "boolean".equals(type.getName())?"is" + org.apache.commons.lang3.StringUtils.capitalize(fieldName):"get" + org.apache.commons.lang3.StringUtils.capitalize(fieldName);
    }

    public static Method getGetterMethod(Class<?> type, String fieldName) {
        try {
            return type.getMethod(getGetterName(type, fieldName), EMPTY_CLASS_ARRAY);
        } catch (NoSuchMethodException var3) {
            return null;
        }
    }

    private static Method getCachedMethod(ReflectionUtil.MethodDescriptor md) {
        Reference<Method> ref = (Reference)methodCache.get(md);
        return ref != null?(Method)ref.get():null;
    }

    private static void cacheMethod(ReflectionUtil.MethodDescriptor md, Method method) {
        if(methodCache.size() > 30) {
            methodCache.clear();
        }

        methodCache.put(md, new WeakReference(method));
    }

    private static class MethodDescriptor {
        private Class<?> clazz;
        private String methodName;
        private Class<?>[] paramTypes;
        private boolean exact;
        private int hashCode;

        public MethodDescriptor(Class<?> clazz, String methodName, Class<?>[] paramTypes, boolean exact) {
            if(clazz == null) {
                throw new IllegalArgumentException("Class cannot be null");
            } else if(methodName == null) {
                throw new IllegalArgumentException("Method Name cannot be null");
            } else {
                if(paramTypes == null) {
                    paramTypes = ReflectionUtil.EMPTY_CLASS_ARRAY;
                }

                this.clazz = clazz;
                this.methodName = methodName;
                this.paramTypes = paramTypes;
                this.exact = exact;
                this.hashCode = methodName.length() << 16 | paramTypes.length;
            }
        }

        public boolean equals(Object obj) {
            if(!(obj instanceof ReflectionUtil.MethodDescriptor)) {
                return false;
            } else {
                ReflectionUtil.MethodDescriptor md = (ReflectionUtil.MethodDescriptor)obj;
                return this.exact == md.exact && this.methodName.equals(md.methodName) && this.clazz.equals(md.clazz) && Arrays.equals(this.paramTypes, md.paramTypes);
            }
        }

        public int hashCode() {
            return this.hashCode;
        }
    }
}

