package com.concurrency.Lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OddEvenPrinterWithLock {
    
    private final int MAX = 10;
    private int number = 1;
    private final Lock lock = new ReentrantLock();
    private final Condition oddTurn = lock.newCondition();
    private final Condition evenTurn = lock.newCondition();

    public static void main(String[] args) {
        OddEvenPrinterWithLock printer = new OddEvenPrinterWithLock();

        Thread oddThread = new Thread(printer::printOdd,"OddThread");
        Thread evenThread = new Thread(printer::printEven, "EvenThread");

        oddThread.start();;
        evenThread.start();
    }

    private void printOdd(){
        lock.lock();
        try{
            while (number <= MAX) {
                if (number % 2 == 0) {
                    oddTurn.await(); // wait until it's odd's turn
                } else {
                    System.out.println(Thread.currentThread().getName() + " → " + number);
                    number++;
                    evenTurn.signal(); // notify even thread
                }
            }
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        } finally{
            lock.unlock();
        }
    }

    private void printEven(){
        lock.lock();
        try{
            while (number <= MAX) {
                if (number % 2 != 0) {
                    evenTurn.await(); // wait until it's even's turn
                } else {
                    System.out.println(Thread.currentThread().getName() + " → " + number);
                    number++;
                    oddTurn.signal(); // notify odd thread
                }
            }
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        } finally{
            lock.unlock();
        }
    }

}
