// Julian Sellanes (301494667)

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

// Exercise 2:
public class PrefixAverage {

  private static final Random RNG = new Random(42);

  private static double[] randomArray(int n) {
    double[] x = new double[n];

    for (int i = 0; i < n; i++)
      x[i] = RNG.nextDouble();

    return x;
  }

  // Returns average time (ms) over reps runs. Does not include input generation time.
  private static long timeMillis(Runnable r, int reps) {
    r.run();
    long start = System.nanoTime();

    for (int i = 0; i < reps; i++)
      r.run();

    long end = System.nanoTime();
    return (end - start) / 1_000_000L / reps;
  }

  // Returns an array a such that, for all j, a[j] equals the average of x[0], ..., x[j].
  public static double[] prefixAverage1(double[] x) {
    int n = x.length;
    double[] a = new double[n];

    for (int j=0; j < n; j++) {
      double total = 0;

      for (int i=0; i <= j; i++)
        total += x[i];

      a[j] = total / (j+1);
    }

    return a;
  }

  // Returns an array a such that, for all j, a[j] equals the average of x[0], ..., x[j].
  public static double[] prefixAverage2(double[] x) {
    int n = x.length;
    double[] a = new double[n];
    double total = 0;

    for (int j=0; j < n; j++) {
      total += x[j];         
      a[j] = total / (j+1);   
    }

    return a;
  }

  public static void main(String[] args) {
    Locale.setDefault(Locale.US);

    boolean plot = false;
    int startN = 1_000;
    int trials = 8;
    int reps = 3;

    // Simple arg parsing
    for (String a : args) {
      if (a.equalsIgnoreCase("--plot")) plot = true;
      if (a.startsWith("--start=")) startN = Integer.parseInt(a.substring("--start=".length()));
      if (a.startsWith("--trials=")) trials = Integer.parseInt(a.substring("--trials=".length()));
      if (a.startsWith("--reps=")) reps = Integer.parseInt(a.substring("--reps=".length()));
    }

    System.out.println("n\tms(prefixAverage2)\tms(prefixAverage1)");

    List<Integer> ns = new ArrayList<>();
    List<Long> t2 = new ArrayList<>();
    List<Long> t1 = new ArrayList<>();

    int n = startN;
    boolean stopSlowVersion = false;           
    long stopIfOverMs = 15_000;      

    for (int t = 0; t < trials; t++) {
      double[] x = randomArray(n);

      long ms2 = timeMillis(() -> prefixAverage2(x), reps);
      long ms1 = -1;
      if (!stopSlowVersion) {
        ms1 = timeMillis(() -> prefixAverage1(x), 1);
        if (ms1 > stopIfOverMs) stopSlowVersion = true;
      }

      System.out.printf("%d\t%d\t\t\t%s%n", n, ms2, (ms1 < 0 ? "(skipped)" : Long.toString(ms1)));

      ns.add(n);
      t2.add(ms2);
      t1.add(ms1);
      n *= 2;
    }

    if (plot) {
      SwingUtilities.invokeLater(() -> {
        JFrame f = new JFrame("PrefixAverage Experimental Analysis (log-log)");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(900, 600);
        f.setLocationRelativeTo(null);
        f.setContentPane(new LogLogPlotPanel(ns, t1, t2));
        f.setVisible(true);
      });
    }
  }

  // Minimal log-log plotter using Swing.
  private static class LogLogPlotPanel extends JPanel {
    private final List<Integer> ns;
    private final List<Long> t1;
    private final List<Long> t2;

    LogLogPlotPanel(List<Integer> ns, List<Long> t1, List<Long> t2) {
      this.ns = ns;
      this.t1 = t1;
      this.t2 = t2;
      setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      int w = getWidth();
      int h = getHeight();
      int pad = 60;

      double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
      double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;

      for (int i = 0; i < ns.size(); i++) {
        double x = Math.log10(ns.get(i));
        minX = Math.min(minX, x);
        maxX = Math.max(maxX, x);

        long y2ms = Math.max(1, t2.get(i));
        double y2 = Math.log10(y2ms);
        minY = Math.min(minY, y2);
        maxY = Math.max(maxY, y2);

        if (t1.get(i) != null && t1.get(i) >= 1) {
          double y1 = Math.log10(t1.get(i));
          minY = Math.min(minY, y1);
          maxY = Math.max(maxY, y1);
        }
      }

      double xSpan = Math.max(1e-9, maxX - minX);
      double ySpan = Math.max(1e-9, maxY - minY);
      minX -= 0.05 * xSpan; maxX += 0.05 * xSpan;
      minY -= 0.05 * ySpan; maxY += 0.05 * ySpan;

      g2.setColor(Color.BLACK);
      g2.drawLine(pad, h - pad, w - pad, h - pad);
      g2.drawLine(pad, pad, pad, h - pad);

      g2.drawString("log10(n)", w / 2 - 20, h - 20);
      g2.drawString("log10(ms)", 15, h / 2);

      final double fMinX = minX, fMaxX = maxX, fMinY = minY, fMaxY = maxY;
      final int fW = w, fH = h, fPad = pad;

      g2.setColor(Color.BLUE);
      for (int i = 0; i < ns.size(); i++) {
        int x = mapX(fMinX, fMaxX, fW, fPad, Math.log10(ns.get(i)));
        int y = mapY(fMinY, fMaxY, fH, fPad, Math.log10(Math.max(1, t2.get(i))));
        g2.fillOval(x - 4, y - 4, 8, 8);
        if (i > 0) {
          int x0 = mapX(fMinX, fMaxX, fW, fPad, Math.log10(ns.get(i - 1)));
          int y0 = mapY(fMinY, fMaxY, fH, fPad, Math.log10(Math.max(1, t2.get(i - 1))));
          g2.drawLine(x0, y0, x, y);
        }
      }

      g2.setColor(Color.RED);
      Integer lastX = null, lastY = null;
      for (int i = 0; i < ns.size(); i++) {
        Long ms = t1.get(i);
        
        if (ms == null || ms < 1) continue;

        int x = mapX(fMinX, fMaxX, fW, fPad, Math.log10(ns.get(i)));
        int y = mapY(fMinY, fMaxY, fH, fPad, Math.log10(ms));
        g2.fillRect(x - 4, y - 4, 8, 8);

        if (lastX != null) g2.drawLine(lastX, lastY, x, y);

        lastX = x;
        lastY = y;
      }

      g2.setColor(Color.BLACK);
      g2.drawString("Blue circles: prefixAverage2 (O(n))", pad, pad - 25);
      g2.drawString("Red squares:  prefixAverage1 (O(n^2))", pad, pad - 10);
    }

    private static int mapX(double minX, double maxX, int w, int pad, double lx) {
      return (int) (pad + (lx - minX) / (maxX - minX) * (w - 2.0 * pad));
    }

    private static int mapY(double minY, double maxY, int h, int pad, double ly) {
      return (int) (h - pad - (ly - minY) / (maxY - minY) * (h - 2.0 * pad));
    }
  }
}
