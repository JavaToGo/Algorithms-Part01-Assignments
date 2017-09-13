import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] markOpen;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull;
    private int numberOfOpenSites;
    private final int gridSize;
    private final int vTopSite;
    private final int vBottomSite;

    public Percolation(int n) {

        if (n <= 0)
            throw new IllegalArgumentException("Initialize with a value greater than 0!");

        // initialize variables.
        gridSize = n;
        markOpen = new boolean[n][n];
        uf = new WeightedQuickUnionUF(n * n + 2); // total array size + virtual sites.
        ufFull = new WeightedQuickUnionUF(n * n + 1); // array sites + top site only
        vTopSite = 0;
        vBottomSite = n * n + 1;

        // initialize arrays from 1 to n*n.
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                markOpen[i - 1][j - 1] = false; // parallel array to trace open sites.
            }
        }
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public void open(int row, int col) {
        checkLimits(row, col);

        if (!isOpen(row, col)) {
            markOpen[row - 1][col - 1] = true;
            numberOfOpenSites++;

            // connect top row to virtual top
            if (row == 1) {
                uf.union(getArrayValue(row, col), vTopSite);
                ufFull.union(getArrayValue(row, col), vTopSite);
            }

            // connect bottom row to virtual bottom
            else if (row == gridSize) {
                uf.union(vBottomSite, getArrayValue(row, col));
            }

            // connect with surrounding open sites
            connectWithOpenSitesAround(getArrayValue(row, col), row, col - 1);
            connectWithOpenSitesAround(getArrayValue(row, col), row, col + 1);
            connectWithOpenSitesAround(getArrayValue(row, col), row - 1, col);
            connectWithOpenSitesAround(getArrayValue(row, col), row + 1, col);

        } 
    }

    private void connectWithOpenSitesAround(int a, int i, int j) {
        try {
            if (isOpen(i, j)) {
                uf.union(a, getArrayValue(i, j));
                ufFull.union(a, getArrayValue(i, j));
            }
        } 
        catch (IllegalArgumentException e) {
            // no action is required.
            // this is to catch boundary errors.
        }
    }

    public boolean isOpen(int row, int col) {
        checkLimits(row, col);
        return markOpen[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        checkLimits(row, col);
        // check if the site has a connection to top row [ i=0 ].
        if (isOpen(row, col))
            return ufFull.connected(vTopSite, getArrayValue(row, col));
        return false;
    }

    public boolean percolates() {
        return uf.connected(vTopSite, vBottomSite);
    }

    private int getArrayValue(int p, int q) {
        return (gridSize * (p - 1)) + q;
    }

    private void checkLimits(int row, int col) {
        if (row <= 0 || col <= 0 || row > gridSize || col > gridSize) {
            throw new IllegalArgumentException("Value should be between 1 and " + gridSize + ".");
        }
    }

    public static void main(String[] args) {
        // This method is for testing
        // and can be ignored.
    }

}
