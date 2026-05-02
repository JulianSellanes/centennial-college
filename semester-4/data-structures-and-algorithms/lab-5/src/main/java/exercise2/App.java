package exercise2;

import java.util.ArrayList;
import java.util.List;

// Julian Sellanes (301494667)

public class App {
    private static final class LinkedTree<E> {
        static final class Node<E> {
            private final E element;
            private final Node<E> parent;
            private final List<Node<E>> children = new ArrayList<>();

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

        Node<E> addChild(Node<E> parent, E element) {
            validate(parent);
            Node<E> child = new Node<>(element, parent);
            parent.children.add(child);
            return child;
        }

        void printElementsWithSubtreeHeights() {
            if (root == null) {
                System.out.println("Tree is empty.");
                return;
            }

            int treeHeight = postorderHeight(root);
            System.out.println();
            System.out.println("Height of the whole tree: " + treeHeight);
        }

        private int postorderHeight(Node<E> node) {
            int maxChildHeight = -1;

            for (Node<E> child : node.children) {
                maxChildHeight = Math.max(maxChildHeight, postorderHeight(child));
            }

            int height = maxChildHeight + 1;
            System.out.println(node.getElement() + " -> " + height);
            return height;
        }

        @SuppressWarnings("unused")
        Node<E> parent(Node<E> node) {
            return node.parent;
        }

        private void validate(Node<E> node) {
            if (node == null) {
                throw new IllegalArgumentException("Position cannot be null.");
            }
        }
    }

    public static void main(String[] args) {
        LinkedTree<String> tree = new LinkedTree<>();

        LinkedTree.Node<String> a = tree.addRoot("A");
        LinkedTree.Node<String> b = tree.addChild(a, "B");
        tree.addChild(a, "C");
        LinkedTree.Node<String> d = tree.addChild(a, "D");
        tree.addChild(b, "E");
        tree.addChild(b, "F");
        LinkedTree.Node<String> g = tree.addChild(d, "G");
        tree.addChild(d, "H");
        tree.addChild(g, "I");

        System.out.println("Exercise 2:");
        System.out.println("Postorder output (element -> height):");
        tree.printElementsWithSubtreeHeights();
    }
}
