package com.finaltest;

import java.util.ArrayList;

// Julian Sellanes (301494667)

public class Exercise2 {
    public interface Entry<K, V> {
        K getKey();
        V getValue();
    }

    public static class MapEntry<K, V> implements Entry<K, V> {
        private K key;
        private V value;

        public MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey()   { return key; }
        public V getValue() { return value; }

        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public String toString() {
            return "<" + key + ", " + value + ">";
        }
    }

    public static class UnsortedTableMap<K, V> {

        private ArrayList<MapEntry<K, V>> table = new ArrayList<>();

        public UnsortedTableMap() { }

        private int findIndex(K key) {
            int n = table.size();
            for (int j = 0; j < n; j++) {
                if (table.get(j).getKey().equals(key)) {
                    return j;
                }
            }
            return -1;
        }

        public int size() {
            return table.size();
        }

        public V get(K key) {
            int j = findIndex(key);
            if (j == -1) return null;
            return table.get(j).getValue();
        }

        public V put(K key, V value) {
            int j = findIndex(key);
            if (j == -1) {
                table.add(new MapEntry<>(key, value));
                return null;
            }
            return table.get(j).setValue(value);
        }

        public V putOnlyIfAbsent(K key, V value) {
            int j = findIndex(key);
            if (j == -1) {
                table.add(new MapEntry<>(key, value));
                return null;
            }
            return table.get(j).getValue();
        }

        @Override
        public String toString() {
            return table.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("Exercise 2:");

        UnsortedTableMap<String, Integer> map = new UnsortedTableMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);

        System.out.println("Initial map: " + map);
        System.out.println("Initial size: " + map.size());
        System.out.println();

        // Case 1: key is absent -> the entry is inserted, method returns null.
        Integer r1 = map.putOnlyIfAbsent("date", 4);
        System.out.println("putOnlyIfAbsent(\"date\", 4) returned: " + r1 + "  (expected null)");
        System.out.println("Map after insert of new key: " + map);
        System.out.println("Size: " + map.size() + "  (expected 4)");
        System.out.println();

        // Case 2: key is present -> method returns existing value, map is untouched.
        Integer r2 = map.putOnlyIfAbsent("banana", 999);
        System.out.println("putOnlyIfAbsent(\"banana\", 999) returned: " + r2 + "  (expected 2)");
        System.out.println("get(\"banana\") = " + map.get("banana") + "  (expected 2, NOT 999)");
        System.out.println("Size: " + map.size() + "  (expected 4, unchanged)");
        System.out.println();

        // Case 3: another absent key, just to confirm repeated use still works.
        Integer r3 = map.putOnlyIfAbsent("elderberry", 5);
        System.out.println("putOnlyIfAbsent(\"elderberry\", 5) returned: " + r3 + "  (expected null)");
        System.out.println("Final map: " + map);
        System.out.println("Final size: " + map.size() + "  (expected 5)");
        System.out.println();

        // Case 4: contrast with the standard put, which DOES replace.
        Integer r4 = map.put("apple", 100);
        System.out.println("put(\"apple\", 100) returned: " + r4 + "  (expected 1)");
        System.out.println("get(\"apple\") = " + map.get("apple") + "  (expected 100)");
    }
}