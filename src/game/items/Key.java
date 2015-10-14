package game.items;

import java.awt.Point;
import java.io.Serializable;

import game.Door;
import game.Room;

public class Key extends InteractableItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Door opens;
	private Container containedIn;
	private Point position;
	private String filename = "";	//TODO add the filename when asset is made.
	private String direction = "N";
	private Point oldPosition;
	
	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	public Key(Door opens, Container containedIn, Room room, Point position){
		super(position);
		this.opens = opens;
		this.containedIn = containedIn;
	}
	
	/**
	 * Called by the character class when it takes ownership of the key.
	 * Makes sure the game doesn't think the key is still in its container
	 * Prevents possible key duplication and/or game not thinking you have the key
	 */
	public void giveToPlayer(){
		containedIn = null;
	}
	
	public Door getDoor(){
		return opens;
	}
	
	public Container getContainedIn(){
		return containedIn;
	}

	@Override
	public String getFilename() {
		return this.filename;
	}

	@Override
	public String getDirection() {
		return this.direction;
	}

	@Override
	public double[] getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOldPosition(Point oldLocation) {
		this.oldPosition = oldLocation;
	}
	
	/**
	 * @param position the position to set
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

}
