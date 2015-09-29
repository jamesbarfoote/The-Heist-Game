package networking;

import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import game.Player;

import java.awt.Point;
import java.io.*;

public class Server extends Thread{
	//array of characters with their room

	private ServerSocket sSocket;
	private ArrayList<Player> players;
	static OutputStream outputStream;
	static InputStream inputStream;

	public Server(int port) throws IOException
	{
		sSocket = new ServerSocket(port);//Set the port
		sSocket.setSoTimeout(100000);//Set how long to wait for a connection
	}

	//create players here
	//when a client connects send out array
	//should generate 1-4 players11

	public void run()//Main thread
	{
		while(true)
		{
			try
			{
				System.out.println("Waiting for client on port " + sSocket.getLocalPort() + "...");
				Socket serv = sSocket.accept(); //Wait for a client to connect to us on this port
				System.out.println("Server connected to " + serv.getRemoteSocketAddress());

				outputStream = new ObjectOutputStream(serv.getOutputStream());
				inputStream = new ObjectInputStream(serv.getInputStream());
				ArrayList<Player> temp = new ArrayList<Player>();
				System.out.println("Created temp aray");
				players = new ArrayList<Player>();
				players.add(createPlayer(2));
				//Send out the whole arraylist to the client
				((ObjectOutputStream) outputStream).writeObject(players);
				System.out.println("Sent out players");
				//
				//				
				//				try {
				//					Thread.sleep(2000);
				//				} catch (InterruptedException e) {
				//					// TODO Auto-generated catch block
				//					e.printStackTrace();
				//				}

				//				while(true)
				//				{

				try {

					temp = (ArrayList<Player>) ((ObjectInputStream) inputStream).readObject();//get the arraylist for a single player
					System.out.println("Got player");
					System.out.println("ID = " + temp.get(0).getID());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//
				if(temp != null && players != null){
					if(players.contains(temp.get(0)))//If the player is already in then list then remove it and replace it
					{
						players.remove(temp.get(0));
						players.add(temp.get(0));
					}
					else
					{
						players.add(temp.get(0));
					}
				}

				//
				//				//Send out the whole arraylist to the client
				//				((ObjectOutputStream) outputStream).writeObject(players);

				//Add pause in here
			}
			//}
			catch(SocketTimeoutException s)
			{
				System.out.println("Socket timed out!");
				break;
			}catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
		}


	}

	private Player createPlayer(int id) {
		Point p = new Point();
		p.setLocation(0, 0);
		game.items.Weapon w = new game.items.Weapon("gun", true);
		game.Player.Type t = game.Player.Type.robber;
		Player currentPlayer = new Player(w, id, p, t);
		return currentPlayer;
	}

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]); //Get our port number from the command line
		try
		{
			Thread t = new Server(port);
			t.start();//Start the thread and wait for connection
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
