package com.lab6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

// Julian Sellanes (301494667)

public class Exercise1 {
    private static final int ENTRY_COUNT = 50_000;
    private static final int LOOKUP_COUNT = 50_000;
    private static final int TRIALS = 3;
    private static final long SEED = 20260406L;
    private static final double[] LOAD_LIMITS = {0.50, 0.75, 0.90};

    public static void main(String[] args) {
        List<Integer> insertKeys = buildRandomKeys(ENTRY_COUNT, 0, SEED);
        List<Integer> missingKeys = buildRandomKeys(LOOKUP_COUNT, ENTRY_COUNT * 3, SEED + 1);

        System.out.println("Exercise 1:");
        System.out.println("(Using the same random key set for each load limit)");
        System.out.println("");

        System.out.printf(
                Locale.US,
                "%-10s %-10s %-12s %-12s %-12s %-10s %-8s%n",
                "MaxLoad",
                "FinalLoad",
                "Insert(ms)",
                "HitGet(ms)",
                "MissGet(ms)",
                "Capacity",
                "Resizes"
        );

        long checksum = 0;
        for (double loadLimit : LOAD_LIMITS) {
            ExperimentResult result = runExperiment(insertKeys, missingKeys, loadLimit);
            checksum += result.checksum;

            System.out.printf(
                    Locale.US,
                    "%-10.2f %-10.3f %-12.3f %-12.3f %-12.3f %-10d %-8d%n",
                    result.maxLoadFactor,
                    result.finalLoadFactor,
                    result.averageInsertMillis,
                    result.averageHitLookupMillis,
                    result.averageMissLookupMillis,
                    result.finalCapacity,
                    result.resizeCount
            );
        }

        System.out.println();
        System.out.println("Checksum: " + checksum);
    }

    private static List<Integer> buildRandomKeys(int count, int startValue, long seed) {
        ArrayList<Integer> keys = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            keys.add(startValue + i);
        }
        Collections.shuffle(keys, new Random(seed));
        return keys;
    }

    private static ExperimentResult runExperiment(
            List<Integer> insertKeys,
            List<Integer> missingKeys,
            double maxLoadFactor
    ) {
        long totalInsertNanos = 0;
        long totalHitLookupNanos = 0;
        long totalMissLookupNanos = 0;
        long checksum = 0;
        int finalCapacity = 0;
        int resizeCount = 0;
        double finalLoadFactor = 0.0;

        for (int trial = 0; trial < TRIALS; trial++) {
            ChainHashMap<Integer, Integer> map = new ChainHashMap<>(17, 109345121, maxLoadFactor);

            long start = System.nanoTime();
            for (Integer key : insertKeys) {
                map.put(key, key);
            }
            totalInsertNanos += System.nanoTime() - start;

            start = System.nanoTime();
            for (Integer key : insertKeys) {
                Integer value = map.get(key);
                if (value != null) {
                    checksum += value;
                }
            }
            totalHitLookupNanos += System.nanoTime() - start;

            start = System.nanoTime();
            for (Integer key : missingKeys) {
                Integer value = map.get(key);
                if (value != null) {
                    checksum += value;
                }
            }
            totalMissLookupNanos += System.nanoTime() - start;

            map.remove(-1);
            finalCapacity = map.capacity();
            resizeCount = map.resizeCount();
            finalLoadFactor = map.loadFactor();
        }

        return new ExperimentResult(
                maxLoadFactor,
                finalLoadFactor,
                nanosToMillis(totalInsertNanos / (double) TRIALS),
                nanosToMillis(totalHitLookupNanos / (double) TRIALS),
                nanosToMillis(totalMissLookupNanos / (double) TRIALS),
                finalCapacity,
                resizeCount,
                checksum
        );
    }

    private static double nanosToMillis(double nanos) {
        return nanos / 1_000_000.0;
    }

    private static final class ExperimentResult {
        private final double maxLoadFactor;
        private final double finalLoadFactor;
        private final double averageInsertMillis;
        private final double averageHitLookupMillis;
        private final double averageMissLookupMillis;
        private final int finalCapacity;
        private final int resizeCount;
        private final long checksum;

        private ExperimentResult(
                double maxLoadFactor,
                double finalLoadFactor,
                double averageInsertMillis,
                double averageHitLookupMillis,
                double averageMissLookupMillis,
                int finalCapacity,
                int resizeCount,
                long checksum
        ) {
            this.maxLoadFactor = maxLoadFactor;
            this.finalLoadFactor = finalLoadFactor;
            this.averageInsertMillis = averageInsertMillis;
            this.averageHitLookupMillis = averageHitLookupMillis;
            this.averageMissLookupMillis = averageMissLookupMillis;
            this.finalCapacity = finalCapacity;
            this.resizeCount = resizeCount;
            this.checksum = checksum;
        }
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

    private static final class UnsortedTableMap<K, V> {
        private final ArrayList<MapEntry<K, V>> table = new ArrayList<>();

        private int size() {
            return table.size();
        }

        private int findIndex(K key) {
            for (int index = 0; index < table.size(); index++) {
                if (table.get(index).getKey().equals(key)) {
                    return index;
                }
            }
            return -1;
        }

        private V get(K key) {
            int index = findIndex(key);
            if (index == -1) {
                return null;
            }
            return table.get(index).getValue();
        }

        private V put(K key, V value) {
            int index = findIndex(key);
            if (index == -1) {
                table.add(new MapEntry<>(key, value));
                return null;
            }
            return table.get(index).setValue(value);
        }

        private V remove(K key) {
            int index = findIndex(key);
            int lastIndex = table.size() - 1;
            if (index == -1) {
                return null;
            }

            V removedValue = table.get(index).getValue();
            if (index != lastIndex) {
                table.set(index, table.get(lastIndex));
            }
            table.remove(lastIndex);
            return removedValue;
        }

        private Iterable<Entry<K, V>> entrySet() {
            ArrayList<Entry<K, V>> snapshot = new ArrayList<>(table.size());
            snapshot.addAll(table);
            return snapshot;
        }
    }

    private abstract static class AbstractHashMap<K, V> {
        private static final int DEFAULT_CAPACITY = 17;
        private static final int DEFAULT_PRIME = 109345121;

        protected int n = 0;
        protected int capacity;
        private final int prime;
        private final long scale;
        private final long shift;
        // This field replaces the fixed 0.5 rule from the original class.
        private final double maxLoadFactor;
        private int resizeCount;

        private AbstractHashMap(int cap, int prime, double maxLoadFactor) {
            if (cap <= 0) {
                throw new IllegalArgumentException("Capacity must be positive.");
            }
            if (prime <= 1) {
                throw new IllegalArgumentException("Prime must be greater than 1.");
            }
            validateMaxLoadFactor(maxLoadFactor);

            this.capacity = cap;
            this.prime = prime;
            this.maxLoadFactor = maxLoadFactor;

            Random random = new Random();
            scale = random.nextInt(prime - 1) + 1;
            shift = random.nextInt(prime);
            createTable();
        }

        private AbstractHashMap(int cap, int prime) {
            this(cap, prime, 0.50);
        }

        private AbstractHashMap(int cap, double maxLoadFactor) {
            this(cap, DEFAULT_PRIME, maxLoadFactor);
        }

        private AbstractHashMap(double maxLoadFactor) {
            this(DEFAULT_CAPACITY, DEFAULT_PRIME, maxLoadFactor);
        }

        private AbstractHashMap(int cap) {
            this(cap, DEFAULT_PRIME, 0.50);
        }

        private AbstractHashMap() {
            this(DEFAULT_CAPACITY, DEFAULT_PRIME, 0.50);
        }

        private static void validateMaxLoadFactor(double maxLoadFactor) {
            if (!Double.isFinite(maxLoadFactor) || maxLoadFactor <= 0.0) {
                throw new IllegalArgumentException("Maximum load factor must be positive.");
            }
        }

        public int size() {
            return n;
        }

        public V get(K key) {
            return bucketGet(hashValue(key), key);
        }

        public V remove(K key) {
            return bucketRemove(hashValue(key), key);
        }

        public V put(K key, V value) {
            V answer = bucketPut(hashValue(key), key, value);
            if (loadFactor() > maxLoadFactor) {
                resizeCount++;
                resize(2 * capacity - 1);
            }
            return answer;
        }

        public double loadFactor() {
            return (double) size() / capacity;
        }

        public int capacity() {
            return capacity;
        }

        public int resizeCount() {
            return resizeCount;
        }

        private int hashValue(K key) {
            long hash = (long) key.hashCode() * scale + shift;
            hash %= prime;
            if (hash < 0) {
                hash += prime;
            }
            return (int) (hash % capacity);
        }

        private void resize(int newCapacity) {
            ArrayList<Entry<K, V>> buffer = new ArrayList<>(n);
            for (Entry<K, V> entry : entrySet()) {
                buffer.add(entry);
            }

            capacity = newCapacity;
            createTable();
            n = 0;

            for (Entry<K, V> entry : buffer) {
                bucketPut(hashValue(entry.getKey()), entry.getKey(), entry.getValue());
            }
        }

        protected abstract void createTable();

        protected abstract V bucketGet(int hash, K key);

        protected abstract V bucketPut(int hash, K key, V value);

        protected abstract V bucketRemove(int hash, K key);

        protected abstract Iterable<Entry<K, V>> entrySet();
    }

    private static final class ChainHashMap<K, V> extends AbstractHashMap<K, V> {
        private UnsortedTableMap<K, V>[] table;

        private ChainHashMap() {
            super();
        }

        private ChainHashMap(int cap) {
            super(cap);
        }

        private ChainHashMap(int cap, int prime) {
            super(cap, prime);
        }

        private ChainHashMap(double maxLoadFactor) {
            super(maxLoadFactor);
        }

        private ChainHashMap(int cap, double maxLoadFactor) {
            super(cap, maxLoadFactor);
        }

        private ChainHashMap(int cap, int prime, double maxLoadFactor) {
            super(cap, prime, maxLoadFactor);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void createTable() {
            table = (UnsortedTableMap<K, V>[]) new UnsortedTableMap[capacity];
        }

        @Override
        protected V bucketGet(int hash, K key) {
            UnsortedTableMap<K, V> bucket = table[hash];
            if (bucket == null) {
                return null;
            }
            return bucket.get(key);
        }

        @Override
        protected V bucketPut(int hash, K key, V value) {
            UnsortedTableMap<K, V> bucket = table[hash];
            if (bucket == null) {
                bucket = new UnsortedTableMap<>();
                table[hash] = bucket;
            }

            int oldBucketSize = bucket.size();
            V answer = bucket.put(key, value);
            n += bucket.size() - oldBucketSize;
            return answer;
        }

        @Override
        protected V bucketRemove(int hash, K key) {
            UnsortedTableMap<K, V> bucket = table[hash];
            if (bucket == null) {
                return null;
            }

            int oldBucketSize = bucket.size();
            V answer = bucket.remove(key);
            n -= oldBucketSize - bucket.size();
            return answer;
        }

        @Override
        protected Iterable<Entry<K, V>> entrySet() {
            ArrayList<Entry<K, V>> buffer = new ArrayList<>();
            for (int index = 0; index < capacity; index++) {
                if (table[index] != null) {
                    for (Entry<K, V> entry : table[index].entrySet()) {
                        buffer.add(entry);
                    }
                }
            }
            return buffer;
        }
    }
}
