package com.ex1;

// Julian Sellanes (301494667)

import java.util.Arrays;
import java.util.Random;

public class App {
    /**
     * Returns the maximum of the first n elements of array arr.
     *
     * Running time: O(n)
     * Extra space:  O(n)
     */
    public static int recursiveMax(int[] arr, int n) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array must have at least 1 element.");
        }

        if (n < 1 || n > arr.length) {
            throw new IllegalArgumentException("n must be between 1 and A.length");
        }

        if (n == 1) {
            return arr[0];
        }

        return Math.max(recursiveMax(arr, n - 1), arr[n - 1]);
    }

    // Helper to verify correctness
    private static int iterativeMax(int[] arr) {
        int best = arr[0];
        
        for (int i = 1; i < arr.length; i++)
            best = Math.max(best, arr[i]);

        return best;
    }

    public static void main(String[] args) {
        Random random = new Random();

        // Test
        for (int t = 1; t <= 10; t++) {
            int n = 5 + random.nextInt(16);
            int[] arr = new int[n];

            for (int i = 0; i < n; i++) {
                arr[i] = random.nextInt(201) - 100;
            }

            int rec = recursiveMax(arr, arr.length);
            int it = iterativeMax(arr);

            System.out.println("Array = " + Arrays.toString(arr));
            System.out.println("recursiveMax = " + rec + " | iterativeMax = " + it);
            System.out.println(rec == it ? "OK\n" : "ERROR\n");
        }
    }
}