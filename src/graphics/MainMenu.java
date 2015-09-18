package graphics;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Represents the main menu which the game opens up to. Used to start single player and multiplayer games
 * @author godfreya
 */
public class MainMenu extends Menu{
	private final int YSTART = 100; //how far down the buttons should appear on the menu
	
	public MainMenu(){
		menuBack = GameCanvas.loadImage("main_menu.png");
		gameButtons = new ArrayList<GameButton>();
		
		//add buttons to the menu
		gameButtons.add(new GameButton("single"));
		gameButtons.add(new GameButton("options"));
		gameButtons.add(new GameButton("quit"));
	}
	
	public boolean mouseReleased(MouseEvent e) {
		String button = onClick(e);
		if(button == null) {
			mouseMoved(e);
			return false;
		}
		switch(button){
		case "options":
		
			return true;
		case "quit":
			if(JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Confirm Quit", 
					JOptionPane.CANCEL_OPTION) == JOptionPane.OK_OPTION) System.exit(0);
			return true;
		case "single":
			
			return true;
		}
		return false;
	}

	public void draw(Graphics g, int width, int height){
		menuX = (width/2) - (menuBack.getWidth(null)/2);
		menuY = height;//(int)(height*0.58) - (menuBack.getHeight(null)/2); 
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
