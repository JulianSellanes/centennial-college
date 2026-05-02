package exercise1;

import java.util.ArrayList;
import java.util.List;

// Julian Sellanes (301494667)

public class App {
    private static final class LinkedBinaryTree<E> {
        static final class Node<E> {
            private final E element;
            private Node<E> parent;
            private Node<E> left;
            private Node<E> right;

            Node(E element, Node<E> parent) {
                this.element = element;
                this.parent = parent;
            }

            E getElement() {
                return element;
            }
        }

        private Node<E> root;

        Node<E> addRoot(E element) {
            if (root != null) {
                throw new IllegalStateException("Tree already has a root.");
            }
            root = new Node<>(element, null);
            return root;
        }

        Node<E> addLeft(Node<E> parent, E element) {
            validate(parent);
            if (parent.left != null) {
                throw new IllegalArgumentException("Left child already exists.");
            }
            parent.left = new Node<>(element, parent);
            return parent.left;
        }

        Node<E> addRight(Node<E> parent, E element) {
            validate(parent);
            if (parent.right != null) {
                throw new IllegalArgumentException("Right child already exists.");
            }
            parent.right = new Node<>(element, parent);
            return parent.right;
        }

        Node<E> preorderNext(Node<E> position) {
            validate(position);

            if (position.left != null) {
                return position.left;
            }
            if (position.right != null) {
                return position.right;
            }

            Node<E> walk = position;

            while (walk.parent != null) {
                Node<E> parent = walk.parent;
                if (walk == parent.left && parent.right != null) {
                    return parent.right;
                }
                walk = parent;
            }

            return null;
        }

        List<Node<E>> preorderSnapshot() {
            List<Node<E>> snapshot = new ArrayList<>();
            preorderSubtree(root, snapshot);
            return snapshot;
        }

        private void preorderSubtree(Node<E> node, List<Node<E>> snapshot) {
            if (node == null) {
                return;
            }
            snapshot.add(node);
            preorderSubtree(node.left, snapshot);
            preorderSubtree(node.right, snapshot);
        }

        private void validate(Node<E> node) {
            if (node == null) {
                throw new IllegalArgumentException("Position cannot be null.");
            }
        }
    }

    public static void main(String[] args) {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>();

        LinkedBinaryTree.Node<String> a = tree.addRoot("A");
        LinkedBinaryTree.Node<String> b = tree.addLeft(a, "B");
        LinkedBinaryTree.Node<String> c = tree.addRight(a, "C");
        tree.addLeft(b, "D");
        tree.addRight(b, "E");
        tree.addLeft(c, "F");
        tree.addRight(c, "G");

        List<LinkedBinaryTree.Node<String>> preorder = tree.preorderSnapshot();

        System.out.println("Exercise 1: preorderNext(p)");
        System.out.println("Preorder traversal:");
        for (LinkedBinaryTree.Node<String> node : preorder) {
            System.out.print(node.getElement() + " ");
        }
        System.out.println();
        System.out.println();

        System.out.println("Node -> preorderNext(node)");
        for (LinkedBinaryTree.Node<String> node : preorder) {
            LinkedBinaryTree.Node<String> next = tree.preorderNext(node);
            String nextValue = next == null ? "null" : next.getElement();
            System.out.println(node.getElement() + " -> " + nextValue);
        }
    }
}
