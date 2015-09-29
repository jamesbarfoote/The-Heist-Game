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

	public Client(int port, String host)
	{
		this.port = port;
		this.host = host;
		players = new ArrayList<Player>();
		clientSyncing();
	}


	private void createPlayer(int id) {
		Point p = new Point();
		p.setLocation(0, 0);
		Weapon w = new Weapon("gun", true);
		game.Player.Type t = game.Player.Type.robber;
		this.currentPlayer = new Player(w, 3, p, t);
		

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

			Socket client = new Socket(host, port);//Connect to the specified computer
			System.out.println("Client connected to " + client.getRemoteSocketAddress());
			System.out.println("Done");
			//Set date for the players before transmitting
			outputStream = new ObjectOutputStream(client.getOutputStream());
//			System.out.println("Out");
			
			ArrayList<Player> temp2;
			int currentNumPlayers = 0;
			System.out.println("About to get array");
			inputStream = new ObjectInputStream(client.getInputStream());
			System.out.println("in");
			try {

				temp2 = (ArrayList<Player>) ((ObjectInputStream) inputStream).readObject();//get the arraylist for a single player
				System.out.println("Got players");
				currentNumPlayers = temp2.size();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("2");
			createPlayer(currentNumPlayers);
//
//			boolean loop = true;
			//while(loop)
			//{
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

//			if(players.size() == 0)
//			{
//				loop = false;
//			}

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
}
