// Julian Sellanes (301494667)

import java.util.Arrays;
import java.util.Random;

// Exercise 3:
public class Uniqueness {

  private static final Random RNG = new Random(42);

  private enum Algo { UNIQUE1, UNIQUE2 }

  // Returns true if there are no duplicate elements in the array.
  public static boolean unique1(int[] data) {
    int n = data.length;
    for (int j=0; j < n-1; j++)
      for (int k=j+1; k < n; k++)
        if (data[j] == data[k])
          return false;              
    return true;                      
  }

  // Returns true if there are no duplicate elements in the array.
  public static boolean unique2(int[] data) {
    int n = data.length;
    int[] temp = Arrays.copyOf(data, n);  
    Arrays.sort(temp);                    
    for (int j=0; j < n-1; j++)
      if (temp[j] == temp[j+1])          
        return false;                 
    return true;                       
  }

  // Creates an int[] containing 0..n-1, then shuffles it. This guarantees there are no duplicates.
  private static int[] shuffledRange(int n) {
    int[] a = new int[n];
    for (int i = 0; i < n; i++)
      a[i] = i;

    for (int i = n - 1; i > 0; i--) {
      int j = RNG.nextInt(i + 1);
      int tmp = a[i];
      a[i] = a[j];
      a[j] = tmp;
    }
    return a;
  }

  // Times only the algorithm call (not input generation). Returns elapsed ms.
  private static long timeOnce(Algo algo, int n) {
    int[] data = shuffledRange(n);
    long start = System.nanoTime();
    boolean result;
    switch (algo) {
      case UNIQUE1:
        result = unique1(data);
        break;
      case UNIQUE2:
        result = unique2(data);
        break;
      default:
        throw new IllegalArgumentException("Unknown algorithm");
    }
    long end = System.nanoTime();

    if (!result) throw new AssertionError("Input was constructed to be unique; result should be true");
    return (end - start) / 1_000_000L;
  }

  // Find the largest n with runtime <= limitMs.
  private static int maxNWithinLimit(Algo algo, long limitMs, int startN, int maxN) {
    timeOnce(algo, Math.min(200, startN));

    int low = startN;
    long tLow = timeOnce(algo, low);
    if (tLow > limitMs) return 0;

    int high = low;
    long tHigh = tLow;

    while (high < maxN) {
      int next = nextCandidate(algo, high, tHigh, limitMs);
      next = Math.min(next, maxN);
      if (next <= high) next = Math.min(maxN, high + 1);
      if (next == high) break;

      long tNext = timeOnce(algo, next);
      System.out.printf("  probe n=%d -> %d ms%n", next, tNext);

      if (tNext <= limitMs) {
        low = next;
        tLow = tNext;
        high = next;
        tHigh = tNext;
      } else {
        high = next;
        tHigh = tNext;
        break;
      }
    }

    if (high == maxN && tHigh <= limitMs) return maxN;

    int left = low;
    int right = high;
    while (left + 1 < right) {
      int mid = left + (right - left) / 2;
      long tMid = timeOnce(algo, mid);
      System.out.printf("  mid n=%d -> %d ms%n", mid, tMid);
      
      if (tMid <= limitMs) {
        left = mid;
      } else {
        right = mid;
      }
    }
    return left;
  }

  // Picks a new n that moves toward the time limit without overshooting too far.
  private static int nextCandidate(Algo algo, int n, long tMs, long limitMs) {
    if (tMs <= 0) return n + 1;

    double factor;

    if (algo == Algo.UNIQUE1) {
      factor = Math.sqrt((double) limitMs / (double) tMs);
      
      factor = Math.min(factor, 1.4);   // at most +40% each step
      factor = Math.max(factor, 1.10);  // at least +10% progress
    } else {
      // n log n
      factor = Math.min((double) limitMs / (double) tMs, 2.0);
      factor = Math.max(factor, 1.25);
    }

    long next = (long) Math.floor(n * factor);
    if (next <= n) next = (long) n + 1;
    if (next > Integer.MAX_VALUE) next = Integer.MAX_VALUE;

    return (int) next;
  }

  private static int defaultMaxNFromHeap() {
    // Conservative cap based on heap to avoid OutOfMemoryError.
    long heap = Runtime.getRuntime().maxMemory();
    long n = heap / 16L;

    return (int) Math.max(1_000, Math.min(n, 50_000_000L));
  }

  public static void main(String[] args) {
    long limitMs = 60_000;
    int startN = 1_000;
    int maxN = defaultMaxNFromHeap();

    for (String a : args) {
      if (a.startsWith("--limitMs=")) limitMs = Long.parseLong(a.substring("--limitMs=".length()));
      if (a.startsWith("--start=")) startN = Integer.parseInt(a.substring("--start=".length()));
      if (a.startsWith("--maxN=")) maxN = Integer.parseInt(a.substring("--maxN=".length()));
    }

    System.out.printf("Time limit: %d ms%n", limitMs);
    System.out.printf("startN=%d, maxN=%d (based on heap)%n%n", startN, maxN);

    System.out.println("Finding max n for unique2 (O(n log n))...");
    int max2 = maxNWithinLimit(Algo.UNIQUE2, limitMs, startN, maxN);
    System.out.printf("=> unique2 max n within limit: %d%n%n", max2);

    System.out.println("Finding max n for unique1 (O(n^2))...");
    int max1 = maxNWithinLimit(Algo.UNIQUE1, limitMs, startN, Math.min(maxN, max2));
    System.out.printf("=> unique1 max n within limit: %d%n", max1);
  }
}
