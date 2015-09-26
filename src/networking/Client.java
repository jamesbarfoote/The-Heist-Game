package networking;

import java.net.*;
import java.util.ArrayList;

import game.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client implements KeyListener{
	//if player has moved then call an update
	//update will tell the server what has changed after it has got the most recent locations from the server 
	public static PrintWriter out;
	ObjectOutputStream outputStream = new ObjectOutputStream();
	private ArrayList<Player> players;
	
	public static void main(String [] args)
	{
		String serverName = args[0];
		int port = Integer.parseInt(args[1]);
		try
		{
			System.out.println("Connecting to " + serverName + " on port " + port);

			Socket client = new Socket(serverName, port);//Connect to the specified computer
			System.out.println("Client connected to " + client.getRemoteSocketAddress());

			//Set date for the players before transmitting
				//Send message key press
			out = new PrintWriter(client.getOutputStream(), true);//Create a stream so that we can send information to the server
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));//Create an input stream so that we can read any response from the server
			
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			
			String txtFromServer;
			String txtFromClient;
			
			while(input != null )
			{
				
				//System.out.println("You typed: " + input.readLine());
				txtFromServer = in.readLine();
				System.out.println("Server said: " + txtFromServer);
				if(txtFromServer.equalsIgnoreCase("Exit"))
				{
					System.out.println("Exiting");
					break;
				}
				
				txtFromClient = input.readLine();
				if(txtFromClient != null)
				{
					out.println(txtFromClient);
					//out.println(key);

				}
			}
			System.out.println("Input was null");
			
			client.close(); //Close the TCP connection

		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println("Should transmit now");
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public String update()//Get the latest information from the server. Then send our current information
	{
		
		return null;
	}

	
}
