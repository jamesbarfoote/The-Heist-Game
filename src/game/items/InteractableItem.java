package game.items;

import java.awt.Point;

import game.Room;


/**
 * Abstraction of the Movable items in the bank. This includes items that can be picked up/used for solving puzzles
 * @author Lachlan
 *
 */
public abstract class InteractableItem implements Item{
	
	Point position;
	private Room room;

	public InteractableItem(Room room, Point p){
		this.room = room;
		this.position = p;
	}
	
	public Room getRoom(){
		return room;
	}
	
	/**
	 * @return the location
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @param location the location to set
	 */
	public void setPosition(Point location) {
		this.position = location;
	}
}