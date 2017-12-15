public class Control{

	public Region[][] a;
	public Region[][] b;
	private double[][] initialTemperatures;
	private int height;
	private int width;
	
	private double c1;
	private double c2;
	private double c3;
	
	private int threshold;
	
	private Jacobi j;

	private String section;
	
	public Control(int height, int width, double c1, double c2, double c3, double s, double t, 
					int steps, double[][] initialTemperatures, int lo, int hi, String section) {
		this.height = height;
		this.width = width;
		this.section = section;
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		threshold = (height * width) / 4;
		this.initialTemperatures = new double[height][width]; 
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				this.initialTemperatures[i][j] = initialTemperatures[i][j];
			}
		}
		initMatrices(s,t);
		j = new Jacobi(a, b, lo, hi, 0, width - 1, steps, threshold);
	}

	public void setEdges(double[][] d){
		if(section.equals("top")){
			for(int i = 0; i < d[0].length; i++){
				a[a.length - 1][i].setTemperature(d[0][i]);
				b[b.length - 1][i].setTemperature(d[0][i]);
			}
		}else if(section.equals("bottom")){
			for(int i = 0; i < d[0].length; i++){
				a[0][i].setTemperature(d[0][i]);
				b[0][i].setTemperature(d[0][i]);
			}
		}else{
			for(int i = 0; i < d[0].length; i++){
				a[0][i].setTemperature(d[0][i]);
				b[0][i].setTemperature(d[0][i]);
				a[a.length - 1][i].setTemperature(d[1][i]);
				b[b.length - 1][i].setTemperature(d[1][i]);
			}
		}
	}


	private void initMatrices(double s, double t) {
		a = new Region[height][width];
		b = new Region[height][width];
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[i].length; j++) {
				a[i][j] = new Region(c1,c2,c3,s,t,initialTemperatures[i][j],i,j,height, width, section);
				b[i][j] = new Region(c1,c2,c3,s,t,initialTemperatures[i][j],i,j,height, width, section);
			}
		}
		
		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < a[i].length; j++) {
				if(i - 1 >= 0) {
					a[i][j].setNeighbor(0, a[i - 1][j]);
					b[i][j].setNeighbor(0, b[i - 1][j]);
				}
				if(i + 1 < height) {
					a[i][j].setNeighbor(1, a[i + 1][j]);
					b[i][j].setNeighbor(1, b[i + 1][j]);
				}
				if(j - 1 >= 0) {
					a[i][j].setNeighbor(2, a[i][j - 1]);
					b[i][j].setNeighbor(2, b[i][j - 1]);
				}
				if(j + 1 < height * 2) {
					a[i][j].setNeighbor(3, a[i][j + 1]);
					b[i][j].setNeighbor(3, b[i][j + 1]);
				}
				a[i][j].calculateNeighborCount();
				b[i][j].calculateNeighborCount();
			}
		}
		
		if(section.equals("top")){
			a[0][0].setTemperature(s);
			b[0][0].setTemperature(s);
		}else if(section.equals("bottom")){
			a[height - 1][width - 1].setTemperature(t);
			b[height - 1][width - 1].setTemperature(t);
		}
	}

	public void printAlloy(){
		for(int i = 0; i < a.length; i++){
			for(int k = 0; k < a[i].length; k++){
				System.out.print("|" + b[i][k].getTemperature() + "|");
			}
			System.out.println();
		}
	}

	public void go() {
		j.invoke();
	}
	
	
}
