package game.items;

import java.awt.Point;

import game.Room;


/**
 * Abstraction of the Movable items in the bank. This includes items that can be picked up/used for solving puzzles
 * @author Lachlan
 *
 */
public abstract class InteractableItem implements Item{
	
	Point position, oldPosition;
	private Room room;

	public InteractableItem(Room room, Point p){
		this.room = room;
		this.position = p;
		this.oldPosition = p;
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
	
	public Point getOldPosition(){
		return oldPosition;
	}

	/**
	 * @param location the location to set
	 */
	public void setPosition(Point location) {
		this.oldPosition = this.position;
		this.position = location;
	}
	
	public void setOldPosition(Point oldLocation){
		this.oldPosition = oldLocation;
	}
}