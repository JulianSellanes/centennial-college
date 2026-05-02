package exercise3;

import java.util.ArrayList;
import java.util.List;

// Julian Sellanes (301494667)

public class App {
    private static final class HeapPriorityQueue<K extends Comparable<K>, V> {
        private static final class Entry<K, V> {
            private final K key;
            private final V value;

            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public String toString() {
                return "(" + key + ", " + value + ")";
            }
        }

        private final List<Entry<K, V>> heap = new ArrayList<>();

        int size() {
            return heap.size();
        }

        boolean isEmpty() {
            return heap.isEmpty();
        }

        Entry<K, V> min() {
            return heap.isEmpty() ? null : heap.get(0);
        }

        void insert(K key, V value) {
            heap.add(new Entry<>(key, value));
            upheap(heap.size() - 1);
        }

        Entry<K, V> removeMin() {
            if (heap.isEmpty()) {
                return null;
            }

            Entry<K, V> answer = heap.get(0);
            swap(0, heap.size() - 1);
            heap.remove(heap.size() - 1);

            if (!heap.isEmpty()) {
                downheap(0);
            }

            return answer;
        }

        String heapSnapshot() {
            return heap.toString();
        }

        private int parent(int j) {
            return (j - 1) / 2;
        }

        private int left(int j) {
            return 2 * j + 1;
        }

        private int right(int j) {
            return 2 * j + 2;
        }

        private boolean hasLeft(int j) {
            return left(j) < heap.size();
        }

        private boolean hasRight(int j) {
            return right(j) < heap.size();
        }

        private int compare(Entry<K, V> first, Entry<K, V> second) {
            return first.key.compareTo(second.key);
        }

        private void swap(int i, int j) {
            Entry<K, V> temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        private void upheap(int j) {
            if (j == 0) {
                return;
            }

            int parentIndex = parent(j);
            if (compare(heap.get(j), heap.get(parentIndex)) >= 0) {
                return;
            }

            swap(j, parentIndex);
            upheap(parentIndex);
        }

        private void downheap(int j) {
            while (hasLeft(j)) {
                int leftIndex = left(j);
                int smallChildIndex = leftIndex;

                if (hasRight(j)) {
                    int rightIndex = right(j);
                    if (compare(heap.get(leftIndex), heap.get(rightIndex)) > 0) {
                        smallChildIndex = rightIndex;
                    }
                }

                if (compare(heap.get(smallChildIndex), heap.get(j)) >= 0) {
                    break;
                }

                swap(j, smallChildIndex);
                j = smallChildIndex;
            }
        }
    }

    public static void main(String[] args) {
        HeapPriorityQueue<Integer, String> heapQueue = new HeapPriorityQueue<>();

        int[] keys = {47, 75, 28, 51, 31, 22, 15};
        String[] values = {"A", "C", "B", "D", "F", "G", "H"};

        System.out.println("Exercise 3:");
        for (int i = 0; i < keys.length; i++) {
            heapQueue.insert(keys[i], values[i]);
            System.out.println("After insert " + keys[i] + ": " + heapQueue.heapSnapshot());
        }

        System.out.println();
        System.out.println("Minimum entry: " + heapQueue.min());
        System.out.println("Heap size: " + heapQueue.size());
        System.out.println();

        System.out.println("Removing entries in priority order:");
        while (!heapQueue.isEmpty()) {
            System.out.println("removeMin -> " + heapQueue.removeMin());
        }
    }
}