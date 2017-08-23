package com.banzhiyan.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class AtomicPositiveInteger extends Number {
    private static final long serialVersionUID = -3038533876489105940L;
    private final AtomicInteger i;

    public AtomicPositiveInteger() {
        this.i = new AtomicInteger();
    }

    public AtomicPositiveInteger(int initialValue) {
        this.i = new AtomicInteger(initialValue);
    }

    public final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.i.get();
            next = current >= 2147483647?0:current + 1;
        } while(!this.i.compareAndSet(current, next));

        return current;
    }

    public final int getAndDecrement() {
        int current;
        int next;
        do {
            current = this.i.get();
            next = current <= 0?2147483647:current - 1;
        } while(!this.i.compareAndSet(current, next));

        return current;
    }

    public final int incrementAndGet() {
        int current;
        int next;
        do {
            current = this.i.get();
            next = current >= 2147483647?0:current + 1;
        } while(!this.i.compareAndSet(current, next));

        return next;
    }

    public final int decrementAndGet() {
        int current;
        int next;
        do {
            current = this.i.get();
            next = current <= 0?2147483647:current - 1;
        } while(!this.i.compareAndSet(current, next));

        return next;
    }

    public final int get() {
        return this.i.get();
    }

    public final void set(int newValue) {
        if(newValue < 0) {
            throw new IllegalArgumentException("new value " + newValue + " < 0");
        } else {
            this.i.set(newValue);
        }
    }

    public final int getAndSet(int newValue) {
        if(newValue < 0) {
            throw new IllegalArgumentException("new value " + newValue + " < 0");
        } else {
            return this.i.getAndSet(newValue);
        }
    }

    public final int getAndAdd(int delta) {
        if(delta < 0) {
            throw new IllegalArgumentException("delta " + delta + " < 0");
        } else {
            int current;
            int next;
            do {
                current = this.i.get();
                next = current >= 2147483647 - delta + 1?delta - 1:current + delta;
            } while(!this.i.compareAndSet(current, next));

            return current;
        }
    }

    public final int addAndGet(int delta) {
        if(delta < 0) {
            throw new IllegalArgumentException("delta " + delta + " < 0");
        } else {
            int current;
            int next;
            do {
                current = this.i.get();
                next = current >= 2147483647 - delta + 1?delta - 1:current + delta;
            } while(!this.i.compareAndSet(current, next));

            return next;
        }
    }

    public final boolean compareAndSet(int expect, int update) {
        if(update < 0) {
            throw new IllegalArgumentException("update value " + update + " < 0");
        } else {
            return this.i.compareAndSet(expect, update);
        }
    }

    public final boolean weakCompareAndSet(int expect, int update) {
        if(update < 0) {
            throw new IllegalArgumentException("update value " + update + " < 0");
        } else {
            return this.i.weakCompareAndSet(expect, update);
        }
    }

    public byte byteValue() {
        return this.i.byteValue();
    }

    public short shortValue() {
        return this.i.shortValue();
    }

    public int intValue() {
        return this.i.intValue();
    }

    public long longValue() {
        return this.i.longValue();
    }

    public float floatValue() {
        return this.i.floatValue();
    }

    public double doubleValue() {
        return this.i.doubleValue();
    }

    public String toString() {
        return this.i.toString();
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.i == null?0:this.i.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null) {
            return false;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            AtomicPositiveInteger other = (AtomicPositiveInteger)obj;
            if(this.i == null) {
                if(other.i != null) {
                    return false;
                }
            } else if(!this.i.equals(other.i)) {
                return false;
            }

            return true;
        }
    }
}

