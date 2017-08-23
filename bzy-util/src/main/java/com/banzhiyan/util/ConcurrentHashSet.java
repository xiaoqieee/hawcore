package com.banzhiyan.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>, Serializable {
    private static final long serialVersionUID = 8213146222352677139L;
    private static final Object PRESENT = new Object();
    private final ConcurrentHashMap<E, Object> map;

    public ConcurrentHashSet() {
        this.map = new ConcurrentHashMap();
    }

    public ConcurrentHashSet(int initialCapacity) {
        this.map = new ConcurrentHashMap(initialCapacity);
    }

    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean contains(Object o) {
        return this.map.containsKey(o);
    }

    public boolean add(E e) {
        return this.map.put(e, PRESENT) == null;
    }

    public boolean remove(Object o) {
        return this.map.remove(o) == PRESENT;
    }

    public void clear() {
        this.map.clear();
    }
}

