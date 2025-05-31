package com.concurrency.RateLimiter;

public class RateLimiterTest {
    public static void main(String[] args) {

        try {
            TockenBucketLimiter tockenBucketLimiter = new TockenBucketLimiter(5, 1);

            Runnable userTask = () -> {
                String user = "user1";
                for (int i = 0; i < 10; i++) {
                    boolean allowed = tockenBucketLimiter.allowRequest(user);
                    System.out.println(Thread.currentThread().getName() + " → Request " + (i+1) + ": " + (allowed ? "✅ Allowed" : "❌ Denied"));
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException ignored) {}
                }
            };

            Thread t1 = new Thread(userTask);
            Thread t2 = new Thread(userTask);
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
