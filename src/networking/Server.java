package networking;


import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

import game.Player;
import game.items.Weapon;

/**
 * 
 */
public class Server {

	/**
	 * The port that the server listens on.
	 */
	private static final int PORT = 9002;

	/**
	 * The set of all players 
	 */
	private static CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<Player>();

	/**
	 * The set of all the output streams(clients).  This
	 * set is kept so we can easily send out the updated players.
	 */
	private static HashSet<ObjectOutputStream> writers = new HashSet<ObjectOutputStream>();

	/**
	 * listens on a port and spawns handler threads.
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Server is running.");
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
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
		private ObjectInputStream in;
		private ObjectOutputStream out;

		/**
		 * Constructs a handler thread,
		 * */
		public Handler(Socket socket) {
			this.socket = socket;
			            Player player2 = new Player(new Weapon("Laser", true), 2, new Point(8,1), game.Player.Type.robber);
			            players.add(player2);
		}


		public void run() {
			try {

				// Create character streams for the socket.
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				ArrayList<Player> temp = new ArrayList<Player>();

				// Request a name from this client.  Keep requesting until
				// a name is submitted that is not already used.  Note that
				// checking for the existence of a name and adding the name
				// must be done while locking the set of names.
				while (true) {
					//Send out the whole arraylist to the client
					out.writeObject(players);

					//Get player
					temp = (ArrayList<Player>) in.readObject();//get the arraylist for a single player
					System.out.println("Got player from client. Weapon = " + temp.get(0).getWeapon().getWeaponType());

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
				for(Player p: players)
				{
					System.out.println("Player " + i + " has weapon " + p.getWeapon().getWeaponType());
					i++;
				}
				CopyOnWriteArrayList<Player> temp3 = new CopyOnWriteArrayList<Player>();
				temp3 = players;
				out.reset();
				out.writeObject(temp3);
				writers.add(out);

				// Accept messages from this client and broadcast them.
				// Ignore other clients that cannot be broadcasted to.
				while (true) {
					ArrayList<Player> temp2 = new ArrayList<Player>();
					//InputStream inputStream2 = new ObjectInputStream(socket.getInputStream());
					//in.reset();
					temp2 = (ArrayList<Player>) in.readObject();//get the arraylist for a single player
					System.out.println(temp2.get(0).getLocation().x);

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

					for(Player p: players)
					{
						System.out.println("Player " + i + " has weapon " + p.getWeapon().getWeaponType() + " and is at " + p.getLocation().x);
						i++;
					}

					out.reset();
					for (ObjectOutputStream writer : writers) {//Send out the revised array list to all players
						writer.writeObject(players);
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// This client is going down!  Remove its name and its print
				// writer from the sets, and close its socket.
				if (player != null) {
					players.remove(player);
				}
				if (out != null) {
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}