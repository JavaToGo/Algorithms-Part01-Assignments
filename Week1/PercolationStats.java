import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Use parameters greater than 0.");

        double[] pTh = new double[trials];

        for (int i = 0; i < trials; i++) {

            Percolation percolation = new Percolation(n);
            int runCount = 0;
            while (!percolation.percolates()) {
                int col;
                int row;

                do {
                    col = 1 + StdRandom.uniform(n);
                    row = 1 + StdRandom.uniform(n);
                } while (percolation.isOpen(row, col));

                percolation.open(row, col);

                runCount++;
            }

            pTh[i] = runCount / (double) (n * n);
        }

        mean = StdStats.mean(pTh);
        stddev = StdStats.stddev(pTh);
        double confidenceFraction = (1.96 * stddev()) / Math.sqrt(trials);
        confidenceLo = mean - confidenceFraction;
        confidenceHi = mean + confidenceFraction;

    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidenceLo;
    }

    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, t);
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
