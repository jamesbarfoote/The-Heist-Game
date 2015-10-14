package game.items;

import java.awt.Point;
import java.io.Serializable;

import game.Room;

/**
 * Abstraction of the Movable items in the bank. This includes items that can be
 * picked up/used for solving puzzles
 * 
 * @author Lachlan Lee ID# 300281826, Cameron Porter 300279891
 *
 */
public abstract class InteractableItem implements Item, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Point position, oldPosition;

	// private Room room; // rooms hold items, an item shouldn't need to know
	// which room its in too? Commented out for now

	public InteractableItem(Point p) {
		this.position = p;
		this.oldPosition = p;
	}

	// public Room getRoom(){
	// return room;
	// }

	/**
	 * @return the location
	 */
	public Point getPosition() {
		return position;
	}

	public Point getOldPosition() {
		return oldPosition;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setPosition(Point location) {
		this.oldPosition = this.position;
		this.position = location;
	}

	public void setOldPosition(Point oldLocation) {
		this.oldPosition = oldLocation;
	}
}