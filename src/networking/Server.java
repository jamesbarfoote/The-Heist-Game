package networking;

import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import game.Player;

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


				//Send out the whole arraylist to the client
				((ObjectOutputStream) outputStream).writeObject(players);

				while(true)
				{

					try {

						temp = (ArrayList<Player>) ((ObjectInputStream) inputStream).readObject();//get the arraylist for a single player
						System.out.println(players.get(1));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if(players.contains(temp.get(0)))//If the player is already in then list then remove it and replace it
					{
						players.remove(temp.get(0));
						players.add(temp.get(0));
					}
					else
					{
						players.add(temp.get(0));
					}


					//Send out the whole arraylist to the client
					((ObjectOutputStream) outputStream).writeObject(players);
					
					//Add pause in here
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
