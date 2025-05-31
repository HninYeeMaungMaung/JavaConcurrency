package com.concurrency.ProducerConsumerQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumerBlockingQueue {
    public static void main(String[] args) {
        
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();

        //Producer
        Callable<String> producer =() -> {
            try{
            blockingQueue.put("Player1");
            blockingQueue.put("Player2");
            blockingQueue.put("Player3");
            blockingQueue.put("Player4");
            blockingQueue.put("Player5");
            blockingQueue.put("Player6");
            blockingQueue.put("END");
            } catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
            return "Producer Finished!";
        };

        //Consumer1
        Callable<String> consumer1 =() -> {
            try{
                int counter = 3;
                while(counter > 0){
                    System.out.println("Consumer 1 : " + blockingQueue.take());
                    counter--;
                }
            } catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
            return "Consumer1 Finished!";
        };


        //Consumer2
        Callable<String> consumer2 = () -> {
            try{
                int counter = 3;
                while(counter > 0){
                    System.out.println("Consumer 2 : " + blockingQueue.take());
                    counter--;
                }
            } catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
            return "Consumer2 Finished!";
        };

        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<?> producerFuture = executor.submit(producer);
        Future<?> consumer1Future = executor.submit(consumer1);
        Future<?> consumer2Future = executor.submit(consumer2);

        try {
            System.out.println("ProducerFuture: " + producerFuture.get());
            System.out.println("Consumer1Future: " + consumer1Future.get());
            System.out.println("Consumer2Future: " + consumer2Future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();

    }
}