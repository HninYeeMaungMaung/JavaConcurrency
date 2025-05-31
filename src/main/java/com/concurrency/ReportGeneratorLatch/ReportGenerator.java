package com.concurrency.ReportGeneratorLatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReportGenerator{

    public void generateReport(int reportCount){

        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService executor = Executors.newFixedThreadPool(reportCount);

        List<Future<String>> futures = new ArrayList<>();

        for (int i = 1; i <= reportCount; i++) {
            int id = i;
            Callable<String> task = () -> {
                System.out.println("[Report-" + id + "] started.");
                Thread.sleep(500 + id * 150); // Simulate time taken
                System.out.println("[Report-" + id + "] finished.");
                latch.countDown();
                return "Report-" + id + " done.";
            };
            futures.add(executor.submit(task));
        }

        try {
            latch.await();
            System.out.println("✅ All reports completed.");
            futures.stream().forEach(report -> {
                try {
                    System.out.println("Summary: " + report.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error occurred: " + e.getMessage());
        } finally {
            executor.shutdown();
        }


        
        
        
    }


    public static void main(String[] args) {
        new ReportGenerator().generateReport(5);
    }
   
    
}
