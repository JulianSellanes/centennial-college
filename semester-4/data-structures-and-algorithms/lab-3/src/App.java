import java.io.File;
import java.util.Scanner;

// Julian Sellanes (301494667)

public class App {

    // Exercise 1: Recursive Product
    public static int recursiveProduct(int m, int n) {
        if (m < 0 || n < 0) throw new IllegalArgumentException("m and n must be non-negative.");

        // Base cases
        if (m == 0 || n == 0) return 0;
        if (m == 1) return n;

        // Uses only + and - for the multiplication.
        if (m < n) {
            // m * n = n + (m - 1) * n
            return n + recursiveProduct(m - 1, n);
        } else {
            // m * n = m + (n - 1) * m
            return m + recursiveProduct(n - 1, m);
        }
    }

    // Exercise 2: Recursive Palindrome
    public static boolean isPalindrome(String s) {
        if (s == null) return false;
        return isPalindromeHelper(s, 0, s.length() - 1);
    }

    private static boolean isPalindromeHelper(String s, int left, int right) {
        // Base case: everything matched so far
        if (left >= right) return true;

        // If ends don't match, not a palindrome
        if (s.charAt(left) != s.charAt(right)) return false;

        // Recur inward
        return isPalindromeHelper(s, left + 1, right - 1);
    }

    // Exercise 3: Recursive file finder
    public static void find(String path, String filename) {
        File root = new File(path);

        if (!root.exists()) {
            System.out.println("Path does not exist: " + root.getAbsolutePath());
            return;
        }

        find(root, filename);
    }

    private static void find(File current, String filename) {
        if (current == null) return;

        // Check file's name
        if (current.isFile()) {
            if (current.getName().equals(filename)) {
                System.out.println("FOUND: " + current.getAbsolutePath());
            }
            return;
        }

        // If it's a directory, recurse into its children
        if (current.isDirectory()) {
            File[] children = current.listFiles();

            if (children == null) return;

            for (File child : children)
                find(child, filename);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Recursion ===");
            System.out.println("1) Recursive product");
            System.out.println("2) Palindrome checker");
            System.out.println("3) Find file by name");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Bye!");
                break;
            }

            switch (choice) {
                case "1":
                    System.out.print("Enter m (non-negative): ");
                    int m = Integer.parseInt(sc.nextLine().trim());

                    System.out.print("Enter n (non-negative): ");
                    int n = Integer.parseInt(sc.nextLine().trim());

                    int product = recursiveProduct(m, n);
                    System.out.println("Result: " + m + " * " + n + " = " + product);
                    break;

                case "2":
                    System.out.print("Enter a string: ");
                    String s = sc.nextLine();

                    boolean ok = isPalindrome(s);
                    System.out.println("Palindrome? " + ok);
                    break;

                case "3":
                    System.out.print("Enter a root path (/Users/juliansellanes/Desktop): ");
                    String path = sc.nextLine().trim();

                    System.out.print("Enter filename to find: ");
                    String filename = sc.nextLine().trim();

                    System.out.println("\nSearching...");
                    find(path, filename);
                    System.out.println("Done.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }
}
