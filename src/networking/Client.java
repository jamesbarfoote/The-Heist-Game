package networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import game.Player;

/**
 * A simple Swing-based client for the chat server.  Graphically
 * it is a frame with a text field for entering messages and a
 * textarea to see the whole dialog.
 *
 * The client follows the Chat Protocol which is as follows.
 * When the server sends "SUBMITNAME" the client replies with the
 * desired screen name.  The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are
 * already in use.  When the server sends a line beginning
 * with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all
 * chatters connected to the server.  When the server sends a
 * line beginning with "MESSAGE " then all characters following
 * this string should be displayed in its message area.
 */
public class Client {

	static DataOutputStream outputStream;
	static DataInputStream inputStream;
	public List<Player> players;
	public Player currentPlayer;
	private int port;
	private String host;
	private Socket client;
	private int ID;

	public Client(int port, String host, Player p) throws IOException, InterruptedException
	{
		this.port = port;
		this.host = host;
		this.currentPlayer = p;
		players = new CopyOnWriteArrayList<Player>();

		try {
			//client = new Socket(host, port);
			client = new Socket("10.140.115.164", 43200);
			outputStream = new DataOutputStream(client.getOutputStream());
			inputStream = new DataInputStream(client.getInputStream());

			System.out.println(client.getLocalSocketAddress());
			System.out.println(client.getRemoteSocketAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Connect to the specified computer


		// Make connection and initialize streams
		String serverAddress = getServerAddress();
		System.out.println("Get serv add");

//		byte[] a = toBytes(map);
//		Object o = toObject(a); //works fine    
//		//daten senden
//		out.write(a);
//		out.flush();
		
		
		 
	
		
		
		//Main stuff
		List<Player> temp2;

		System.out.println("in");
		int size = inputStream.readInt();			 
		byte[] bytes = new byte[size];			 
		inputStream.readFully(bytes);
		Object plays = toObject(bytes);
		temp2 = (List<Player>) plays;

		//temp2 = (List<Player>) inputStream.readObject();//get the arraylist for a single player
		System.out.println("Got initial array. size = " + temp2.size());
		ID = temp2.size();
		currentPlayer.setID(ID);
		players.add(currentPlayer);

		run();

	}

	/**
	 * Prompt for and return the address of the server.
	 */
	private String getServerAddress() {
		return "localhost";
	}

	public int getID()
	{
		return ID;
	}

	/**
	 * return the desired player.
	 */
	public Player getPlayer() {
		return currentPlayer;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	/**
	 * Connects to the server then enters the processing loop.
	 * @throws InterruptedException 
	 */
	public void run() throws IOException, InterruptedException {
		//System.out.println("Running");


		// Process all messages from server, according to the protocol.
		//	while (true) {
		//System.out.println("Processing");


		//send our player out
		List<Player> temp = new CopyOnWriteArrayList<Player>();
		temp.add(currentPlayer);
		byte[] bytes = toBytes(temp);
		outputStream.writeInt(bytes.length);
		outputStream.write(bytes);
		outputStream.flush();
		
		//outputStream.writeObject(temp);
//		System.out.println("Sent!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		System.out.println("Sent players to server. Size = " + players.size());

//		Thread.sleep(200);

		//Get players array
		int size = inputStream.readInt();			 
		byte[] bytes2 = new byte[size];			 
		inputStream.readFully(bytes2);
		Object plays = toObject(bytes2);
		players = (List<Player>) plays;

		//Thread.sleep(2000);

	}
	//	}

	public void setPlayer(Player player2) {
		this.currentPlayer = player2;

	}
	
	public static byte[] toBytes(Object object){
	    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	    try{
	        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
	        oos.writeObject(object);
	    }catch(java.io.IOException ioe){
	        ioe.printStackTrace();
	    }
	     
	    return baos.toByteArray();
	} 
	 
	public static Object toObject(byte[] bytes){
	    Object object = null;
	    try{
	        object = new java.io.ObjectInputStream(new
	        java.io.ByteArrayInputStream(bytes)).readObject();
	    }catch(java.io.IOException ioe){
	        ioe.printStackTrace();
	    }catch(java.lang.ClassNotFoundException cnfe){
	        cnfe.printStackTrace();
	    }
	    return object;
	}

	/**
	 * Runs the client as an application with a closeable frame.
	 */
	//	public static void main(String[] args) throws Exception {
	//		Client client = new Client();
	//		client.run();
	//	}
}