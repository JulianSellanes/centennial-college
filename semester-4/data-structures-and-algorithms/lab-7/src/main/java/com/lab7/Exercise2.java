package com.lab7;

import java.util.ArrayDeque;
import java.util.Comparator;

// Julian Sellanes (301494667)

public class Exercise2 {
    private static class SimpleQueue<E> {
        private final ArrayDeque<E> data = new ArrayDeque<>();

        public void enqueue(E value) {
            data.addLast(value);
        }

        public E dequeue() {
            return data.removeFirst();
        }

        public E first() {
            return data.element();
        }

        public boolean isEmpty() {
            return data.isEmpty();
        }

        public int size() {
            return data.size();
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private static <T> void merge(SimpleQueue<T> first, SimpleQueue<T> second, SimpleQueue<T> result, Comparator<T> comparator) {
        while (!first.isEmpty() && !second.isEmpty()) {
            if (comparator.compare(first.first(), second.first()) <= 0) {
                result.enqueue(first.dequeue());
            } else {
                result.enqueue(second.dequeue());
            }
        }

        while (!first.isEmpty()) {
            result.enqueue(first.dequeue());
        }

        while (!second.isEmpty()) {
            result.enqueue(second.dequeue());
        }
    }

    private static <T> SimpleQueue<T> bottomUpMergeSort(SimpleQueue<T> values, Comparator<T> comparator) {
        SimpleQueue<SimpleQueue<T>> queueOfQueues = new SimpleQueue<>();

        while (!values.isEmpty()) {
            SimpleQueue<T> singleItemQueue = new SimpleQueue<>();
            singleItemQueue.enqueue(values.dequeue());
            queueOfQueues.enqueue(singleItemQueue);
        }

        if (queueOfQueues.isEmpty()) {
            return new SimpleQueue<>();
        }

        while (queueOfQueues.size() > 1) {
            SimpleQueue<T> first = queueOfQueues.dequeue();
            SimpleQueue<T> second = queueOfQueues.isEmpty()
                ? new SimpleQueue<>()
                : queueOfQueues.dequeue();

            SimpleQueue<T> merged = new SimpleQueue<>();
            merge(first, second, merged, comparator);
            queueOfQueues.enqueue(merged);
        }

        return queueOfQueues.dequeue();
    }

    private static <T> SimpleQueue<T> buildQueue(T[] items) {
        SimpleQueue<T> queue = new SimpleQueue<>();
        for (T item : items) {
            queue.enqueue(item);
        }
        return queue;
    }

    public static void main(String[] args) {
        SimpleQueue<Integer> numbers = buildQueue(new Integer[]{85, 24, 63, 45, 17, 31, 96, 50});

        System.out.println("Exercise 2:\n");
        System.out.println("Original queue: " + numbers);

        SimpleQueue<Integer> sortedNumbers = bottomUpMergeSort(numbers, Comparator.naturalOrder());
        System.out.println("Sorted queue:   " + sortedNumbers);

        SimpleQueue<String> words = buildQueue(new String[]{"pear", "apple", "orange", "banana"});
        System.out.println("Original words: " + words);

        SimpleQueue<String> sortedWords = bottomUpMergeSort(words, Comparator.naturalOrder());
        System.out.println("Sorted words:   " + sortedWords);
    }
}
