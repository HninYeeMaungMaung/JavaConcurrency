package com.concurrency.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TockenBucketLimiter implements RateLimiter{

    private final int capacity;
    private final int refillRate;
    private final ConcurrentHashMap<String, AtomicInteger> userBuckets = new ConcurrentHashMap<>();
    
    public TockenBucketLimiter(int capacity, int refillRate){
        this.capacity = capacity;
        this.refillRate = refillRate;

        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleAtFixedRate(this::refillAll, 0, 10, TimeUnit.SECONDS);
    }

    private void refillAll(){
        for(Map.Entry<String, AtomicInteger> bucket: userBuckets.entrySet()){
            AtomicInteger tokens = bucket.getValue();
            int current = tokens.get();
            int newTokens = Math.min(capacity, current + refillRate);
            tokens.set(newTokens);
        }

    }

    @Override
    public boolean allowRequest(String userID) {

        userBuckets.putIfAbsent(userID, new AtomicInteger(capacity));
        AtomicInteger tokens = userBuckets.get(userID);

        while (true) {
            int current = tokens.get();
            if (current == 0) return false;
            if (tokens.compareAndSet(current, current - 1)) return true;
        }
    }
    
}
