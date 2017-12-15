public class PrintModule{

	public synchronized void printMatrix(double[][] d){
		for(int i = 0; i < d.length; i++){
			for(int j = 0; j < d[i].length; j++){
				System.out.print("|" + d[i][j] + "|");
			}
			System.out.println();
		}
		System.out.println();
	}

}
