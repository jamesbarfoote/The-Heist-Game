package networking;

import game.Player;
import game.items.Weapon;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client{
	//if player has moved then call an update
	//update will tell the server what has changed after it has got the most recent locations from the server 
	public static PrintWriter out;
	static OutputStream outputStream;
	static InputStream inputStream;
	public ArrayList<Player> players;
	public Player currentPlayer;
	private int port;
	private String host;
	private Socket client;

	public Client(int port, String host, Player p)
	{
		this.port = port;
		this.host = host;
		this.currentPlayer = p;
		players = new ArrayList<Player>();

		try {
			client = new Socket(host, port);
			outputStream = new ObjectOutputStream(client.getOutputStream());
			inputStream = new ObjectInputStream(client.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Connect to the specified computer

		clientSyncing();
	}


	//start by sending array
	//the main class should determine what player u are
	//then recieve an updated array of players
	//public static void main(String [] args)
	public void clientSyncing()
	{		
		try
		{
			System.out.println("Connecting to " + host + " on port " + port);


			System.out.println("Client connected to " + client.getRemoteSocketAddress());
			System.out.println("Done");

			ArrayList<Player> temp2;
			int currentNumPlayers = 0;
			System.out.println("About to get array");

			System.out.println("in");
			try {

				temp2 = (ArrayList<Player>) ((ObjectInputStream) inputStream).readObject();//get the arraylist for a single player
				System.out.println("Got players");
				currentNumPlayers = temp2.size();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("2");
			
			//send our player out
			System.out.println("Main part");
			ArrayList<Player> temp = new ArrayList<Player>();
			temp.add(currentPlayer);
			((ObjectOutputStream) outputStream).writeObject(temp);


			//Recieve the players
			try {

				players = (ArrayList<Player>) ((ObjectInputStream) inputStream).readObject();//get the arraylist for a single player

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Recieved players");

			//Pause
			//			try {
			//				Thread.sleep(2000);
			//			} catch (InterruptedException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}
			//}


			client.close(); //Close the TCP connection

		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	
	public void updatePlayer(Player p)
	{
		this.currentPlayer = p;
	}

	public void update()
	{
		//send our player out
		System.out.println("Main part");
		ArrayList<Player> temp = new ArrayList<Player>();
		temp.add(currentPlayer);
		try{
			((ObjectOutputStream) outputStream).writeObject(temp);


			//Recieve the players
			try {

				players = (ArrayList<Player>) ((ObjectInputStream) inputStream).readObject();//get the arraylist for a single player

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Recieved players");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
