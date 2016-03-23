package Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import Game.GameClient;
import UserInterface.GameUI;

public class GameServer implements Runnable {
	//Network attributes
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private PrintWriter out;
	private BufferedReader in;
	//Game attributes
	private GameClient localClient;
	private GameClient otherClient;
	private int turn;
	
	public static void main(String [] args) {
	}
	
	protected GameServer(InputStream is, OutputStream os){
		try {
			initialize(is, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initialize(InputStream is, OutputStream os) throws IOException {
		in = new BufferedReader(new InputStreamReader(is));
		out = new PrintWriter(os);
		//Wait for the two client to ready
		
		
	}

	@Override
	public void run() {
		
		
	}
	
	protected void setClient(GameClient client, boolean isLocal) {
		if(isLocal) {
			localClient = client;
		}
	}
}