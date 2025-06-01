package com.concurrency.Lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityTask implements Comparable<PriorityTask>, Runnable {
    private final String name;
    private final int priority;
    private final ReentrantLock lock;

    public PriorityTask(String name, int priority, ReentrantLock lock) {
        this.name = name;
        this.priority = priority;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " trying to acquire lock...");
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    System.out.println(name + " acquired lock (priority " + priority + ")");
                    Thread.sleep(800); // simulate work
                } finally {
                    lock.unlock();
                    System.out.println(name + " released lock");
                }
            } else {
                System.out.println(name + " failed to acquire lock (priority " + priority + ")");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public int compareTo(PriorityTask other) {
        // Higher priority first
        return Integer.compare(other.priority, this.priority);
    }
}
