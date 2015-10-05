package graphics;

import graphics.GameCanvas.State;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import networking.Client;

/**
 * Represents the main menu which the game opens up to. Used to start single player and multiplayer games
 * @author godfreya
 */
public class MainMenu extends Menu{
	private final int YSTART = 130; //how far down the buttons should appear on the menu
	public enum MenuState{MAIN, NEW, LOAD} //determines whether main menu is in default state, starting
	//new game or loading one
	private MenuState state;
	
	public MainMenu(GameCanvas cv){
		canvas = cv;
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
			canvas.showConfirmation(this, "Quit Game?");
			break;
		case "single":
			canvas.setState(State.PLAYING);
			//Client cS = new Client(1234, "localhost");//Connect to a server on the same machine as the client
			break;
		case "multi":
			setupMultiplayer();
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
	
	private void setupMultiplayer() {
		
		//Client cM = new Client(1234, "localhost");//Connect to the server. Change localhost to the actual host computer
		canvas.setState(State.PLAYING);
	}

	//confirm proposed action
	public void accept(){
		switch(action){
		case QUIT:
			System.exit(0);
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
}
