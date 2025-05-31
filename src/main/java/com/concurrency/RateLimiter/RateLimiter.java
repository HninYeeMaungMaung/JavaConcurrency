package com.concurrency.RateLimiter;

public interface RateLimiter {
    
    boolean allowRequest(String userID);

}
