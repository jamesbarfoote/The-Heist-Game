package game;

import game.items.InteractableItem;
import game.items.Key;

import java.awt.Point;

/**
 * Representation of a Door that the player can pass through into another room. Doors can be locked to prevent
 * the player passing through them. 
 * The door can be unlocked by any key
 * @author Lachlan
 *
 */
public class Door{


	private boolean locked; //Whether the door is locked or not
	private Point position; //The position in the worldpsace
	
	public Door(boolean locked, Point pos){
		this.position = pos;
		this.locked = locked;
	}
	
	/**
	 * Unlocks the door if they player has provided the required key
	 * Should only be called by methods handling door unlocking
	 */
	public boolean unlockDoor(){
		locked = false;
		return locked;
	}
	
//	/**
//	 * Checks whether a given key is the correct key to unlock the door
//	 * @param k
//	 * @return
//	 */
//	public boolean isCorrectKey(Key k){
//		if (k == key){ return true; }
//		else{ return false; }
//	}
	
	public boolean isLocked(){
		return locked;
	}
	
	public Point getPosition(){
		return position;
	}
}
