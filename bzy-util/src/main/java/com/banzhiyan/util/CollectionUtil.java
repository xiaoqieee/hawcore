package com.banzhiyan.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class CollectionUtil {
    public CollectionUtil() {
    }

    public static <E> Set<E> createListedSet() {
        return new CollectionUtil.ListedSet();
    }

    public static <T> boolean isEmpty(Iterable<T> iterable) {
        if(iterable == null) {
            return true;
        } else {
            Iterator<T> iterator = iterable.iterator();
            return !iterator.hasNext();
        }
    }

    public static boolean isNotEmpty(Iterable<?> iterable) {
        return !isEmpty(iterable);
    }

    public static <E> List<E> toList(Enumeration<E> enumeration) {
        if(enumeration == null) {
            return Collections.emptyList();
        } else {
            LinkedList list = Lists.newLinkedList();

            while(enumeration.hasMoreElements()) {
                list.add(enumeration.nextElement());
            }

            return Collections.unmodifiableList(list);
        }
    }

    public static Map<String, String> toMap(Properties properties) {
        if(properties != null && !properties.isEmpty()) {
            Map<String, String> map = Maps.newHashMap();
            Iterator i$ = properties.entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<Object, Object> entry = (Map.Entry)i$.next();
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                map.put(key, value);
            }

            return Collections.unmodifiableMap(map);
        } else {
            return Collections.emptyMap();
        }
    }

    public static class ListedSet<E> implements List<E>, Set<E> {
        private List<E> delegate = new LinkedList();

        public ListedSet() {
        }

        public Iterator<E> iterator() {
            return this.delegate.iterator();
        }

        public int size() {
            return this.delegate.size();
        }

        public void add(int index, E element) {
            if(element == null) {
                throw new RuntimeException("The nulling object must not be inserted!");
            } else if(!this.delegate.contains(element)) {
                this.delegate.add(index, element);
            }
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            if(c == null) {
                throw new RuntimeException("The nulling objects must not be inserted!");
            } else if(c.isEmpty()) {
                return false;
            } else {
                Iterator iterator = c.iterator();

                while(iterator.hasNext()) {
                    E element = (E)iterator.next();
                    this.add(index, element);
                }

                return true;
            }
        }

        public E get(int index) {
            return this.delegate.get(index);
        }

        public int indexOf(Object o) {
            return this.delegate.indexOf(o);
        }

        public int lastIndexOf(Object o) {
            return this.delegate.lastIndexOf(o);
        }

        public ListIterator<E> listIterator() {
            return this.delegate.listIterator();
        }

        public ListIterator<E> listIterator(int index) {
            return this.delegate.listIterator(index);
        }

        public E remove(int index) {
            return this.delegate.remove(index);
        }

        public E set(int index, E element) {
            this.delegate.add(index, element);
            return element;
        }

        public List<E> subList(int fromIndex, int toIndex) {
            return this.delegate.subList(fromIndex, toIndex);
        }

        public boolean add(E e) {
            if(e == null) {
                throw new RuntimeException("The nulling object must not be inserted!");
            } else {
                return this.delegate.contains(e)?false:this.delegate.add(e);
            }
        }

        public boolean addAll(Collection<? extends E> c) {
            if(c == null) {
                throw new RuntimeException("The nulling objects must not be inserted!");
            } else if(c.isEmpty()) {
                return false;
            } else {
                boolean returnValue = false;

                Object element;
                for(Iterator iterator = c.iterator(); iterator.hasNext(); returnValue = this.add((E)element)) {
                    element = iterator.next();
                }

                return returnValue;
            }
        }

        public void clear() {
            this.delegate.clear();
        }

        public boolean contains(Object o) {
            return this.delegate.contains(o);
        }

        public boolean containsAll(Collection<?> c) {
            return this.delegate.containsAll(c);
        }

        public boolean isEmpty() {
            return this.size() == 0;
        }

        public boolean remove(Object o) {
            return this.delegate.remove(o);
        }

        public boolean removeAll(Collection<?> c) {
            return this.delegate.removeAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            return this.delegate.retainAll(c);
        }

        public Object[] toArray() {
            return this.delegate.toArray();
        }

        public <T> T[] toArray(T[] a) {
            return this.delegate.toArray(a);
        }

        public String toString() {
            return this.delegate.toString();
        }
    }
}

