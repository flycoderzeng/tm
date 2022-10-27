package com.tm.web;

import java.util.concurrent.ConcurrentLinkedDeque;

public class QueueTest {
    public static void main(String[] args) {
        ConcurrentLinkedDeque<Integer> integerConcurrentLinkedDeque = new ConcurrentLinkedDeque<>();
        integerConcurrentLinkedDeque.add(1);
        Integer poll = integerConcurrentLinkedDeque.poll();
        System.out.println(poll);
    }
}
