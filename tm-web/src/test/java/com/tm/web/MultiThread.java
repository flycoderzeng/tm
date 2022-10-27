package com.tm.web;

import org.apache.commons.lang.math.RandomUtils;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MultiThread {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedBlockingQueue<Integer>();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                    System.out.println("产生一个任务");
                    queue.add(RandomUtils.nextInt());
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Integer task = queue.peek();
                    if(task == null) {
                        continue;
                    }
                    queue.poll();
                    System.out.println("消费一个任务");
                    System.out.println("执行任务" + task);
                }
            }
        });
        t1.start();
        t2.start();
        //Thread.currentThread().setDaemon(true);
    }
}
