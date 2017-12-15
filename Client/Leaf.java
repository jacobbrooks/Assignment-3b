public class Leaf extends JTree {

	private final Region[][] A;
	private final Region[][] B;
	private final int loRow;	
	private final int hiRow;
	private final int loCol;
	private final int hiCol;
	private int steps = 0;

	Leaf(Region[][] A, Region[][] B, int loRow, int hiRow, int loCol, int hiCol) {
		this.A = A;
		this.B = B;
		this.loRow = loRow;
		this.hiRow = hiRow;
		this.loCol = loCol;
		this.hiCol = hiCol;
	}

	public void compute() {

		boolean AtoB = (steps++ % 2) == 0;
		Region[][] a = (AtoB) ? A : B;
		Region[][] b = (AtoB) ? B : A;
		double md = 0.0;

		for (int i = loRow; i <= hiRow; ++i) {
			for (int j = loCol; j <= hiCol; ++j) {
				double temperature = a[i][j].calculateTemperature();
				b[i][j].setTemperature(temperature);
				md = Math.max(md, Math.abs(b[i][j].getTemperature() - a[i][j].getTemperature()));
			}
		}

		maxDiff = md;
		
	}

}
