package game;

import game.items.Key;

import java.awt.Point;

public class Door {

	private Room room1;
	private Room room2;
	private Point room1Entry; //The Room 1 tile the door is on
	private Point room2Entry; //The Room 2 tile the door is on
	private boolean locked;
	private Key key;
	
	public Door(Room room1, Room room2, Point room1Entry, Point room2Entry, boolean locked, Key k){
		this.room1 = room1;
		this.room2 = room2;
		this.room1Entry = room1Entry;
		this.room2Entry = room2Entry;
		this.locked = locked;
	}
	
	/**
	 * Unlocks the door if they player has provided the required key
	 * Should only be called by methods handling door unlocking
	 */
	public boolean unlockDoor(Key k){
		if(isCorrectKey(k)){
			locked = false;
		}
		return locked;
	}
	
	/**
	 * Checks whether a given key is the correct key to unlock the door
	 * @param k
	 * @return
	 */
	public boolean isCorrectKey(Key k){
		if (k == key){ return true; }
		else{ return false; }
	}

	public Room getRoom1() {
		return room1;
	}

	public Room getRoom2() {
		return room2;
	}

	public Point getRoom1Entry() {
		return room1Entry;
	}

	public Point getRoom2Entry() {
		return room2Entry;
	}
	
	public boolean isLocked(){
		return locked;
	}
	
	public Key getKey(){
		return key;
	}
}
