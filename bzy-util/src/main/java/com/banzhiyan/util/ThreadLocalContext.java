package com.banzhiyan.util;



import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class ThreadLocalContext {
    private static final ThreadLocalContext INSTANCE = new ThreadLocalContext();
    private final ThreadLocalContext.ThreadLocalImpl threadLocal = new ThreadLocalContext.ThreadLocalImpl();

    public static ThreadLocalContext getInstance() {
        return INSTANCE;
    }

    private ThreadLocalContext() {
    }

    public Object set(String key, Object value) {
        Map<String, Object> context = (Map)this.threadLocal.get();
        return context.put(key, value);
    }

    public <T> T get(String key) {
        Map<String, Object> context = (Map)this.threadLocal.get();
        return (T)context.get(key);
    }

    public <T> T remove(String key) {
        Map<String, Object> context = (Map)this.threadLocal.get();
        return (T)context.remove(key);
    }

    public <T> T remove(Object source) {
        Map<String, Object> context = (Map)this.threadLocal.get();
        Iterator i$ = context.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)i$.next();
            if(source.equals(entry.getValue())) {
                String key = (String)entry.getKey();
                if(key != null) {
                    return (T)context.remove(key);
                }
            }
        }

        return null;
    }

    public void remove() {
        ((Map)this.threadLocal.get()).clear();
        this.threadLocal.remove();
    }

    private static class ThreadLocalImpl extends ThreadLocal<Map<String, Object>> {
        private ThreadLocalImpl() {
        }

        protected Map<String, Object> initialValue() {
            return Maps.newConcurrentMap();
        }
    }
}

