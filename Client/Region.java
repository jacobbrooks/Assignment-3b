import java.util.Random;

public class Region {
	
	private double c[];
	private double p[];
	private double temperature;
	public Region[] neighbors;
	private int neighborCount;
	private int row;
	private int col;
	private double s;
	private double t;
	private int height;
	private int width;
	private String section; 
	
	public Region(double c1, double c2, double c3, double s, double t, double temperature, int row, int col, int height, int width, String section) {
		c = new double[3];
		p = new double[3];
		c[0] = c1;
		c[1] = c2;
		c[2] = c3;
		
		this.row = row;
		this.col = col;
		
		this.s = s;
		this.t = t;
		
		this.height = height;
		this.width = width;
		this.section = section;
		
		calculatePercentages();
		
		this.temperature = temperature;
		neighbors = new Region[4];
		
		for(int i = 0; i < 4; i++) {
			neighbors[i] = null;
		}
		
	}
	
	public void setNeighbor(int i, Region r) {
		neighbors[i] = r;
	}
	
	public double getS() {
		return s;
	}
	
	public double getT() {
		return t;
	}
	
	public double getTemperature() {
		return temperature;
	}
	
	public void setTemperature(double t) {
		temperature = t;
	}
	
	
	public double calculateTemperature(){
		
		if(section.equals("top")){
			if(row == 0 && col == 0) {
				return s;
			}
		}else if(section.equals("bottom")){
			if(row == height - 1 && col == width - 1) {
				return t;
			}
		}
		
		double temperature = 0;
		for(int i = 0; i < 3; i++) {
			double neighborTerm = 0;
			for(int j = 0; j < 4; j++) {
				if(neighbors[j] != null) {
					neighborTerm += neighbors[j].temperature * neighbors[j].p[i];
				}
			}
			temperature += (c[i] * neighborTerm) / (double) neighborCount;
		}
		
		return temperature;
		
	}
	
	public void calculateNeighborCount() {
		neighborCount = 4;
		for(int i = 0; i < 4; i++) {
			if(neighbors[i] == null) {
				neighborCount--;
			}
		}
	}

	private void calculatePercentages() {
		Random r = new Random();
		for(int i = 0; i < 3; i++){
		    p[i] = 1.0 / 3.0;
		}
		double min = 0.0;
		double max = 0.25 * (1.0 / 3.0);
		double value = min + (max - min) * r.nextDouble();
		double divisor1 = r.nextDouble();
		double divisor2 = 1.0 - divisor1;
		int addIndex = r.nextInt(3);
		p[addIndex] += value;
		if(addIndex == 0){
		    p[1] -= value * divisor1;
		    p[2] -= value * divisor2;
		}else if(addIndex == 1){
		    p[0] -= value * divisor1;
		    p[2] -= value * divisor2;
		}else{
		    p[0] -= value * divisor1;
		    p[1] -= value * divisor2;
		}
	}

}
