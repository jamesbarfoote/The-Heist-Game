package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.Player;
import game.Room;
import networking.Client;

/**
 * Represents an in game menu which opens when escape is pressed to give the player options
 * @author godfreya, james.barfoote
 */
public class GameMenu extends Menu{
	private final int YSTART = 185; //how far down the buttons should appear on the menu
	private Player currentPlayer;
	private List<Player> players;
	private Client cm;
	private Room room;

	/**
	 * 
	 * @param GameCanvas
	 * @param currentPlayer
	 * @param players List
	 */
	public GameMenu(GameCanvas cv, Player currentPlayer, List<Player> players, Room room){
		canvas = cv;
		this.room = room;
		this.currentPlayer = currentPlayer;
		this.players = players;
		menuBack = GameCanvas.loadImage("menu.png");
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = (canvas.getHeight()/2) - (menuBack.getHeight(null)/2); 
		gameButtons = new ArrayList<GameButton>();

		//add buttons to the menu
		gameButtons.add(new GameButton("resume"));
		gameButtons.add(new GameButton("save"));
		gameButtons.add(new GameButton("options"));
		gameButtons.add(new GameButton("quit"));
		setButtonCoordinates();
	}

	//deal with mouse clicks
	public void mouseReleased(MouseEvent e) {
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove();
			return;
		}
		switch(button){
		case "save":
			action = Action.SAVE;
			canvas.showConfirmation(this, Action.SAVE, "Enter save name", null);
			break;
		case "options":

			break;
		case "quit":
			action = Action.QUIT;
			canvas.showConfirmation(this, Action.QUIT, "Exit to main menu?", null);
			break;
		case "resume":
			canvas.gameMenuSelect();

			break;
		}
	}

	public void keyPressed(KeyEvent e){

	}

	//confirm the current action
	public void accept(String data){
		switch(action){
		case QUIT:
			canvas.setState(GameCanvas.State.MENU);
			canvas.removeConfirmation();
			canvas.simulateMouseMove();
			break;
		case SAVE:

			action = null;
			canvas.removeConfirmation();
			canvas.simulateMouseMove();
			break;
		}
	}

	//block the proposed action
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
			yDown += 50;
		}
	}

	public void draw(Graphics g){
		g.drawImage(menuBack, menuX, menuY, null);
		for(GameButton gb: gameButtons){
			g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
		}
	}

	/**
	 * Creates and starts the client
	 */
	private void startClient() {
		try {
			cm = new Client(43200, "127.0.0.1", currentPlayer, this.room);
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
		this.room = cm.getRoom();
		System.out.println("Number of players = " + players.size());

	}

	@Override
	public Client getClient() {
		startClient();
		return cm;
	}
}
