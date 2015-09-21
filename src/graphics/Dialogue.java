package graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class Dialogue{
	protected List<GameButton> gameButtons;
	protected Image menuBack; //menu image
	protected int menuX; //start x position for drawing menu, helpful for drawing buttons on later
	protected int menuY; //same for y position
	protected GameCanvas canvas;
 	
	//possible outcomes of a mouse click action. 
	//yes == action confirmed
	//no == action declined
	//void == no button was clicked
	//act == mouse click performed a setup action, no response yet
	public enum Choice{YES, NO, VOID, ACT}
	
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
	public abstract Choice mouseReleased(MouseEvent e);
	
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
}
