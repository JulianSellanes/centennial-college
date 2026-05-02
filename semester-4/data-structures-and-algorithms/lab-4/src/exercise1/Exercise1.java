package exercise1;

import java.util.Iterator;
import java.util.NoSuchElementException;

interface Position<E> {
    E getElement();
}

interface PositionalList<E> extends Iterable<E> {
    int size();

    boolean isEmpty();

    Position<E> first();

    Position<E> last();

    Position<E> before(Position<E> p);

    Position<E> after(Position<E> p);

    Position<E> addFirst(E e);

    Position<E> addLast(E e);

    Position<E> addBefore(Position<E> p, E e);

    Position<E> addAfter(Position<E> p, E e);

    E set(Position<E> p, E e);

    E remove(Position<E> p);

    Iterable<Position<E>> positions();

    default int indexOf(Position<E> p) {
        int index = 0;
        for (Position<E> walk = first(); walk != null; walk = after(walk)) {
            if (walk == p) {
                return index;
            }
            index++;
        }
        throw new IllegalArgumentException("Position does not belong to this list.");
    }
}

class LinkedPositionalList<E> implements PositionalList<E> {
    private static class Node<E> implements Position<E> {
        private E element;
        private Node<E> prev;
        private Node<E> next;

        Node(E element, Node<E> prev, Node<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public E getElement() {
            if (next == null) {
                throw new IllegalStateException("Position is no longer valid.");
            }
            return element;
        }

        public Node<E> getPrev() {
            return prev;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public void setPrev(Node<E> prev) {
            this.prev = prev;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }
    }

    private final Node<E> header;
    private final Node<E> trailer;
    private int size = 0;

    LinkedPositionalList() {
        header = new Node<>(null, null, null);
        trailer = new Node<>(null, header, null);
        header.setNext(trailer);
    }

    private Node<E> validate(Position<E> p) {
        if (!(p instanceof Node)) {
            throw new IllegalArgumentException("Invalid position.");
        }
        Node<E> node = (Node<E>) p;
        if (node.getNext() == null) {
            throw new IllegalArgumentException("Position is no longer in the list.");
        }
        return node;
    }

    private Position<E> position(Node<E> node) {
        if (node == header || node == trailer) {
            return null;
        }
        return node;
    }

    private Position<E> addBetween(E e, Node<E> pred, Node<E> succ) {
        Node<E> newest = new Node<>(e, pred, succ);
        pred.setNext(newest);
        succ.setPrev(newest);
        size++;
        return newest;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Position<E> first() {
        return position(header.getNext());
    }

    @Override
    public Position<E> last() {
        return position(trailer.getPrev());
    }

    @Override
    public Position<E> before(Position<E> p) {
        return position(validate(p).getPrev());
    }

    @Override
    public Position<E> after(Position<E> p) {
        return position(validate(p).getNext());
    }

    @Override
    public Position<E> addFirst(E e) {
        return addBetween(e, header, header.getNext());
    }

    @Override
    public Position<E> addLast(E e) {
        return addBetween(e, trailer.getPrev(), trailer);
    }

    @Override
    public Position<E> addBefore(Position<E> p, E e) {
        Node<E> node = validate(p);
        return addBetween(e, node.getPrev(), node);
    }

    @Override
    public Position<E> addAfter(Position<E> p, E e) {
        Node<E> node = validate(p);
        return addBetween(e, node, node.getNext());
    }

    @Override
    public E set(Position<E> p, E e) {
        Node<E> node = validate(p);
        E old = node.getElement();
        node.setElement(e);
        return old;
    }

    @Override
    public E remove(Position<E> p) {
        Node<E> node = validate(p);
        Node<E> predecessor = node.getPrev();
        Node<E> successor = node.getNext();
        predecessor.setNext(successor);
        successor.setPrev(predecessor);
        size--;
        E answer = node.getElement();
        node.setElement(null);
        node.setPrev(null);
        node.setNext(null);
        return answer;
    }

    private class PositionIterator implements Iterator<Position<E>> {
        private Position<E> cursor = first();
        private Position<E> recent = null;

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public Position<E> next() {
            if (cursor == null) {
                throw new NoSuchElementException("No more positions.");
            }
            recent = cursor;
            cursor = after(cursor);
            return recent;
        }

        @Override
        public void remove() {
            if (recent == null) {
                throw new IllegalStateException("Nothing to remove.");
            }
            LinkedPositionalList.this.remove(recent);
            recent = null;
        }
    }

    private class PositionIterable implements Iterable<Position<E>> {
        @Override
        public Iterator<Position<E>> iterator() {
            return new PositionIterator();
        }
    }

    @Override
    public Iterable<Position<E>> positions() {
        return new PositionIterable();
    }

    private class ElementIterator implements Iterator<E> {
        private final Iterator<Position<E>> positionIterator = positions().iterator();

        @Override
        public boolean hasNext() {
            return positionIterator.hasNext();
        }

        @Override
        public E next() {
            return positionIterator.next().getElement();
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ElementIterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("(");
        Node<E> walk = header.getNext();
        while (walk != trailer) {
            builder.append(walk.getElement());
            walk = walk.getNext();
            if (walk != trailer) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }
}

public class Exercise1 {
    public static void runDemo() {
        System.out.println("Exercise 1:");

        LinkedPositionalList<String> list = new LinkedPositionalList<>();
        Position<String> alice = list.addLast("Alice");
        Position<String> carol = list.addLast("Carol");
        Position<String> dave = list.addLast("Dave");
        Position<String> bob = list.addAfter(alice, "Bob");

        System.out.println("List: " + list);
        System.out.println("Index of Alice: " + list.indexOf(alice));
        System.out.println("Index of Bob: " + list.indexOf(bob));
        System.out.println("Index of Carol: " + list.indexOf(carol));
        System.out.println("Index of Dave: " + list.indexOf(dave));

        list.remove(bob);
        System.out.println("After removing Bob: " + list);
        System.out.println("Current index of Dave: " + list.indexOf(dave));
    }

    public static void main(String[] args) {
        runDemo();
    }
}
