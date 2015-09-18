package graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an in game menu which opens when escape is pressed to give the player options
 * @author godfreya
 */
public class GameMenu{
	private List<GameButton> gameButtons;
	private Image menuBack; //menu image
	private final int YSTART = 200; //how far down the buttons should appear on the menu
	private int menuX; //start x position for drawing menu, helpful for drawing buttons on later
	private int menuY; //same for y position
	
	public GameMenu(){
		menuBack = GameCanvas.loadImage("menu.png");
		gameButtons = new ArrayList<GameButton>();
		
		//add buttons to the menu
		gameButtons.add(new GameButton("resume"));
		gameButtons.add(new GameButton("options"));
		gameButtons.add(new GameButton("quit"));
	}
	
	/**
	 * draw the menu centred on screen
	 * @param g
	 * @param width The width of the entire game screen
	 * @param height The height of the entire game screen
	 */
	public void draw(Graphics g, int width, int height){
		menuX = (width/2) - (menuBack.getWidth(null)/2);
		menuY = (height/2) - (menuBack.getHeight(null)/2); 
		g.drawImage(menuBack, menuX, menuY, null);
		
		int yDown = menuY + YSTART;
		for(GameButton gb: gameButtons){
			int x = menuX + (menuBack.getWidth(null)/2) - gb.getImage().getWidth(null)/2;
			gb.setCoordinates(x, yDown);
			g.drawImage(gb.getImage(), x, yDown, null);
			yDown += 50;
		}
	}
	
	/**
	 * mouse has moved over the menu, update menu accordingly
	 * @param e
	 */
	public void mouseMoved(MouseEvent e){
		for(GameButton gb: gameButtons){
			gb.select(false);
		}
		for(GameButton gb: gameButtons){
			if(gb.contains(e)){
				gb.select(true);
				return;
			}
		}
	}
	
	/**
	 * a mouse click action has occurred while the menu is up, we now must respond
	 * @param e
	 * @return
	 */
	public String onClick(MouseEvent e){
		for(GameButton gb: gameButtons){
			if(gb.contains(e) && gb.selected()) return gb.getName();
		}
		return null;
	}
	
	/**
	 * represents an option on the in game menu
	 * @author godfreya
	 */
	private class GameButton{
		private boolean selected = false; //whether button is selected or not
		private Image on;  //image for selected button
		private Image off; //image for deselected button
		private String name; //name of button
		private int startX; //start x coordinate for drawing
		private int startY; //start y coordinate for drawing
		
		public GameButton(String name){
			this.name = name;
			off = GameCanvas.loadImage(name + "_off.png");
			on = GameCanvas.loadImage(name + "_on.png");
		}
		
		public Image getImage(){
			return selected ? on: off;
		}
		
		public String getName(){
			return name;
		}
		
		public boolean selected(){
			return selected;
		}
		
		//set the coordinates for the buttons appearance on screen
		public void setCoordinates(int x, int y){
			startX = x;
			startY = y;
		}
		
		//set button select status
		public void select(boolean b){
			selected = b;
		}
		
		/**
		 * did the mouse event occur on this button?
		 * @param e
		 * @return
		 */
		public boolean contains(MouseEvent e){
			return e.getX() >= startX && e.getX() <= startX + getImage().getWidth(null) &&
					e.getY() >= startY && e.getY() <= startY + getImage().getHeight(null);
		}
	}

}
