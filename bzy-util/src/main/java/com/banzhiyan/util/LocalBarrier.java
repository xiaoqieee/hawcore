package com.banzhiyan.util;

import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by xn025665 on 2017/8/24.
 */

public final class LocalBarrier {
    private static final ConcurrentMap<String, TimedCountDownLatch> barriers = new ConcurrentHashMap();
    private final String id;

    public LocalBarrier(String id) {
        Assert.hasText(id);
        this.id = id;
    }

    public boolean setBarrier() {
        LocalBarrier.TimedCountDownLatch previous = (LocalBarrier.TimedCountDownLatch)barriers.putIfAbsent(this.id, new LocalBarrier.TimedCountDownLatch());
        return previous == null;
    }

    public synchronized boolean removeBarrier() {
        LocalBarrier.TimedCountDownLatch timedCountDownLatch = (LocalBarrier.TimedCountDownLatch)barriers.get(this.id);
        if(timedCountDownLatch == null) {
            return false;
        } else {
            CountDownLatch countDownLatch = timedCountDownLatch.getCountDownLatch();
            countDownLatch.countDown();
            barriers.remove(this.id);
            return true;
        }
    }

    public boolean waitOnBarrier(long maxWait, TimeUnit unit) throws InterruptedException {
        LocalBarrier.TimedCountDownLatch timedCountDownLatch = (LocalBarrier.TimedCountDownLatch)barriers.get(this.id);
        if(timedCountDownLatch == null) {
            return false;
        } else {
            CountDownLatch countDownLatch = timedCountDownLatch.getCountDownLatch();
            return countDownLatch.await(maxWait, unit);
        }
    }

    public synchronized void clearExpiredBarriers(long timeout) {
        LocalBarrier.TimedCountDownLatch timedCountDownLatch = null;
        Iterator i$ = barriers.keySet().iterator();

        while(i$.hasNext()) {
            String key = (String)i$.next();
            timedCountDownLatch = (LocalBarrier.TimedCountDownLatch)barriers.get(key);
            if(System.currentTimeMillis() - timedCountDownLatch.getCreateTime() > timeout) {
                barriers.remove(key);
            }
        }

    }

    private class TimedCountDownLatch {
        private final long createTime = System.currentTimeMillis();
        private final CountDownLatch countDownLatch = new CountDownLatch(1);

        public TimedCountDownLatch() {
        }

        public long getCreateTime() {
            return this.createTime;
        }

        public CountDownLatch getCountDownLatch() {
            return this.countDownLatch;
        }
    }
}

