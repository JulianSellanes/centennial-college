package com.lab7;

import java.util.ArrayList;
import java.util.List;

// Julian Sellanes (301494667)

public class Exercise1 {
    private static class BinarySearchTree<K extends Comparable<K>, V> {
        private static class Node<K, V> {
            private final K key;
            private V value;
            private Node<K, V> left;
            private Node<K, V> right;

            private Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        private Node<K, V> root;

        public void put(K key, V value) {
            root = put(root, key, value);
        }

        private Node<K, V> put(Node<K, V> node, K key, V value) {
            if (node == null) {
                return new Node<>(key, value);
            }

            int comparison = key.compareTo(node.key);
            if (comparison < 0) {
                node.left = put(node.left, key, value);
            } else if (comparison > 0) {
                node.right = put(node.right, key, value);
            } else {
                node.value = value;
            }
            return node;
        }

        public V get(K key) {
            Node<K, V> match = treeSearch(root, key);
            return match == null ? null : match.value;
        }

        private Node<K, V> treeSearch(Node<K, V> current, K key) {
            while (current != null) {
                int comparison = key.compareTo(current.key);
                if (comparison == 0) {
                    return current;
                }
                current = comparison < 0 ? current.left : current.right;
            }
            return null;
        }

        public List<String> inOrderTraversal() {
            List<String> entries = new ArrayList<>();
            inOrderTraversal(root, entries);
            return entries;
        }

        private void inOrderTraversal(Node<K, V> node, List<String> entries) {
            if (node == null) {
                return;
            }
            inOrderTraversal(node.left, entries);
            entries.add(node.key + "=" + node.value);
            inOrderTraversal(node.right, entries);
        }
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer, String> tree = new BinarySearchTree<>();

        tree.put(6, "A");
        tree.put(2, "B");
        tree.put(4, "C");
        tree.put(1, "D");
        tree.put(9, "E");
        tree.put(8, "F");

        System.out.println("Exercise 1:\n");
        System.out.println("Tree contents in sorted order: " + tree.inOrderTraversal());
        System.out.println("Search for key 4: " + tree.get(4));
        System.out.println("Search for key 8: " + tree.get(8));
        System.out.println("Search for missing key 7: " + tree.get(7));
    }
}
