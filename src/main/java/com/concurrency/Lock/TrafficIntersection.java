package com.concurrency.Lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficIntersection {

    private static final Lock intersectionLock = new ReentrantLock();

    static class Car implements Runnable {
        private final String direction;

        public Car(String direction) {
            this.direction = direction;
        }

        @Override
        public void run() {
            System.out.println(direction + " car is waiting to enter the intersection...");
            intersectionLock.lock(); // acquire access
            try {
                System.out.println(direction + " car has entered the intersection.");
                Thread.sleep(1000); // simulate crossing time
                System.out.println(direction + " car has left the intersection.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                intersectionLock.unlock(); // release access
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        String[] directions = {"North", "South", "East", "West"};

        for (int i = 0; i < 10; i++) {
            String dir = directions[i % 4];
            executor.execute(new Car(dir));

            try {
                Thread.sleep(300); // cars arriving at intervals
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        executor.shutdown();
    }
}
