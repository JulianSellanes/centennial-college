package exercise3;

interface Queue<E> {
    int size();

    boolean isEmpty();

    void enqueue(E e);

    E first();

    E dequeue();
}

class SinglyLinkedList<E> {
    private static class Node<E> {
        private final E element;
        private Node<E> next;

        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E first() {
        return isEmpty() ? null : head.element;
    }

    public void addLast(E e) {
        Node<E> newest = new Node<>(e, null);
        if (isEmpty()) {
            head = newest;
        } else {
            tail.next = newest;
        }
        tail = newest;
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        E answer = head.element;
        head = head.next;
        size--;
        if (size == 0) {
            tail = null;
        }
        return answer;
    }

    public void appendAll(SinglyLinkedList<E> other) {
        if (this == other) {
            throw new IllegalArgumentException("A list cannot be appended to itself.");
        }
        if (other.isEmpty()) {
            return;
        }
        if (isEmpty()) {
            head = other.head;
        } else {
            tail.next = other.head;
        }
        tail = other.tail;
        size += other.size;

        other.head = null;
        other.tail = null;
        other.size = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("(");
        Node<E> walk = head;
        while (walk != null) {
            builder.append(walk.element);
            walk = walk.next;
            if (walk != null) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }
}

class LinkedQueue<E> implements Queue<E> {
    private final SinglyLinkedList<E> list = new SinglyLinkedList<>();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void enqueue(E e) {
        list.addLast(e);
    }

    @Override
    public E first() {
        return list.first();
    }

    @Override
    public E dequeue() {
        return list.removeFirst();
    }

    public void concatenate(LinkedQueue<E> q2) {
        if (this == q2) {
            throw new IllegalArgumentException("A queue cannot be concatenated with itself.");
        }
        list.appendAll(q2.list);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}

public class Exercise3 {
    public static void runDemo() {
        System.out.println("Exercise 3:");

        LinkedQueue<String> q1 = new LinkedQueue<>();
        q1.enqueue("A");
        q1.enqueue("B");
        q1.enqueue("C");

        LinkedQueue<String> q2 = new LinkedQueue<>();
        q2.enqueue("D");
        q2.enqueue("E");

        System.out.println("Queue 1 before concatenate: " + q1);
        System.out.println("Queue 2 before concatenate: " + q2);

        q1.concatenate(q2);

        System.out.println("Queue 1 after concatenate: " + q1);
        System.out.println("Queue 2 after concatenate: " + q2);
    }

    public static void main(String[] args) {
        runDemo();
    }
}
