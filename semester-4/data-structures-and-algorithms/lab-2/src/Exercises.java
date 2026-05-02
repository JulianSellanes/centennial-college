// Julian Sellanes (301494667)

/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


/**
 * Code for end-of-chapter exercises on asymptotics.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
class Exercises {

  // Returns the sum of the integers in given array.
  public static int example1(int[] arr) {
    /*
     * Example 1
     * Big-Oh: O(n)
     * Explanation: There is one loop that runs n times, doing O(1) work each iteration
     * (one array access + one addition). Total work grows linearly with n.
     */
    int n = arr.length, total = 0;
    for (int j=0; j < n; j++)       // loop from 0 to n-1
      total += arr[j];
    return total;
  }

  // Returns the sum of the integers with even index in given array.
  public static int example2(int[] arr) {
    /*
     * Example 2
     * Big-Oh: O(n)
     * Explanation: The loop increments by 2, so it runs about n/2 times.
     * Big-Oh ignores constant factors, so n/2 is still O(n).
     */
    int n = arr.length, total = 0;
    for (int j=0; j < n; j += 2)    // note the increment of 2
      total += arr[j];
    return total;
  }

  // Returns the sum of the prefix sums of given array.
  public static int example3(int[] arr) {
    /*
     * Example 3
     * Big-Oh: O(n^2)
     * Explanation: Nested loops:
     * - outer loop j runs n times
     * - inner loop k runs (j+1) times for each j
     * Total inner iterations = 1 + 2 + ... + n = n(n+1)/2, which is O(n^2).
     */
    int n = arr.length, total = 0;
    for (int j=0; j < n; j++)       // loop from 0 to n-1
      for (int k=0; k <= j; k++)    // loop from 0 to j
        total += arr[j];
    return total;
  }

  // Returns the sum of the prefix sums of given array.
  public static int example4(int[] arr) {
     /*
     * Example 4
     * Big-Oh: O(n)
     * Explanation: One loop runs n times. Inside it does constant work:
     * two additions and assignments. Total grows linearly with n.
     */
    int n = arr.length, prefix = 0, total = 0;
    for (int j=0; j < n; j++) {     // loop from 0 to n-1
      prefix += arr[j];
      total += prefix;
    }
    return total;
  }

  // Returns the number of times second array stores sum of prefix sums from first.
  public static int example5(int[] first, int[] second) { // assume equal-length arrays
    /*
     * Example 5
     * Big-Oh: O(n^3)
     * Explanation:
     * - outer loop i runs n times
     * - inside it, there is a nested (j,k) structure:
     *     j runs n times, and for each j, k runs (j+1) times.
     * So the (j,k) work is O(n^2) total per i
     * Therefore total is n * O(n^2) = O(n^3).
     */
    int n = first.length, count = 0;
    for (int i=0; i < n; i++) {     // loop from 0 to n-1
      int total = 0;
      for (int j=0; j < n; j++)     // loop from 0 to n-1
        for (int k=0; k <= j; k++)  // loop from 0 to j
          total += first[k];
      if (second[i] == total) count++;
    }
    return count;
  }
}
