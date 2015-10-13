package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import game.Player;

/**
 * The server for the game. Stores a list of all the players. Hands sending and receving them
 */
public class Server implements Runnable {

	/**
	 * The port that the server listens on.
	 */
	private static final int PORT = 43200;

	/**
	 * The set of all players 
	 */
	private static List<Player> players = new CopyOnWriteArrayList<Player>();

	/**
	 * The set of all the output streams(clients).  This
	 * set is kept so we can easily send out the updated players list.
	 */
	private static HashSet<DataOutputStream> writers = new HashSet<DataOutputStream>();


	/**
	 * listens on a port and creates handler threads.
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Server is running.");

		InetAddress ip = InetAddress.getLocalHost();
		System.out.println("Current IP address : " + ip.getHostAddress());

		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			System.out.println("Listener closed");
			listener.close();
		}
	}

	/**
	 * A handler thread class.  Handlers are created from the listening
	 * loop and are responsible for a dealing with a single client
	 * and broadcasting its player.
	 */
	private static class Handler extends Thread {
		private Player player;
		private Socket socket;
		private DataInputStream in;
		private DataOutputStream out;

		/**
		 * Constructs a handler thread
		 * @param The socket connected to a single client
		 * */
		public Handler(Socket socket) {
			this.socket = socket;
			System.out.println(socket.getLocalSocketAddress());
			System.out.println(socket.getRemoteSocketAddress());
		}

		/**
		* The main method for the server
		*/
		@SuppressWarnings("unchecked")
		public void run() {
			try {

				// Create the streams for the socket.
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				List<Player> temp = new CopyOnWriteArrayList<Player>();

				//Main loop
				while (true) {
					//Send out the whole arraylist to the client
					byte[] bytes = toBytes(players);
					out.writeInt(bytes.length);
					out.write(bytes);
					out.flush();

					//Get player
					int size2 = in.readInt();			 
					byte[] bytes2 = new byte[size2];			 
					in.readFully(bytes2);
					Object plays = toObject(bytes2);
					temp = (List<Player>) plays;

					//Only alter the list of players when another thread isn't
					synchronized (players) {
						//If the player is already in the list of players then remove it and add the new one
						//this avoid having duplicated players
						if (players.contains(temp.get(0))) {
							players.remove(temp.get(0));
							players.add(temp.get(0));
							player = temp.get(0);
							break;
						}
						else
						{
							//Add the player straight to the list as its isn't in the list yet
							players.add(temp.get(0));
							player = temp.get(0);
							break;
						}
					}
				}

				List<Player> temp3 = new CopyOnWriteArrayList<Player>();
				temp3 = players;
				byte[] bytes3 = toBytes(temp3);
				out.writeInt(bytes3.length);
				out.write(bytes3);
				out.flush();
				writers.add(out);

				// Accept messages from this client and broadcast them.
				// Ignore other clients that cannot be broadcasted to.
				while (true) {

					List<Player> temp2 = new CopyOnWriteArrayList<Player>();
					int size2 = in.readInt();			 
					byte[] bytes2 = new byte[size2];			 
					in.readFully(bytes2);
					Object plays = toObject(bytes2);
					temp2 = (List<Player>) plays;
					
					synchronized (players) {//Make sure no other thread is currently trying to modify players
						for(Player p: players)
						{
							if(p.getID() == temp2.get(0).getID())
							{
								players.remove(p);//Remove player
								players.add(temp2.get(0));//Replace player
							}
						}
					}

					for (@SuppressWarnings("unused") DataOutputStream writer : writers) {//Send out the revised list to all players
						byte[] bytes4 = toBytes(players);
						out.writeInt(bytes4.length);
						out.write(bytes4);
						out.flush();

					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				// Client is closing so remove it from the writer set and close the socket
				if (player != null) {
					players.remove(player);
					System.out.println("PLayer removed");
				}
				if (out != null) {
					writers.remove(out);
					System.out.println("Writer removed");
				}
				try {
					socket.close();
					System.out.println("Socket closed");
				} catch (IOException e) {
				}
			}
		}


		/**
		 * Converts the list of players to a byte array
		 * @param object. List of all players
		 * @return array of bytes
		 */
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

		/**
		 * Converts an array of bytes to a list of players
		 * @param bytes
		 * @return Object
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


	}

	@Override
	public void run() {
		try {
			main(null);
		} catch (Exception e) {
			System.out.println("Couldn't run the server");
			e.printStackTrace();
		}
		
	}

	
}

