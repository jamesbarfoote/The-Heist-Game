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
	private ArrayList<Player> players = new ArrayList<Player>();;
	static OutputStream outputStream;
	static InputStream inputStream;
	ArrayList<Socket> connections = new ArrayList<Socket>();

	public Server(int port) throws IOException
	{
		sSocket = new ServerSocket(1234);//Set the port
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
				for(Socket s : connections)
				{
					if(s.isClosed())
					{
						connections.remove(s);
					}
				}

				System.out.println("Waiting for client on port " + sSocket.getLocalPort() + "...");
				Socket serv = sSocket.accept(); //Wait for a client to connect to us on this port
				System.out.println("Server connected to " + serv.getRemoteSocketAddress());

				connections.add(serv);

				outputStream = new ObjectOutputStream(serv.getOutputStream());
				inputStream = new ObjectInputStream(serv.getInputStream());
				ArrayList<Player> temp = new ArrayList<Player>();
			//	System.out.println("Created temp aray");

				//Send out the whole arraylist to the client
				((ObjectOutputStream) outputStream).writeObject(players);
				//System.out.println("Sent out players");

				try {

					temp = (ArrayList<Player>) ((ObjectInputStream) inputStream).readObject();//get the arraylist for a single player
//					System.out.println("Got player");
//					System.out.println("ID = " + temp.get(0).getWeapon().getWeaponType());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//
				//				if(temp != null && players != null){
				//					if(players.contains(temp.get(0)))//If the player is already in then list then remove it and replace it
				//					{
				//						players.remove(temp.get(0));
				//						players.add(temp.get(0));
				//					}
				//					else
				//					{
				//						players.add(temp.get(0));
				//					}
				//				}
				//				else if(temp != null && players == null)
				//				{
				//System.out.println("Temp size = " + temp.size());
				players.add(temp.get(0));
				for(Player p: players)
				{
					temp.add(p);
				}
				//}

				//
				//Send out the whole arraylist to the client
			//	System.out.println("About to send players");
				((ObjectOutputStream) outputStream).writeObject(temp);
//				System.out.println("Sent players");
//				System.out.println("Size = " + players.size());
//
//				System.out.println("LOOPED");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateInfo();

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


	private void updateInfo()  {

		while(true){
			ArrayList<Player> temp = new ArrayList<Player>();
			Player playerToRemove = null;
			try{
				//Recieve
				try {

					temp = (ArrayList<Player>) ((ObjectInputStream) inputStream).readObject();//get the arraylist for a single player
				//	System.out.println("Got player");
					//System.out.println("ID = " + temp.get(0).getWeapon().getWeaponType());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(temp != null){

					for(Player p: players)//Find and remove the player before updating it in the players array
					{
						if(p.getID() != temp.get(0).getID())
						{
							temp.add(p);
						}
						else
						{
							playerToRemove = p;
						}
					}
					//players.add(temp.get(0));
					//				for(Player p: players)
					//				{
					//					if(!temp.contains(p))
					//					{
					//						temp.add(p);
					//					}
					//				}


					//Send out the whole arraylist to the client
					((ObjectOutputStream) outputStream).writeObject(temp);
					//System.out.println("Sent out players " + players.size());
					if(playerToRemove != null) 
					{
						players.remove(playerToRemove);
					//	System.out.println("Player removed!");
					}
					players.add(temp.get(0));
				//	System.out.println("Player updated");
					
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}
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
