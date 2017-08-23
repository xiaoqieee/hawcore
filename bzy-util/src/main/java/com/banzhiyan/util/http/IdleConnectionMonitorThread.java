package com.banzhiyan.util.http;

import org.apache.http.conn.HttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by xn025665 on 2017/8/23.
 */

class IdleConnectionMonitorThread extends Thread {
    private final HttpClientConnectionManager connMgr;
    private final int idletime;
    private volatile boolean shutdown;

    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr, int idletime) {
        this.connMgr = connMgr;
        this.idletime = idletime;
        this.setDaemon(true);
    }

    public void run() {
        while(!this.shutdown) {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException var2) {
                ;
            }

            this.connMgr.closeExpiredConnections();
            this.connMgr.closeIdleConnections((long)this.idletime, TimeUnit.SECONDS);
        }

    }

    public void shutdown() {
        this.shutdown = true;
    }
}

