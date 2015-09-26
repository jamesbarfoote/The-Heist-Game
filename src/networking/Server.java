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
	private ArrayList<String> players;
	private Date date;
	static OutputStream outputStream;
	static InputStream inputStream;
	
	public Server(int port) throws IOException
	{
		sSocket = new ServerSocket(port);//Set the port
		sSocket.setSoTimeout(100000);//Set how long to wait for a connection
		this.players = new ArrayList<String>();
		date  = new Date();
	}
	
	//create players here
	//when a client connects send out array
	//should generate 1-4 players
	
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
				
				try {
					players = (ArrayList<String>) ((ObjectInputStream) inputStream).readObject();
					System.out.println(players.get(1));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				PrintWriter out = new PrintWriter(serv.getOutputStream(), true);//Create a stream so that we can send information to the server
//				BufferedReader in = new BufferedReader(new InputStreamReader(serv.getInputStream()));//Create an input stream so that we can read any response from the server
//				//out.println("You have successfully connected");
//				String inputTxt;
//				
//						//ObjectInputStream objectInput = new ObjectInputStream(new GZIPInputStream(socketStream));
//						
//				
//				while((inputTxt = in.readLine()) != null)
//				{
//					System.out.println("Client said " + inputTxt);
//					//System.out.println("Client said: " + in.readLine());
//					//out.println("Message recieved by: " + serv.getLocalSocketAddress()); //Send back a response to the client
//					out.println("Client said: " + inputTxt);
//					if(inputTxt.equalsIgnoreCase("Exit"))
//					{
//						serv.close(); //Close the connection
//						break;
//					}
//				}
//				
				
				
				
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
