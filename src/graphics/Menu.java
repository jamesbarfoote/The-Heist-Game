package graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class Menu {
	protected List<GameButton> gameButtons;
	protected Image menuBack; //menu image
	protected int menuX; //start x position for drawing menu, helpful for drawing buttons on later
	protected int menuY; //same for y position
	
	/**
	 * draw the menu centred on screen
	 * @param g
	 * @param width The width of the entire game screen
	 * @param height The height of the entire game screen
	 */
	public abstract void draw(Graphics g, int width, int height);
	
	/**
	 * for dealing with mouse clicks on the menu. returns true if button correctly selected
	 */
	public abstract boolean mouseReleased(MouseEvent e);
	
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
	 * represents an option on the menu
	 * @author godfreya
	 */
	protected class GameButton{
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
