package networking;

import java.net.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client {
	public static void main(String [] args)
	{
		String serverName = args[0];
		int port = Integer.parseInt(args[1]);
		try
		{
			System.out.println("Connecting to " + serverName + " on port " + port);

			Socket client = new Socket(serverName, port);//Connect to the specified computer
			System.out.println("Client connected to " + client.getRemoteSocketAddress());

			
				//Send message key press
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);//Create a stream so that we can send information to the server
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
				}
			}
			System.out.println("Input was null");
			
//			DataOutputStream out = new DataOutputStream(outToServer);
//				out.writeUTF("Hello I am " + client.getLocalSocketAddress()); //Write the data to the server
//
//				InputStream inFromServer = client.getInputStream();
//				DataInputStream in = new DataInputStream(inFromServer);
//				System.out.println("Server said " + in.readUTF());
			

			client.close(); //Close the TCP connection

		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	
}
