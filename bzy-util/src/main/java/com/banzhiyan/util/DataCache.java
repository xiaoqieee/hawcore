package com.banzhiyan.util;

import org.springframework.util.Assert;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xn025665 on 2017/8/23.
 */

public final class DataCache<K, V> {
    private final ConcurrentMap<K, Entry<K, V>> cachedData;
    private final ReferenceQueue<V> queue;
    private final ReentrantLock lock;
    private String name;
    private long maxAge;
    private long nextReapTime;

    public DataCache(String name) {
        this(name, 0L);
    }

    public DataCache(String name, long maxAge) {
        this.cachedData = new ConcurrentHashMap();
        this.queue = new ReferenceQueue();
        this.lock = new ReentrantLock();
        this.nextReapTime = 0L;
        this.maxAge = maxAge > 0L?maxAge:0L;
        this.name = name;
    }

    public V get(Object key) {
        Assert.notNull(key);
        V value = null;
        this.lock.lock();

        try {
            this.flushEntries();
            DataCache.Entry<K, V> entry = (DataCache.Entry)this.cachedData.get(key);
            if(entry != null) {
                if(entry.hasExpired()) {
                    this.discard(entry);
                } else {
                    value = entry.get();
                }
            }
        } finally {
            this.lock.unlock();
        }

        return value;
    }

    public V put(K key, V value) {
        Assert.notNull(key);
        Assert.notNull(value);
        return this.put(key, value, this.maxAge);
    }

    public V putIfAbsent(K key, V value) {
        Assert.notNull(key);
        Assert.notNull(value);
        this.lock.lock();

        V var4;
        try {
            V oldValue = this.get(key);
            if(oldValue != null) {
                var4 = oldValue;
                return var4;
            }

            this.put(key, value);
            var4 = value;
        } finally {
            this.lock.unlock();
        }

        return var4;
    }

    private V put(K key, V value, long maxage) {
        this.lock.lock();

        V var11;
        try {
            long now = System.currentTimeMillis();
            long max = maxage > 0L?now + maxage:0L;
            DataCache.Entry<K, V> entry = new DataCache.Entry(key, value, this.queue, max);
            if(max > 0L && this.nextReapTime == 0L) {
                this.nextReapTime = max;
            }

            DataCache.Entry<K, V> oldEntry = (DataCache.Entry)this.cachedData.put(key, entry);
            var11 = oldEntry == null?null:oldEntry.get();
        } finally {
            this.lock.unlock();
        }

        return var11;
    }

    private void discard(DataCache.Entry<K, V> entry) {
        DataCache.Entry<K, V> curr = (DataCache.Entry)this.cachedData.get(entry.key);
        if(curr == entry) {
            this.cachedData.remove(entry.key);
        }

    }

    private void flushEntries() {
        DataCache.Entry entry = null;

        while((entry = (DataCache.Entry)this.queue.poll()) != null) {
            this.discard(entry);
        }

        long now = System.currentTimeMillis();
        if(this.maxAge > 0L && this.nextReapTime > 0L && this.nextReapTime < now) {
            Iterator i$ = this.cachedData.values().iterator();

            while(i$.hasNext()) {
                DataCache.Entry<K, V> e = (DataCache.Entry)i$.next();
                if(e.hasExpired()) {
                    this.discard(e);
                }
            }

            this.nextReapTime = this.cachedData.size() > 0?System.currentTimeMillis() + this.maxAge:0L;
        }

    }

    public Set<K> keySet() {
        this.lock.lock();

        Set var1;
        try {
            this.flushEntries();
            var1 = this.cachedData.keySet();
        } finally {
            this.lock.unlock();
        }

        return var1;
    }

    public V remove(Object key) {
        V value = null;
        this.lock.lock();

        V var4;
        try {
            this.flushEntries();
            DataCache.Entry<K, V> entry = (DataCache.Entry)this.cachedData.remove(key);
            if(entry != null) {
                if(entry.hasExpired()) {
                    this.discard(entry);
                } else {
                    value = entry.get();
                }
            }

            var4 = value;
        } finally {
            this.lock.unlock();
        }

        return var4;
    }

    public void clear() {
        this.lock.lock();

        try {
            this.cachedData.clear();
            this.nextReapTime = 0L;
            this.flushEntries();
        } finally {
            this.lock.unlock();
        }

    }

    public int size() {
        this.lock.lock();

        int var1;
        try {
            this.flushEntries();
            var1 = this.cachedData.size();
        } finally {
            this.lock.unlock();
        }

        return var1;
    }

    public String toString() {
        return "DataCache[" + this.name + " sz=" + this.size() + ']';
    }

    private static class Entry<K, V> extends SoftReference<V> {
        final K key;
        final long expiry;

        private Entry(K key, V value, ReferenceQueue<V> queue, long maxTime) {
            super(value, queue);
            this.key = key;
            this.expiry = maxTime;
        }

        boolean hasExpired() {
            return this.expiry > 0L && System.currentTimeMillis() >= this.expiry;
        }
    }
}

