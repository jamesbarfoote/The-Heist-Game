package graphics;

import java.awt.Image;
import java.awt.Point;

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
	
	public int getX(){
		return startX;
	}
	
	public int getY(){
		return startY;
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
	public boolean contains(Point p){
		return p.getX() >= startX && p.getX() <= startX + getImage().getWidth(null) &&
				p.getY() >= startY && p.getY() <= startY + getImage().getHeight(null);
	}
}