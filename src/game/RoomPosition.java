package game;

/**
 * Tuple for storing an x and y position in a given room.
 * @author Lachlan
 *
 */
public class RoomPosition {

	private int xPos;
	private int yPos;
	
	public RoomPosition(int x, int y){
		this.xPos = x;
		this.yPos = y;
	}
	
	/**
	 * Increment the x value by 1
	 */
	public void incX(){
		xPos += 1;
	}
	
	/**
	 * Decrement the x value by 1
	 */
	public void decX(){
		xPos -= 1;
	}
	
	/**
	 * Increment the y value by 1
	 */
	public void incY(){
		yPos += 1;
	}
	
	/**
	 * Decrement the y value by 1
	 */
	public void decY(){
		yPos -= 1;
	}
	
	public int getXPos(){
		return xPos;
	}
	
	public int getYPos(){
		return yPos;
	}
}
