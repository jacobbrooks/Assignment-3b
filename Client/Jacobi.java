import java.util.concurrent.RecursiveAction;

public class Jacobi extends RecursiveAction {
	static final double EPSILON = 0.001; // convergence criterion
	final JTree root;
	final int maxSteps;
	Region[][] A;
	Region[][] B;

	Jacobi(Region[][] A, Region[][] B, int firstRow, int lastRow, int firstCol, int lastCol, int maxSteps,
			int leafCells) {
		this.maxSteps = maxSteps;
		root = build(A, B, firstRow, lastRow, firstCol, lastCol, leafCells);
	}

	public void compute() {
		for (int i = 0; i < maxSteps; ++i) {
			root.invoke();
			root.reinitialize();
		}
	}

	static JTree build(Region[][] a, Region[][] b, int lr, int hr, int lc, int hc, int size) {
		if ((hr - lr + 1) * (hc - lc + 1) <= size)
			return new Leaf(a, b, lr, hr, lc, hc);
		int mr = (lr + hr) / 2; // midpoints
		int mc = (lc + hc) / 2;
		return new Interior(build(a, b, lr, mr, lc, mc, size), build(a, b, lr, mr, mc + 1, hc, size),
				build(a, b, mr + 1, hr, lc, mc, size), build(a, b, mr + 1, hr, mc + 1, hc, size));
	}
}
