package game.items;

import java.awt.Point;
import java.io.Serializable;

import game.Door;
import game.Room;
/**
 * An item for unlocking doors. A key should unlock a particular door
 * Not currently in use as key specific doors are not yet implemented
 * @author Lachlan Lee ID# 300281826
 *
 */
public class Key extends InteractableItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Door opens;
	private Container containedIn;
	private Point position;

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
