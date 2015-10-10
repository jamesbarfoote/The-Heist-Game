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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client{
	//if player has moved then call an update
	//update will tell the server what has changed after it has got the most recent locations from the server 
	public static PrintWriter out;
	static ObjectOutputStream outputStream;
	static ObjectInputStream inputStream;
	public List<Player> players;
	public Player currentPlayer;
	private int port;
	private String host;
	private Socket client;
	private int ID;

	public Client(int port, String host, Player p)
	{
		this.port = port;
		this.host = host;
		this.currentPlayer = p;
		players = new CopyOnWriteArrayList<Player>();

		try {
			//client = new Socket(host, port);
			client = new Socket("127.0.0.1", 9002);
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
	@SuppressWarnings("unchecked")
	public void clientSyncing()
	{		
		try
		{

			List<Player> temp2;

			//System.out.println("in");
			try {

				temp2 = (List<Player>) inputStream.readObject();//get the arraylist for a single player
				System.out.println("Got initial array. size = " + temp2.size());
				ID = temp2.size();
				currentPlayer.setID(ID);
				players.add(currentPlayer);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			//send our player out
			List<Player> temp = new CopyOnWriteArrayList<Player>();
			temp.add(currentPlayer);
			outputStream.writeObject(temp);
			//System.out.println("Sent!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			//System.out.println("Sent players to server. Size = " + players.size());

			Thread.sleep(200);
			//List<Player> temp3 = new CopyOnWriteArrayList<Player>();

			//Recieve the players
			try {
				//inputStream.reset();
				temp2 = (List<Player>) inputStream.readObject();//get the arraylist for a single player
				//System.out.println("Got players from server. Size = " + temp3.size());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int i = 0;
//			for(Player p: temp3)
//			{
//				System.out.println("Player " + i + " has weapon " + p.getWeapon().getWeaponType());
//				i++;
//			}
			//			System.out.println("Recieved players");
			//			System.out.println("Client player size = " + players.size());


		}catch(IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void closeConnection()
	{
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //Close the TCP connection
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public void updatePlayer(Player p)
	{
		System.out.println("x = " + p.getLocation().x);

		this.currentPlayer = p;
	}

	public void update()
	{		
		//send our player out
		//	System.out.println("Updating The server with the new player info");
		List<Player> temp = new CopyOnWriteArrayList<Player>();
		temp.add(currentPlayer);

		//System.out.println("Current Player x: " + currentPlayer.getLocation().x + " Y: " + currentPlayer.getLocation().y);
		//currentPlayer.setLocation(new Point(10,0));
		try{
			int i = 0;
			
			//outputStream.flush();
			outputStream.writeObject(temp);//Send out our player
			//outputStream.reset();
			//System.out.println("Sent player");


			//Recieve the players
			try {
				
				players = (List<Player>) inputStream.readObject();//get the arraylist for a single player

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//			for(Player p: players)
//            {
//            	System.out.println("Player " + i + " has weapon " + p.getWeapon().getWeaponType() + " and is at " + p.getLocation().x);
//            	i++;
//            }
			//	System.out.println("Recieved players");
			//System.out.println("After Player x: " + currentPlayer.getLocation().x + " Y: " + currentPlayer.getLocation().y);

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		//		try {
		//			Thread.sleep(2000);
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	}


	public int getID() {
		return ID;
	}


	public void setPlayer(Player p) {
		currentPlayer = p;

	}
}
