import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable{

	private final int ID;
	private final int CLIENTS;
		
	private ServerSocket ss;
	private Socket client;
	private DataInputStream input;
	private DataOutputStream output;

	private Control c;
	
	private int chunkHeight;
	private int chunkWidth;

	public ClientHandler(ServerSocket ss, int id, Control c, int clients){
		ID = id;		
		CLIENTS = clients;
		this.ss = ss;
		this.c = c;
		client = null;
		input = null;
		output = null;
		initStreamsAndSockets();
	}

	private void initStreamsAndSockets(){
		try{
			client = ss.accept();
			output = new DataOutputStream(client.getOutputStream());
			input = new DataInputStream(client.getInputStream());
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void closeStreamsAndSockets(){
		try {
           	output.close();
          	input.close();
       		client.close();
    	}catch(IOException e){
			e.printStackTrace();
		} 
	}

	private void sendChunkToClient(){
		double[][] d = c.getMyChunk(ID);
		chunkHeight = d.length;
		chunkWidth = d[0].length;
		try{
			output.writeInt(chunkHeight);
			output.writeInt(chunkWidth);
			if(ID == 0){
				output.writeInt(0);
				output.writeInt(chunkHeight - 2);
				output.writeUTF("top");
			}else if(ID == CLIENTS - 1){
				output.writeInt(1);
				output.writeInt(chunkHeight - 1);
				output.writeUTF("bottom");
			}else{
				output.writeInt(1);
				output.writeInt(chunkHeight - 2);
				output.writeUTF("middle");
			}
			output.flush();
			for(int i = 0; i < d.length; i++){
				for(int j = 0; j < d[i].length; j++){
					output.writeDouble(d[i][j]);
				}
			}
			output.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void sendEdgesToClient(){
		try{
			if(ID == 0 || ID == CLIENTS - 1){
				double[] edge = c.getMyEdge(ID, false);
				for(int i = 0; i < edge.length; i++){
					output.writeDouble(edge[i]);
				}
			}else{
				double[] topEdge = c.getMyEdge(ID, true);
				double[] bottomEdge = c.getMyEdge(ID, false);
				for(int i = 0; i < topEdge.length; i++){
					output.writeDouble(topEdge[i]);
				}
				for(int i = 0; i < bottomEdge.length; i++){
					output.writeDouble(bottomEdge[i]);
				}
			}
			output.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void receiveAndSetUpdatedChunk(){
		double[][] d = new double[chunkHeight][chunkWidth];
		try{
			for(int i = 0; i < chunkHeight; i++){
				for(int j = 0; j < chunkWidth; j++){
					d[i][j] = input.readDouble();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		c.setMyChunk(ID, d);
	}

	public void run(){
		try{
			int iterations = 10000;

			sendChunkToClient();
			receiveAndSetUpdatedChunk();

			output.writeInt(iterations);
			output.flush();

			while(iterations > 0){
				sendEdgesToClient();
				receiveAndSetUpdatedChunk();
				iterations--;
			}

			output.writeInt(-1);

			closeStreamsAndSockets();

		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
