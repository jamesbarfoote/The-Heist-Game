package game.items;

import java.awt.Point;

import game.Door;
import game.Room;

public class Key extends InteractableItem{
	
	private Container containedIn;
	private String filename = "";	//TODO add the filename when asset is made.

	public Key(){
		super(null);
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
	
	public Container getContainedIn(){
		return containedIn;
	}

	@Override
	public String getFilename() {
		return this.filename;
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

	@Override
	public String getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

}
