package com.finaltest;

// Julian Sellanes (301494667)

public class Exercise1 {
    public interface Position<E> {
        E getElement();
    }

    public static class LinkedBinaryTree<E> {

        protected static class Node<E> implements Position<E> {
            private E element;
            private Node<E> parent;
            private Node<E> left;
            private Node<E> right;

            public Node(E e, Node<E> above, Node<E> leftChild, Node<E> rightChild) {
                element = e;
                parent = above;
                left = leftChild;
                right = rightChild;
            }

            public E getElement()        { return element; }
            public Node<E> getParent()   { return parent; }
            public Node<E> getLeft()     { return left; }
            public Node<E> getRight()    { return right; }

            public void setParent(Node<E> p) { parent = p; }
            public void setLeft(Node<E> n)   { left = n; }
            public void setRight(Node<E> n)  { right = n; }
        }

        protected Node<E> root = null;
        private int size = 0;

        public LinkedBinaryTree() { }

        public int size()            { return size; }
        public boolean isEmpty()     { return size == 0; }
        public Position<E> root()    { return root; }

        public Position<E> parent(Position<E> p) {
            return validate(p).getParent();
        }

        public Position<E> left(Position<E> p) {
            return validate(p).getLeft();
        }

        public Position<E> right(Position<E> p) {
            return validate(p).getRight();
        }

        protected Node<E> validate(Position<E> p) {
            if (!(p instanceof Node))
                throw new IllegalArgumentException("Not valid position type");
            Node<E> node = (Node<E>) p;
            if (node.getParent() == node)
                throw new IllegalArgumentException("p is no longer in the tree");
            return node;
        }

        public Position<E> addRoot(E e) {
            if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
            root = new Node<>(e, null, null, null);
            size = 1;
            return root;
        }

        public Position<E> addLeft(Position<E> p, E e) {
            Node<E> parent = validate(p);
            if (parent.getLeft() != null)
                throw new IllegalArgumentException("p already has a left child");
            Node<E> child = new Node<>(e, parent, null, null);
            parent.setLeft(child);
            size++;
            return child;
        }

        public Position<E> addRight(Position<E> p, E e) {
            Node<E> parent = validate(p);
            if (parent.getRight() != null)
                throw new IllegalArgumentException("p already has a right child");
            Node<E> child = new Node<>(e, parent, null, null);
            parent.setRight(child);
            size++;
            return child;
        }

        public int pathLength() {
            return pathLengthHelper(root, 0);
        }

        private int pathLengthHelper(Node<E> node, int depth) {
            if (node == null) return 0;
            int sum = depth;
            sum += pathLengthHelper(node.getLeft(), depth + 1);
            sum += pathLengthHelper(node.getRight(), depth + 1);
            return sum;
        }
    }

    public static void main(String[] args) {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>();
        Position<String> a = tree.addRoot("A");
        Position<String> b = tree.addLeft(a, "B");
        Position<String> c = tree.addRight(a, "C");
        tree.addLeft(b, "D");
        tree.addRight(b, "E");
        tree.addLeft(c, "F");
        tree.addRight(c, "G");

        System.out.println("Exercise 1:");
        System.out.println("Tree size: " + tree.size());
        System.out.println("Computed path length: " + tree.pathLength());
        System.out.println("Expected path length: 10");

        // Second test: a single-node tree.
        LinkedBinaryTree<String> single = new LinkedBinaryTree<>();
        single.addRoot("only");
        System.out.println();
        System.out.println("Single-node tree path length: " + single.pathLength() + " (expected 0)");

        // Third test: left-skewed tree of 4 nodes -> depths 0,1,2,3 -> sum 6.
        LinkedBinaryTree<Integer> skewed = new LinkedBinaryTree<>();
        Position<Integer> p = skewed.addRoot(1);
        p = skewed.addLeft(p, 2);
        p = skewed.addLeft(p, 3);
        skewed.addLeft(p, 4);
        System.out.println("Left-skewed tree (4 nodes) path length: " + skewed.pathLength() + " (expected 6)");

        // Fourth test: empty tree.
        LinkedBinaryTree<Integer> empty = new LinkedBinaryTree<>();
        System.out.println("Empty tree path length: " + empty.pathLength() + " (expected 0)");
    }
}
