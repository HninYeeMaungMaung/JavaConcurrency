package com.concurrency.Lock;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class LockWithTimeoutAndPriority {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        PriorityBlockingQueue<PriorityTask> queue = new PriorityBlockingQueue<>();

        queue.add(new PriorityTask("Task-A", 1, lock));
        queue.add(new PriorityTask("Task-B", 3, lock));
        queue.add(new PriorityTask("Task-C", 2, lock));

        ExecutorService executor = Executors.newFixedThreadPool(3);

        while (!queue.isEmpty()) {
            executor.submit(queue.poll());
        }

        executor.shutdown();
    }
}

