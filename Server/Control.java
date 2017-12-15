public class Control{

	public final int HEIGHT = 16;
	public final int WIDTH = HEIGHT * 2;
	private final double S = 100.0;
	private final double T = 100.0;
	private final int CLIENTS;
	public volatile double[][] alloy;
	private PrintModule p;

	public Control(int clients, PrintModule p){
		CLIENTS = clients;
		alloy = new double[HEIGHT][WIDTH];
		initAlloy();
		this.p = p;
	}

	public void printAlloy(){
		p.printMatrix(alloy);
	}

	private void initAlloy(){
		for(int i = 0; i < HEIGHT; i++){
			for(int j = 0; j < WIDTH; j++){
				alloy[i][j] = 0.0;
			}
		}
		alloy[0][0] = S;
		alloy[HEIGHT - 1][WIDTH - 1] = T;
	}

	public double[][] getMyChunk(int id){
		int chunkSize = HEIGHT / CLIENTS;
		int offset = chunkSize * id;
		double[][] retChunk;		
		if(id == 0 || id == CLIENTS - 1){
			retChunk = new double[chunkSize + 1][WIDTH];
		}else{
			retChunk = new double[chunkSize + 2][WIDTH];
		}
		for(int i = offset - 1; i <= offset + chunkSize; i++){
			if(id == 0 && i == offset - 1){
				i = offset;
			}else if(id == CLIENTS - 1 && i == offset + chunkSize){
				break;
			}
			for(int j = 0; j < WIDTH; j++){
				if(id == 0){
					retChunk[i][j] = alloy[i][j];
				}else{
					retChunk[i - (offset - 1)][j] = alloy[i][j];
				}
			}
		}
		return retChunk;
	}

	public void setMyChunk(int id, double[][] chunk){
		int chunkSize = HEIGHT / CLIENTS;
		int offset = chunkSize * id;		
		for(int i = offset - 1; i <= offset + chunkSize; i++){
			if(id == 0 && i == offset - 1){
				i = offset;
			}else if(id == CLIENTS - 1 && i == offset + chunkSize){
				break;
			}
			for(int j = 0; j < WIDTH; j++){
				if(id == 0){
					alloy[i][j] = chunk[i][j];
				}else{
					alloy[i][j] = chunk[i - (offset - 1)][j];
				}
			}
		}
	}

	public double[] getMyEdge(int id, boolean top){
		double[] edge = new double[WIDTH];
		int edgeIndex = 0;
		if(id == 0){
			edgeIndex = HEIGHT / CLIENTS;
		}else if(id == CLIENTS - 1){
			edgeIndex = ((HEIGHT / CLIENTS) * id) - 1;
		}else if(top){
			edgeIndex =((HEIGHT / CLIENTS) * id) - 1;
		}else{
			edgeIndex = (HEIGHT / CLIENTS) * (1 + id);
		}
		for(int i = 0; i < WIDTH; i++){
			edge[i] = alloy[edgeIndex][i];
		}
		return edge;
	}

}
