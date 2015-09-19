package graphics;

import java.awt.Image;
import java.awt.event.MouseEvent;

/**
 * represents an option on the menu
 * @author godfreya
 */
public class GameButton{
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