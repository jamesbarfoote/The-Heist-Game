package graphics;

import graphics.GameCanvas.State;
import networking.Client;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import game.Player;
import game.Room;

/**
 * Is the screen where each player can see the other players that are currently connected
 * @author Godfreya, james.barfoote
 */
public class Lobby extends Menu{
	private final int YSTART = 70; //how far up the buttons should appear on the menu
	private Player currentPlayer;
	private List<Player> players = new CopyOnWriteArrayList<Player>();
	private Client cm;
	private String host = "localhost";
	private Room room;

	/**
	 * 
	 * @param cv The main drawing area
	 * @param player The current player
	 * @param players The list of all players in the game
	 * @param host The server address
	 */
	public Lobby(GameCanvas cv, Player player, List<Player> players, String host, Room room){
		canvas = cv;
		this.currentPlayer = player;
		this.players = players;
		this.host = host;
		this.room = room;
		System.out.println("Lobby height = " + room.getHeight());
		menuBack = GameCanvas.loadImage("lobby.png");
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = (canvas.getHeight()/2) - (menuBack.getHeight(null)/2); 
		gameButtons = new ArrayList<GameButton>();
		gameButtons.add(new GameButton("back"));
		gameButtons.add(new GameButton("start"));
		setButtonCoordinates();
		startClient();
	}

	public void keyPressed(KeyEvent e){

	}

	public void decline(){

	}

	public void accept(String data){

	}

	public void mouseReleased(MouseEvent e){
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove();
			return;
		}
		switch(button){
		case "back":
			canvas.setState(State.MENU);
			break;
		case "start":
			canvas.setClient(cm);
			canvas.setCurrentPlayer(currentPlayer);
			canvas.setState(State.PLAYING_MULTI);
			break;
		}
	}

	/**
	 * Creates a new client and connects to the server.
	 * Updates the current player and list of players
	 */
	public void startClient()
	{
		try {
			System.out.println("Lobby player = " + currentPlayer.getName());
			System.out.println("Lobby room = " + this.room.getPlayers().get(0).getID());
			cm = new Client(43200, host, currentPlayer, this.room);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Connect to the server. Change localhost to the actual host computer

		players = cm.getPlayers();
		for(Player p: players)
		{
			if(p.getID() == cm.getID())
			{
				currentPlayer = p;
			}
		}

	}


	/**
	 * Returns the client for use in other classes
	 */
	public Client getClient()
	{
		return cm;
	}

	protected void setButtonCoordinates(){
		int yDown = menuY + menuBack.getHeight(null) - YSTART;
		int x = menuX + menuBack.getWidth(null)/4;
		GameButton current = gameButtons.get(0);
		current.setCoordinates(x, yDown);
		current = gameButtons.get(1);
		x = menuX + menuBack.getWidth(null)*3/5 + 10;
		current.setCoordinates(x, yDown);
	}

	public void draw(Graphics g){	
		g.drawImage(menuBack, menuX, menuY, null);
		for(GameButton gb: gameButtons){
			g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
		}

		int nameX = menuX + 100;
		int nameY = menuY + 100;
		
		try {
			cm.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		players = cm.getPlayers();
		for(Player p: players){
			g.drawString(p.getName(), nameX, nameY);
			nameY += 50;
		}
	}
}
