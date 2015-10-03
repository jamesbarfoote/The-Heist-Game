package graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class Dialogue{
	protected List<GameButton> gameButtons;
	protected Image menuBack; //menu image
	protected int menuX; //start x position for drawing menu, helpful for drawing buttons on later
	protected int menuY; //same for y position
	protected GameCanvas canvas;
	
	/**
	 * draw the menu centred on screen
	 * @param g
	 * @param width The width of the entire game screen
	 * @param height The height of the entire game screen
	 */
	public abstract void draw(Graphics g);
	
	/**
	 * for dealing with mouse clicks on the menu. returns true if button correctly selected
	 */
	public abstract void mouseReleased(MouseEvent e);
	
	/**
	 * mouse has moved over the menu, update menu accordingly
	 * @param e
	 */
	public void mouseMoved(Point p){
		for(GameButton gb: gameButtons){
			gb.select(false);
		}
		for(GameButton gb: gameButtons){
			if(gb.contains(p)){
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
		Point p = new Point(e.getX(), e.getY());
		for(GameButton gb: gameButtons){
			if(gb.contains(p) && gb.selected()) return gb.getName();
		}
		return null;
	}
}
