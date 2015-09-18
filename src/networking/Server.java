package networking;

import java.net.*;
import java.io.*;

public class Server extends Thread{

	private ServerSocket sSocket;
	
	public Server(int port) throws IOException
	{
		sSocket = new ServerSocket(port);//Set the port
		sSocket.setSoTimeout(100000);//Set how long to wait for a connection
	}
	
	public void run()//Main thread
	{
		while(true)
		{
			try
			{
				System.out.println("Waiting for client on port " + sSocket.getLocalPort() + "...");
				Socket serv = sSocket.accept(); //Wait for a client to connect to us on this port
				System.out.println("Server connected to " + serv.getRemoteSocketAddress());
				
				DataInputStream in = new DataInputStream(serv.getInputStream());//Read the data from the connected client
				System.out.println("Client said " + in.readUTF());
				
				DataOutputStream out = new DataOutputStream(serv.getOutputStream()); //Create an output stream
				out.writeUTF("You connected to " + serv.getLocalSocketAddress()); //Send back a response to the client
				
				serv.close(); //Close the connection
				
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
