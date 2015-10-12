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
 * 
 */
public class Server {

	/**
	 * The port that the server listens on.
	 */
	private static final int PORT = 43200;

	/**
	 * The set of all players 
	 */
	private static CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<Player>();

	/**
	 * The set of all the output streams(clients).  This
	 * set is kept so we can easily send out the updated players.
	 */
	private static HashSet<DataOutputStream> writers = new HashSet<DataOutputStream>();

	/**
	 * listens on a port and spawns handler threads.
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
	 * A handler thread class.  Handlers are spawned from the listening
	 * loop and are responsible for a dealing with a single client
	 * and broadcasting its messages.
	 */
	private static class Handler extends Thread {
		private Player player;
		private Socket socket;
		private DataInputStream in;
		private DataOutputStream out;

		/**
		 * Constructs a handler thread,
		 * */
		public Handler(Socket socket) {
			System.out.println("New handler created");
			this.socket = socket;
			System.out.println(socket.getLocalSocketAddress());
			System.out.println(socket.getRemoteSocketAddress());

			//Player player2 = new Player(new Weapon("Laser", true), 2, new Point(8,2), game.Player.Type.robber);
			//players.add(player2);
		}


		public void run() {
			try {

				// Create character streams for the socket.
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				List<Player> temp = new CopyOnWriteArrayList<Player>();

				// Request a name from this client.  Keep requesting until
				// a name is submitted that is not already used.  Note that
				// checking for the existence of a name and adding the name
				// must be done while locking the set of names.
				while (true) {
					//Send out the whole arraylist to the client
					byte[] bytes = toBytes(players);
					out.writeInt(bytes.length);
					out.write(bytes);
					out.flush();
					
					//out.writeUnshared(players);

					//Get player
					int size2 = in.readInt();			 
					byte[] bytes2 = new byte[size2];			 
					in.readFully(bytes2);
					Object plays = toObject(bytes2);
					temp = (List<Player>) plays;
					
					//temp = (List<Player>) in.readObject();//get the arraylist for a single player
					//System.out.println("Got player from client. Weapon = " + temp.get(0).getWeapon().getWeaponType());

					synchronized (players) {
						if (players.contains(temp.get(0))) {
							players.remove(temp.get(0));
							players.add(temp.get(0));
							player = temp.get(0);
							break;
						}
						else
						{
							players.add(temp.get(0));
							player = temp.get(0);
							break;
						}
					}
				}

				// Now that a successful name has been chosen, add the
				// socket's print writer to the set of all writers so
				// this client can receive broadcast messages.
				int i = 0;
								
				List<Player> temp3 = new CopyOnWriteArrayList<Player>();
				temp3 = players;
				//out.reset();
				byte[] bytes3 = toBytes(temp3);
				out.writeInt(bytes3.length);
				out.write(bytes3);
				out.flush();
				//out.writeUnshared(temp3);
				writers.add(out);

				// Accept messages from this client and broadcast them.
				// Ignore other clients that cannot be broadcasted to.
				while (true) {
					
					for(Player p: players)
					{
						System.out.println("Player " + " is at " + p.getLocation().x);
						//i++;
					}
					List<Player> temp2 = new CopyOnWriteArrayList<Player>();
					//InputStream inputStream2 = new ObjectInputStream(socket.getInputStream());
					//in.reset();
					
					int size2 = in.readInt();			 
					byte[] bytes2 = new byte[size2];			 
					in.readFully(bytes2);
					Object plays = toObject(bytes2);
					temp2 = (List<Player>) plays;
					
					//temp2 = (List<Player>) in.readObject();//get the arraylist for a single player
					//System.out.println(temp2.get(0).getLocation().x);

					//Find player is the list of player and add them
					//Player playerToRemove = null;
					synchronized (players) {
						for(Player p: players)
						{
							if(p.getID() == temp2.get(0).getID())
							{
								//playerToRemove = p;
								players.remove(p);//Remove player
								players.add(temp2.get(0));//Replace player
							}
						}
					}
					//players.remove(playerToRemove);

					
						//out.reset();
					for (DataOutputStream writer : writers) {//Send out the revised array list to all players
						byte[] bytes4 = toBytes(players);
						out.writeInt(bytes4.length);
						out.write(bytes4);
						out.flush();
						//writer.writeUnshared(players);
						
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				// This client is going down!  Remove its name and its print
				// writer from the sets, and close its socket.
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
		
		
	}
}

