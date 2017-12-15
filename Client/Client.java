import java.io.*;
import java.net.*;

class Client{

	private Socket serverConnection;
	private DataInputStream input;
	private DataOutputStream output;
	private Control c;
	private double[][] myChunk;

	private int lo;
	private int hi;

	private String section;

	public Client(){
		serverConnection = null;
		input = null;
		output = null;
		initStreamsAndSockets();
		myChunk = pullChunk();
		c = new Control(myChunk.length, myChunk[0].length, 0.75, 1.0, 1.25, 100.0, 100.0, 1, myChunk, lo, hi, section);
	}

	private double[][] pullChunk(){
		int height = 0;
		int width = 0;	
		section = "";	
		try{
			height = input.readInt();
			width = input.readInt();
			lo = input.readInt();
			hi = input.readInt();	
			section = input.readUTF();
		}catch(IOException e){
			e.printStackTrace();
		}
		double[][] d = new double[height][width];
		for(int i = 0; i < d.length; i++){
			for(int j = 0; j < d[i].length; j++){
				try{
					d[i][j] = input.readDouble();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return d;
	}

	private double[][] pullEdges(){
		double[][] edges = new double[2][myChunk.length];
		try{
			for(int i = 0; i < myChunk.length; i++){
				edges[0][i] = input.readDouble();
			}		
			if(section.equals("middle")){
				for(int i = 0; i < myChunk.length; i++){
					edges[1][i] = input.readDouble();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return edges;
	}

	private void pushChunk(){
		for(int i = 0; i < c.b.length; i++){
			for(int j = 0; j < c.b[i].length; j++){
				try{
					output.writeDouble(c.b[i][j].getTemperature());
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		try{
			output.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void go(){
		c.go();
		pushChunk();
		double[][] d = pullEdges();
		c.setEdges(d);
		c.go();	
		//c.printAlloy();
		pushChunk();
		d = pullEdges();
		c.setEdges(d);
		c.go();	
		//c.printAlloy();
	}

	private void initStreamsAndSockets(){
		try{
			serverConnection = new Socket("altair.cs.oswego.edu", 6145);
			output = new DataOutputStream(serverConnection.getOutputStream());
			input = new DataInputStream(serverConnection.getInputStream());
		}catch(IOException e){
			System.out.println(e);
		}
	}

	private void closeStreamsAndSockets(){
		try {
           	output.close();
          	input.close();
       		serverConnection.close();
    	}catch(IOException e){
			e.printStackTrace();
		} 
	}

}
