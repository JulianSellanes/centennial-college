package com.ex3;

// Julian Sellanes (301494667)

import java.util.Arrays;

public class App {
    /**
     * Returns an array containing the k largest values from arr.
     *
     * Running time: O(n) since k = 10 is a constant
     * Extra space:  O(k) = O(1) because k is constant.
     */
    public static int[] tenLargest(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }

        int k = Math.min(10, arr.length);
        int[] bestIdx = new int[k];
        Arrays.fill(bestIdx, -1);

        for (int found = 0; found < k; found++) {
            int best = -1;

            for (int i = 0; i < arr.length; i++) {
                if (isAlreadyChosen(i, bestIdx, found)) continue;

                if (best == -1 || arr[i] > arr[best]) {
                    best = i;
                }
            }

            bestIdx[found] = best;
        }

        // Convert indices into values
        int[] result = new int[k];

        for (int i = 0; i < k; i++) {
            result[i] = arr[bestIdx[i]];
        }

        return result;
    }

    // Checks only the first count chosen indices
    private static boolean isAlreadyChosen(int index, int[] chosen, int count) {
        for (int i = 0; i < count; i++) {
            if (chosen[i] == index)
                return true;
        }

        return false;
    }
    public static void main(String[] args) {
        // Test
        int[] arr = {12, 5, 99, 3, 45, 77, 1, 88, 34, 100, 67, 23, 90, 2, 56};

        System.out.println("arr = " + Arrays.toString(arr));

        int[] top10 = tenLargest(arr);
        System.out.println("Top 10 (desc) = " + Arrays.toString(top10));

        // Test with smaller array
        int[] smallArr = {4, 1, 9};
        System.out.println("\nsmallarr = " + Arrays.toString(smallArr));
        System.out.println("Top (up to 10) = " + Arrays.toString(tenLargest(smallArr)));
    }
}