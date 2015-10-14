package game.items;

import java.awt.Point;

import game.Door;
import game.Room;

/**
 * An item for unlocking doors. A key should unlock a particular door
 * Not currently in use as key specific doors are not yet implemented
 * @author Lachlan
 *
 */
public class Key extends InteractableItem{
	
	private String filename = "";	//TODO add the filename when asset is made.

	public Key(){
		super(null);
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
