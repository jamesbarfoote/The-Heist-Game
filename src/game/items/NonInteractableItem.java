package game.items;

import game.Room;

import java.awt.Point;


/**
 * Abstraction of the Immovable objects in the bank. This encompasses all objects that cannot be interacted with
 * A.K.A. set dressing for the rooms.
 * @author Lachlan
 *
 */
public class NonInteractableItem implements Item{

	private Room room;
	private Point position;
	
	//Add enum for types of Non-interactive items
	
	public NonInteractableItem(Room room, Point point){
		this.room = room;
		this.position = position;
	}
	
	public Room getRoom(){
		return room;
	}
	
	public Point getPosition(){
		return position;
	}
}
