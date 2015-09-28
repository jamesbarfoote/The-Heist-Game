package networking;

import java.net.*;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

import control.Main;
import game.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client{
	//if player has moved then call an update
	//update will tell the server what has changed after it has got the most recent locations from the server 
	public static PrintWriter out;
	static OutputStream outputStream;
	static InputStream inputStream;
	private static ArrayList<String> players;
	private static int playerID;
	private int port;
	private String host;
	
	public Client(int port, String host)
	{
		this.port = port;
		this.host = host;
		players = new ArrayList<String>();
		clientSyncing();
	}
	
	
	//start by sending array
	//the main class should determine what player u are
	//then recieve an updated array of players
	//public static void main(String [] args)
	public void clientSyncing()
	{
		//String serverName = args[0];
		playerID = Main.getPlayer().getID(); //Get the id of the player that we are
		//int port = Integer.parseInt(args[1]);
		ArrayList<Player> temp = new ArrayList<Player>();
		temp.add(Main.getPlayer());
		String s = "new string";
		players.add(s);
		players.add("another string");
		try
		{
			System.out.println("Connecting to " + host + " on port " + port);

			Socket client = new Socket(host, port);//Connect to the specified computer
			System.out.println("Client connected to " + client.getRemoteSocketAddress());

			//Set date for the players before transmitting
			outputStream = new ObjectOutputStream(client.getOutputStream());
			inputStream = new ObjectInputStream(client.getInputStream());
			
			//send our player out
			 ((ObjectOutputStream) outputStream).writeObject(temp);
				//Send message key press
			//out = new PrintWriter(client.getOutputStream(), true);//Create a stream so that we can send information to the server
//			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));//Create an input stream so that we can read any response from the server
//			
//			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//			
//			String txtFromServer;
//			String txtFromClient;
//			
//			while(input != null )
//			{
//				
//				//System.out.println("You typed: " + input.readLine());
//				txtFromServer = in.readLine();
//				System.out.println("Server said: " + txtFromServer);
//				if(txtFromServer.equalsIgnoreCase("Exit"))
//				{
//					System.out.println("Exiting");
//					break;
//				}
//				
//				txtFromClient = input.readLine();
//				if(txtFromClient != null)
//				{
//					//GZIPOutputStream objectOutput = new GZIPOutputStream(new ObjectOutputStream(outputStream));
//					//((ObjectOutput) objectOutput).writeObject(players);
//					//out.println(txtFromClient);
//					//out.println(key);
//
//				}
			//}
			//System.out.println("Input was null");
			
			client.close(); //Close the TCP connection

		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	
}
