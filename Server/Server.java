import java.io.*;
import java.net.*;

public class Server{

	private final int CLIENTS = 2;
	private ServerSocket serverSocket;
	private Control c;
	public PrintModule p; //for debugging purposes
	
	public Server(){
		String[] command = {"ssh", "nuc19", "java -classpath Desktop/3b/Client/ ClientMain"};
		String[] command1 = {"ssh", "nuc18", "java -classpath Desktop/3b/Client/ ClientMain"};
		String[][] commands = {command, command1};
		
		p = new PrintModule(); //remember, just debugging
		c = new Control(CLIENTS, p);		

		try{
			serverSocket = new ServerSocket(6145);
		}catch(IOException e){
			e.printStackTrace();
		}

		serve(commands);
	}

	private void serve(String[][] commands){
		/*for(int i = 0; i < 2; i++){
			ssh(commands[i]);
			try{
				Thread.sleep(4000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}*/
		Thread[] t = new Thread[CLIENTS];
		for(int i = 0; i < CLIENTS; i++){
			t[i] = new Thread(new ClientHandler(serverSocket, i, c, p, CLIENTS));
			t[i].start();
		}
		try{
			t[0].join();
			t[1].join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		c.printAlloy();
	}

	private void ssh(String[] command){
		Process p;
		try{
			p = Runtime.getRuntime().exec(command);		
		}catch(IOException e){
			e.printStackTrace();
		}
	}	


}

	
