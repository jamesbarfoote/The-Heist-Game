package graphics;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Represents an in game menu which opens when escape is pressed to give the player options
 * @author godfreya
 */
public class GameMenu extends Menu{
	private final int YSTART = 200; //how far down the buttons should appear on the menu
	
	public GameMenu(GameCanvas cv){
		canvas = cv;
		menuBack = GameCanvas.loadImage("menu.png");
		gameButtons = new ArrayList<GameButton>();
		
		//add buttons to the menu
		gameButtons.add(new GameButton("resume"));
		gameButtons.add(new GameButton("save"));
		gameButtons.add(new GameButton("options"));
		gameButtons.add(new GameButton("quit"));
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
			
			break;
		case "options":
		
			break;
		case "quit":
			action = Action.QUIT;
			canvas.showConfirmation(this, "Quit to main menu?");
				
			break;
		case "resume":
			canvas.gameMenuSelect();
			
			break;
		}
	}
	
	//confirm the current action
	public void accept(){
		switch(action){
		case QUIT:
			canvas.setState(GameCanvas.State.MENU);
			canvas.removeConfirmation();
			canvas.simulateMouseMove();
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
	
	public void draw(Graphics g, int width, int height){
		menuX = (width/2) - (menuBack.getWidth(null)/2);
		menuY = (height/2) - (menuBack.getHeight(null)/2); 
		g.drawImage(menuBack, menuX, menuY, null);
		setButtonCoordinates();
		for(GameButton gb: gameButtons){
			g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
		}
	}
}
