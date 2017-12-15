import java.io.*;
import java.net.*;

public class Server implements Runnable{

	private final int CLIENTS;
	private ServerSocket serverSocket;
	public Control c;
	
	public Server(int clients, int height){
		CLIENTS = clients;

		c = new Control(CLIENTS, height);		

		try{
			serverSocket = new ServerSocket(6145);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void serve(String[][] commands){
		//ssh into machines
		for(int i = 0; i < CLIENTS; i++){
			ssh(commands[i]);
			try{
				Thread.sleep(5000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}

		//start then join client handlers
		Thread[] t = new Thread[CLIENTS];
		for(int i = 0; i < CLIENTS; i++){
			t[i] = new Thread(new ClientHandler(serverSocket, i, c, CLIENTS));
			t[i].start();
		}
		try{
			for(int i = 0; i < CLIENTS; i++){
				t[i].join();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	private void ssh(String[] command){
		Process p;
		try{
			p = Runtime.getRuntime().exec(command);		
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void run(){
		String[] command = {"ssh", "nuc19", "java -classpath Desktop/3b/Client/ ClientMain"};
		String[] command1 = {"ssh", "nuc18", "java -classpath Desktop/3b/Client/ ClientMain"};
		String[] command2 = {"ssh", "nuc17", "java -classpath Desktop/3b/Client/ ClientMain"};
		String[] command3 = {"ssh", "nuc16", "java -classpath Desktop/3b/Client/ ClientMain"};
		String[][] commands = {command, command1, command2, command3};
		serve(commands);
	}	


}

	
