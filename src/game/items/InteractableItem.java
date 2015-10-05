package game.items;

import java.awt.Point;

import game.Room;


/**
 * Abstraction of the Movable items in the bank. This includes items that can be picked up/used for solving puzzles
 * @author Lachlan
 *
 */
public abstract class InteractableItem implements Item{

	private Room room;
	
	public InteractableItem(Room room){
		this.room = room;
	}
	
	public Room getRoom(){
		return room;
	}
}
