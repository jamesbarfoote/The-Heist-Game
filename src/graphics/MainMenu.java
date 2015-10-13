package graphics;

import graphics.GameCanvas.State;
import networking.Client;
import networking.Server;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import game.Player;

//import networking.Client;

/**
 * Represents the main menu which the game opens up to. Used to start single player and multiplayer games
 * @author godfreya
 */
public class MainMenu extends Menu{
	private final int YSTART = 130; //how far down the buttons should appear on the menu
	public enum MenuState{MAIN, NEW, LOAD} //determines whether main menu is in default state, starting
	//new game or loading one
	private MenuState state;
	Player player;
	List<Player> players = new CopyOnWriteArrayList<Player>();
	Client cm;
	
	public MainMenu(GameCanvas cv, Player player, List<Player> players){
		canvas = cv;
		System.out.println("5 Player is at " + player.getLocation().x);
		this.player = player;
		this.players = players;
		menuBack = GameCanvas.loadImage("main_menu.png");
		state = MenuState.MAIN;
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = GameCanvas.logo.getHeight(null) + 90;
		loadButtons();
	}

	//add buttons to the menu
	private void loadButtons(){
		gameButtons = new ArrayList<GameButton>();
		if(state.equals(MenuState.MAIN)){
			gameButtons.add(new GameButton("new"));
			gameButtons.add(new GameButton("load"));
			gameButtons.add(new GameButton("options"));
			gameButtons.add(new GameButton("quit"));
		}
		else if(state.equals(MenuState.NEW) || state.equals(MenuState.LOAD)){
			gameButtons.add(new GameButton("single"));
			gameButtons.add(new GameButton("multi"));
			gameButtons.add(new GameButton("back"));
		}
		setButtonCoordinates();
	}
	
	public void keyPressed(KeyEvent e){
		
	}
	
	public void mouseReleased(MouseEvent e) {
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove();
			return;
		}
		switch(button){
		case "options":
		
			break;
		case "quit":
			action = Action.QUIT;
			canvas.showConfirmation(this, Action.QUIT, "Quit Game?");
			break;
		case "single":
			Server myRunnable = new Server(); 
			Thread myThread = new Thread(myRunnable);
			myThread.setDaemon(true); // important, otherwise JVM does not exit at end of main()
			myThread.start(); 

			
			canvas.initialize();
			
			//startClient();
			canvas.setState(State.PLAYING);
			System.out.println("reached");
			System.out.println("-----------------------------------------------");
			break;
		case "multi":
			action = Action.TEXT;
			canvas.showConfirmation(this, Action.IP,  "Enter IP Address");
			String host = "localhost";
			Lobby l = new Lobby(canvas, player, players, host);
			cm = l.getClient();
			canvas.initialize();
			break;
		case "new":
			state = MenuState.NEW;
			loadButtons();
			break;
		case "load":
			state = MenuState.LOAD;
			loadButtons();
			break;
		case "back":
			state = MenuState.MAIN;
			loadButtons();
			break;
		}
		canvas.simulateMouseMove();
	}
	
	private void startClient() {
		try {
			cm = new Client(43200, "127.0.0.1", player);
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
				player = p;
			}
		}

		System.out.println("Number of players = " + players.size());
		
	}

	private void setupMultiplayer() {
		canvas.setState(State.MULTI);
	}

	//confirm proposed action
	public void accept(){
		switch(action){
		case QUIT:
			System.exit(0);
		case TEXT:
			
			break;
		case IP:
			
			break;
		}
	}
	
	//retract proposed action
	public void decline(){
		action = null;
		canvas.removeConfirmation();
		canvas.simulateMouseMove();
	}
	
	protected void setButtonCoordinates(){
		int yDown = menuY + YSTART;
		for(GameButton gb: gameButtons){
			int x = menuX + (menuBack.getWidth(null)/2) - gb.getImage().getWidth(null)/2;
			gb.setCoordinates(x, yDown);
			yDown += 60;
		}
	}

	public void draw(Graphics g){
		g.drawImage(menuBack, menuX, menuY, null);
		for(GameButton gb: gameButtons){
			g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
		}
	}

	
	public Client getClient() {
		return cm;
	}
}
