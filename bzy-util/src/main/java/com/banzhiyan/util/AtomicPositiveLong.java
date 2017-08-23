package com.banzhiyan.util;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class AtomicPositiveLong extends Number implements Serializable {
    private static final long serialVersionUID = -8416688392313180434L;
    private final AtomicLong l;

    public AtomicPositiveLong() {
        this.l = new AtomicLong();
    }

    public AtomicPositiveLong(long initialValue) {
        this.l = new AtomicLong(initialValue);
    }

    public final long getAndIncrement() {
        long current;
        long next;
        do {
            current = this.l.get();
            next = current >= 9223372036854775807L?0L:current + 1L;
        } while(!this.l.compareAndSet(current, next));

        return current;
    }

    public final long getAndDecrement() {
        long current;
        long next;
        do {
            current = this.l.get();
            next = current <= 0L?9223372036854775807L:current - 1L;
        } while(!this.l.compareAndSet(current, next));

        return current;
    }

    public final long incrementAndGet() {
        long current;
        long next;
        do {
            current = this.l.get();
            next = current >= 9223372036854775807L?0L:current + 1L;
        } while(!this.l.compareAndSet(current, next));

        return next;
    }

    public final long decrementAndGet() {
        long current;
        long next;
        do {
            current = this.l.get();
            next = current <= 0L?9223372036854775807L:current - 1L;
        } while(!this.l.compareAndSet(current, next));

        return next;
    }

    public final long get() {
        return this.l.get();
    }

    public final void set(long newValue) {
        if(newValue < 0L) {
            throw new IllegalArgumentException("new value " + newValue + " < 0");
        } else {
            this.l.set(newValue);
        }
    }

    public final long getAndSet(long newValue) {
        if(newValue < 0L) {
            throw new IllegalArgumentException("new value " + newValue + " < 0");
        } else {
            return this.l.getAndSet(newValue);
        }
    }

    public final long getAndAdd(long delta) {
        if(delta < 0L) {
            throw new IllegalArgumentException("delta " + delta + " < 0");
        } else {
            long current;
            long next;
            do {
                current = this.l.get();
                next = current >= 9223372036854775807L - delta + 1L?delta - 1L:current + delta;
            } while(!this.l.compareAndSet(current, next));

            return current;
        }
    }

    public final long addAndGet(long delta) {
        if(delta < 0L) {
            throw new IllegalArgumentException("delta " + delta + " < 0");
        } else {
            long current;
            long next;
            do {
                current = this.l.get();
                next = current >= 9223372036854775807L - delta + 1L?delta - 1L:current + delta;
            } while(!this.l.compareAndSet(current, next));

            return next;
        }
    }

    public final boolean compareAndSet(long expect, long update) {
        if(update < 0L) {
            throw new IllegalArgumentException("update value " + update + " < 0");
        } else {
            return this.l.compareAndSet(expect, update);
        }
    }

    public final boolean weakCompareAndSet(long expect, long update) {
        if(update < 0L) {
            throw new IllegalArgumentException("update value " + update + " < 0");
        } else {
            return this.l.weakCompareAndSet(expect, update);
        }
    }

    public byte byteValue() {
        return this.l.byteValue();
    }

    public short shortValue() {
        return this.l.shortValue();
    }

    public int intValue() {
        return this.l.intValue();
    }

    public long longValue() {
        return this.l.longValue();
    }

    public float floatValue() {
        return this.l.floatValue();
    }

    public double doubleValue() {
        return this.l.doubleValue();
    }

    public String toString() {
        return this.l.toString();
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.l == null?0:this.l.hashCode());
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
            AtomicPositiveLong other = (AtomicPositiveLong)obj;
            if(this.l == null) {
                if(other.l != null) {
                    return false;
                }
            } else if(!this.l.equals(other.l)) {
                return false;
            }

            return true;
        }
    }
}
