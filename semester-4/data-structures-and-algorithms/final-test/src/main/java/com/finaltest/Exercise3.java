package com.finaltest;

import java.util.Comparator;
import java.util.Locale;

// Julian Sellanes (301494667)

public class Exercise3 {
    public interface Queue<E> {
        int size();
        boolean isEmpty();
        void enqueue(E e);
        E first();
        E dequeue();
    }

    public static class LinkedQueue<E> implements Queue<E> {

        private static class Node<E> {
            E element;
            Node<E> next;
            Node(E e, Node<E> n) { element = e; next = n; }
        }

        private Node<E> head = null;
        private Node<E> tail = null;
        private int size = 0;

        public int size()        { return size; }
        public boolean isEmpty() { return size == 0; }

        public void enqueue(E e) {
            Node<E> node = new Node<>(e, null);
            if (isEmpty()) {
                head = node;
            } else {
                tail.next = node;
            }
            tail = node;
            size++;
        }

        public E first() {
            return isEmpty() ? null : head.element;
        }

        public E dequeue() {
            if (isEmpty()) return null;
            E answer = head.element;
            head = head.next;
            size--;
            if (isEmpty()) tail = null;
            return answer;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            Node<E> walk = head;
            while (walk != null) {
                sb.append(walk.element);
                walk = walk.next;
                if (walk != null) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static class Account {
        private int accountNumber;
        private String customerName;
        private double accountBalance;

        public Account(int accountNumber, String customerName, double accountBalance) {
            this.accountNumber = accountNumber;
            this.customerName = customerName;
            this.accountBalance = accountBalance;
        }

        public int getAccountNumber()       { return accountNumber; }
        public String getCustomerName()     { return customerName; }
        public double getAccountBalance()   { return accountBalance; }

        @Override
        public String toString() {
            return String.format(Locale.US, "Account{#%d, %s, $%.2f}",
                                 accountNumber, customerName, accountBalance);
        }
    }

    public static <K> void quickSort(Queue<K> S, Comparator<K> comp) {
        int n = S.size();
        if (n < 2) return;

        K pivot = S.first();
        Queue<K> L = new LinkedQueue<>();
        Queue<K> E = new LinkedQueue<>();
        Queue<K> G = new LinkedQueue<>();

        while (!S.isEmpty()) {
            K element = S.dequeue();
            int c = comp.compare(element, pivot);
            if (c < 0)       L.enqueue(element);
            else if (c == 0) E.enqueue(element);
            else             G.enqueue(element);
        }

        quickSort(L, comp);
        quickSort(G, comp);

        while (!L.isEmpty()) S.enqueue(L.dequeue());
        while (!E.isEmpty()) S.enqueue(E.dequeue());
        while (!G.isEmpty()) S.enqueue(G.dequeue());
    }

    public static void main(String[] args) {
        System.out.println("Exercise 3:");

        Queue<Account> accounts = new LinkedQueue<>();
        accounts.enqueue(new Account(1001, "Alice",   1500.75));
        accounts.enqueue(new Account(1002, "Bob",      250.00));
        accounts.enqueue(new Account(1003, "Charlie", 9875.40));
        accounts.enqueue(new Account(1004, "Diana",    500.10));
        accounts.enqueue(new Account(1005, "Evan",    3000.00));
        accounts.enqueue(new Account(1006, "Fiona",     75.25));
        accounts.enqueue(new Account(1007, "George",  3000.00));
        accounts.enqueue(new Account(1008, "Hanna",   1200.00));

        // Comparator orders Account objects by their balance in increasing order.
        Comparator<Account> byBalance = new Comparator<Account>() {
            @Override
            public int compare(Account a, Account b) {
                return Double.compare(a.getAccountBalance(), b.getAccountBalance());
            }
        };

        System.out.println("Before sorting:");
        printQueue(accounts);

        quickSort(accounts, byBalance);

        System.out.println();
        System.out.println("After quickSort by accountBalance (increasing):");
        printQueue(accounts);
    }

    // Prints the contents of a queue one element per line without consuming it.
    private static void printQueue(Queue<Account> q) {
        int n = q.size();
        for (int i = 0; i < n; i++) {
            Account a = q.dequeue();
            System.out.println("  " + a);
            q.enqueue(a);
        }
    }
}
