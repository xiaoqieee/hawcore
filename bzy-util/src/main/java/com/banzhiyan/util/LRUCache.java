package com.banzhiyan.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 5760874835823396792L;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int DEFAULT_MAX_CAPACITY = 1024;
    private final int maxCapacity;
    private final Lock lock;

    public LRUCache() {
        this(1024);
    }

    public LRUCache(int maxCapacity) {
        super(16, 0.75F, true);
        this.lock = new ReentrantLock();
        this.maxCapacity = maxCapacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > this.maxCapacity;
    }

    public boolean containsKey(Object key) {
        this.lock.lock();

        boolean var2;
        try {
            var2 = super.containsKey(key);
        } finally {
            this.lock.unlock();
        }

        return var2;
    }

    public boolean containsValue(Object value) {
        this.lock.lock();

        boolean var2;
        try {
            var2 = super.containsValue(value);
        } finally {
            this.lock.unlock();
        }

        return var2;
    }

    public V get(Object key) {
        this.lock.lock();

        Object var2;
        try {
            var2 = super.get(key);
        } finally {
            this.lock.unlock();
        }

        return (V)var2;
    }

    public V put(K key, V value) {
        this.lock.lock();

        Object var3;
        try {
            var3 = super.put(key, value);
        } finally {
            this.lock.unlock();
        }

        return (V)var3;
    }

    public V remove(Object key) {
        this.lock.lock();

        Object var2;
        try {
            var2 = super.remove(key);
        } finally {
            this.lock.unlock();
        }

        return (V)var2;
    }

    public int size() {
        this.lock.lock();

        int var1;
        try {
            var1 = super.size();
        } finally {
            this.lock.unlock();
        }

        return var1;
    }

    public void clear() {
        this.lock.lock();

        try {
            super.clear();
        } finally {
            this.lock.unlock();
        }

    }

    public Set<K> keySet() {
        this.lock.lock();

        Set var1;
        try {
            var1 = super.keySet();
        } finally {
            this.lock.unlock();
        }

        return var1;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }
}

