package graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import game.Player;
import graphics.GameCanvas.State;
import networking.Client;

/**
 * Represents the main menu which the game opens up to. Used to start single player and multiplayer games
 * @author godfreya
 */
public class MainMenu extends Menu{
	private final int YSTART = 125; //how far down the buttons should appear on the menu
	public enum MenuState{MAIN, NEW, LOAD} //determines whether main menu is in default state, starting
	//new game or loading one
	private MenuState state;
	
	public MainMenu(GameCanvas cv){
		canvas = cv;
		menuBack = GameCanvas.loadImage("main_menu.png");
		state = MenuState.MAIN;
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
		else if(state.equals(MenuState.NEW)){
			gameButtons.add(new GameButton("single"));
			gameButtons.add(new GameButton("multi"));
			gameButtons.add(new GameButton("back"));
		}
		setButtonCoordinates();
	}
	
	public Choice mouseReleased(MouseEvent e) {
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove();
			return Choice.VOID;
		}
		switch(button){
		case "options":
		
			return Choice.ACT;
		case "quit":
			action = Action.QUIT;
			canvas.showConfirmation(this, "Quit Game?");
			return Choice.ACT;
		case "single":
			canvas.setState(State.PLAYING);
			Client cS = new Client(1234, "localhost");//Connect to a server on the same machine as the client
			return Choice.ACT;
		case "multi":
			setupMultiplayer();
			return Choice.ACT;
		
		case "new":
			state = MenuState.NEW;
			loadButtons();
			canvas.simulateMouseMove();
			return Choice.ACT;
		case "load":
			
			return Choice.ACT;
		case "back":
			state = MenuState.MAIN;
			loadButtons();
			canvas.simulateMouseMove();
			return Choice.ACT;
		}
		return Choice.VOID;
	}
	
	private void setupMultiplayer() {
		
		Client cM = new Client(1234, "localhost");//Connect to the server. Change localhost to the actual host computer
		
		canvas.setState(State.PLAYING);
		
	}

	public void accept(){
		switch(action){
		case QUIT:
			System.exit(0);
		}
	}
	
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

	public void draw(Graphics g, int width, int height){
		menuX = (width/2) - (menuBack.getWidth(null)/2);
		menuY = height + 15;
		g.drawImage(menuBack, menuX, menuY, null);
		setButtonCoordinates();
		for(GameButton gb: gameButtons){
			g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
		}
	}
}
