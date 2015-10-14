package game;

import game.items.InteractableItem;
import game.items.Key;

import java.awt.Point;

public class Door{


	private boolean locked;
	private Key key;
	private Point position;
	
	public Door(boolean locked, Point pos, Key k){
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
	
	public Key getKey(){
		return key;
	}
	
	public Point getPosition(){
		return position;
	}
}
