package com.lab6;

import java.util.ArrayList;
import java.util.Comparator;

// Julian Sellanes (301494667)

public class Exercise2 {
    public static void main(String[] args) {
        SortedTableMap<String, Integer> scores = new SortedTableMap<>();
        scores.put("pear", 5);
        scores.put("apple", null);
        scores.put("orange", 9);
        scores.put("banana", 7);

        System.out.println("Exercise 2:");
        System.out.println("Entries in sorted-key order:");
        for (Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println();
        printLookup(scores, "apple");
        printLookup(scores, "grape");
        scores.remove("grape");

        System.out.println();
        System.out.println("Both get(\"apple\") and get(\"grape\") return null.");
        System.out.println();
        System.out.println("containKey(\"apple\") is true because the key exists with a null value.");
        System.out.println("containKey(\"grape\") is false because the key is absent.");
    }

    private static void printLookup(SortedTableMap<String, Integer> map, String key) {
        System.out.println("get(\"" + key + "\") = " + map.get(key));
        System.out.println("containKey(\"" + key + "\") = " + map.containKey(key));
    }

    private interface Entry<K, V> {
        K getKey();

        V getValue();
    }

    private static final class MapEntry<K, V> implements Entry<K, V> {
        private final K key;
        private V value;

        private MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        private V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    private static final class DefaultComparator<E> implements Comparator<E> {
        @Override
        @SuppressWarnings("unchecked")
        public int compare(E first, E second) {
            return ((Comparable<E>) first).compareTo(second);
        }
    }

    private static final class SortedTableMap<K, V> {
        private final ArrayList<MapEntry<K, V>> table = new ArrayList<>();
        private final Comparator<K> comparator;

        private SortedTableMap() {
            this(new DefaultComparator<>());
        }

        private SortedTableMap(Comparator<K> comparator) {
            this.comparator = comparator;
        }

        private int compare(K key, MapEntry<K, V> entry) {
            return comparator.compare(key, entry.getKey());
        }

        private void checkKey(K key) {
            try {
                comparator.compare(key, key);
            } catch (ClassCastException exception) {
                throw new IllegalArgumentException("Incompatible key", exception);
            }
        }

        private int findIndex(K key, int low, int high) {
            if (high < low) {
                return high + 1;
            }

            int mid = (low + high) / 2;
            int comparison = compare(key, table.get(mid));

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                return findIndex(key, low, mid - 1);
            } else {
                return findIndex(key, mid + 1, high);
            }
        }

        private int findIndex(K key) {
            return findIndex(key, 0, table.size() - 1);
        }

        public int size() {
            return table.size();
        }

        public boolean containKey(K key) {
            checkKey(key);
            int index = findIndex(key);
            return index < size() && compare(key, table.get(index)) == 0;
        }

        public V get(K key) {
            checkKey(key);
            int index = findIndex(key);
            if (index == size() || compare(key, table.get(index)) != 0) {
                return null;
            }
            return table.get(index).getValue();
        }

        public V put(K key, V value) {
            checkKey(key);
            int index = findIndex(key);
            if (index < size() && compare(key, table.get(index)) == 0) {
                return table.get(index).setValue(value);
            }

            table.add(index, new MapEntry<>(key, value));
            return null;
        }

        public V remove(K key) {
            checkKey(key);
            int index = findIndex(key);
            if (index == size() || compare(key, table.get(index)) != 0) {
                return null;
            }
            return table.remove(index).getValue();
        }

        public Iterable<Entry<K, V>> entrySet() {
            ArrayList<Entry<K, V>> snapshot = new ArrayList<>(table.size());
            snapshot.addAll(table);
            return snapshot;
        }
    }
}
