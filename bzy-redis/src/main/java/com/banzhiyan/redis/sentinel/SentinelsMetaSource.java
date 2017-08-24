package com.banzhiyan.redis.sentinel;

import org.springframework.beans.factory.FactoryBean;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class SentinelsMetaSource implements FactoryBean<Set<String>> {
    private final String address;

    public SentinelsMetaSource(String address) {
        if(address == null) {
            throw new NullPointerException("Null sentinels");
        } else if(address.trim().isEmpty()) {
            throw new IllegalArgumentException("No address in sentinels: '" + address + "'");
        } else {
            this.address = address.trim();
        }
    }

    public Set<String> getObject() throws Exception {
        return Collections.unmodifiableSet(this.getAddresses(this.address));
    }

    private Set<String> getAddresses(String s) {
        Set<String> addrs = new HashSet();
        String[] arr$ = s.split("(?:\\s|,)+");
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String hostStuff = arr$[i$];
            if(!"".equals(hostStuff)) {
                int finalColon = hostStuff.lastIndexOf(58);
                if(finalColon < 1) {
                    throw new IllegalArgumentException("Invalid server '" + hostStuff + "' in list:  " + s);
                }

                addrs.add(hostStuff);
            }
        }

        return addrs;
    }

    public Class<?> getObjectType() {
        return Set.class;
    }

    public boolean isSingleton() {
        return true;
    }
}

