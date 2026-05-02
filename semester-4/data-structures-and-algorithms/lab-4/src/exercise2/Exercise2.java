package exercise2;

interface Stack<E> {
    int size();

    boolean isEmpty();

    void push(E e);

    E top();

    E pop();
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

    public void addFirst(E e) {
        head = new Node<>(e, head);
        if (tail == null) {
            tail = head;
        }
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

class LinkedStack<E> implements Stack<E> {
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
    public void push(E e) {
        list.addFirst(e);
    }

    @Override
    public E top() {
        return list.first();
    }

    @Override
    public E pop() {
        return list.removeFirst();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}

public class Exercise2 {
    public static <E> void transfer(Stack<E> source, Stack<E> target) {
        while (!source.isEmpty()) {
            target.push(source.pop());
        }
    }

    public static void runDemo() {
        System.out.println("Exercise 2:");

        LinkedStack<Integer> source = new LinkedStack<>();
        source.push(10);
        source.push(20);
        source.push(30);

        LinkedStack<Integer> target = new LinkedStack<>();
        target.push(5);

        System.out.println("Source before transfer: " + source);
        System.out.println("Target before transfer: " + target);

        transfer(source, target);

        System.out.println("Source after transfer: " + source);
        System.out.println("Target after transfer: " + target);
    }

    public static void main(String[] args) {
        runDemo();
    }
}
