package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import game.Room;
import game.Player;

/**
 * A simple client that connects to a server. Sends and receives updated player information
 * @author james.barfoote
 */
public class Client {

	static DataOutputStream outputStream;
	static DataInputStream inputStream;
	public List<Player> players;
	public Player currentPlayer;
	public Room room;
	private Socket client;
	private int ID;

	/**
	 * Sets up and starts a connection to the server
	 * @param port the server is listening to
	 * @param host Address of the server
	 * @param currentPlayer
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unchecked")
	public Client(int port, String host, Player currentPlayer, Room r) throws IOException, InterruptedException
	{
		System.out.println("Client started");
		this.room = r;
		this.currentPlayer = currentPlayer;
		players = new CopyOnWriteArrayList<Player>(); //This type is used to avoid concurrent modifications

		try {
			System.out.println("Host = " + host + " Port = " + port);
			client = new Socket(host, port);//Create a new connection to the server
			outputStream = new DataOutputStream(client.getOutputStream());
			inputStream = new DataInputStream(client.getInputStream());
		} catch (UnknownHostException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}

		//Main stuff. Sending and receiving
		Room temp2;
		int size = inputStream.readInt();	//Get the size of the array that follows		 
		byte[] bytes = new byte[size];		//Create a new array of the correct size	 
		inputStream.readFully(bytes);		//Dont stop reading till the array is full (this way we dont lose any data)
		Object room = toObject(bytes);		//Convert the byte array to a Room
		temp2 = (Room) room;

		//Set the current players ID based on how big the list of players is
		if(room != null){
			this.ID = (temp2.getPlayers().size()) -5;

			this.currentPlayer.setID(ID);
			players.add(currentPlayer);
			this.room.setPlayers(players);
			this.room.setCurrentPlayer(currentPlayer);
		}
		else
		{
			this.ID = 0;
			this.currentPlayer.setID(ID);
			players.add(currentPlayer);
			this.room.setPlayers(players);
			this.room.setCurrentPlayer(currentPlayer);
		}

		//Run the main thread that deals with the constant sending and receiving between server and client
		run();

	}



	/**
	 * Connects to the server then enters the processing loop.
	 * @throws InterruptedException 
	 */
	public void run() throws IOException, InterruptedException {

		//send our room out
		this.room.setCurrentPlayer(this.currentPlayer);
		for(int i = 0; i < players.size(); i++) //Update the current player in the players list
		{
			if(players.get(i).getID() == currentPlayer.getID())
			{
				
				players.set(i, currentPlayer);
			}
		}
		this.room.setPlayers(players);
		
		byte[] bytes = toBytes(this.room);
		outputStream.writeInt(bytes.length);//Send the length of the array
		outputStream.write(bytes);
		outputStream.flush(); //Clear the stream

		//Get players array
		int size = inputStream.readInt();			 
		byte[] bytes2 = new byte[size];			 
		inputStream.readFully(bytes2);
		Object room = toObject(bytes2);
		room = (Room) room;
		this.players = this.room.getPlayers();

	}

	/**
	 * Sets the current player
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.currentPlayer = player;

	}

	/**
	 * Converts a list of players to an array of bytes
	 * @param List<Player>
	 * @return Array of bytes
	 */
	public static byte[] toBytes(Room object){
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		try{
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
			oos.writeObject(object);
		}catch(java.io.IOException ioe){
			ioe.printStackTrace();
		}

		return baos.toByteArray();
	} 

	/**
	 * Converts a byte array back to a list of players
	 * @param bytes
	 * @return List<Player>
	 */
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
	 * 
	 * @return Current Players ID
	 */
	public int getID()
	{
		return ID;
	}

	/**
	 * @return the current player.
	 */
	public Player getPlayer() {
		return currentPlayer;
	}

	/**
	 * 
	 * @return List of all players
	 */
	public List<Player> getPlayers()
	{
		return players;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
}
