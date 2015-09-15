package Networking;

import java.net.*;
import java.io.*;

public class client {
	public static void main(String [] args)
	{
		String serverName = args[0];
		int port = Integer.parseInt(args[1]);
		try
		{
			System.out.println("Connecting to " + serverName + " on port " + port);
			
			Socket client = new Socket(serverName, port);//Connect to the specified computer
			System.out.println("Connected to " + client.getRemoteSocketAddress());
			
			OutputStream outToServer = client.getOutputStream(); //Create a stream so that we can send information to the server
			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF("Hello I am " + client.getLocalSocketAddress()); //Write the data to the server
			
			InputStream inFromServer = client.getInputStream();//Create an input stream so that we can read any response from the server
			DataInputStream in = new DataInputStream(inFromServer);
			System.out.println("Server said " + in.readUTF());
			
			client.close(); //Close the TCP connection

		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
