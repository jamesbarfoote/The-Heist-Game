package graphics;

import graphics.Menu.Choice;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Represents the main menu which the game opens up to. Used to start single player and multiplayer games
 * @author godfreya
 */
public class MainMenu extends Menu{
	private final int YSTART = 100; //how far down the buttons should appear on the menu
	
	public MainMenu(GameCanvas gc){
		canvas = gc;
		menuBack = GameCanvas.loadImage("main_menu.png");
		gameButtons = new ArrayList<GameButton>();
		
		//add buttons to the menu
		gameButtons.add(new GameButton("single"));
		gameButtons.add(new GameButton("options"));
		gameButtons.add(new GameButton("quit"));
	}
	
	public Choice mouseReleased(MouseEvent e) {
		String button = onClick(e);
		if(button == null) {
			mouseMoved(e);
			return Choice.VOID;
		}
		switch(button){
		case "options":
		
			return Choice.ACT;
		case "quit":
			canvas.showDialogue();
			return Choice.ACT;
		case "single":
			
			return Choice.ACT;
		}
		return Choice.VOID;
	}

	public void draw(Graphics g, int width, int height){
		menuX = (width/2) - (menuBack.getWidth(null)/2);
		menuY = height;
		g.drawImage(menuBack, menuX, menuY, null);
		
		int yDown = menuY + YSTART;
		for(GameButton gb: gameButtons){
			int x = menuX + (menuBack.getWidth(null)/2) - gb.getImage().getWidth(null)/2;
			gb.setCoordinates(x, yDown);
			g.drawImage(gb.getImage(), x, yDown, null);
			yDown += 50;
		}
	}
}
