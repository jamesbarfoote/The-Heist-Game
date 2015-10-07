package game.items;

import java.awt.Point;

import game.Door;
import game.Room;

public class Key extends InteractableItem{
	
	private Door opens;
	private Container containedIn;
	private Point position;
	
	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	public Key(Door opens, Container containedIn, Room room, Point position){
		super(room, position);
		this.opens = opens;
		this.containedIn = containedIn;
	}
	
	/**
	 * Called by the character class when it takes ownership of the key.
	 * Makes sure the game doesn't think the key is still in its container
	 * Prevents possible key duplication and/or game not thinking you have the key
	 */
	public void giveToPlayer(){
		containedIn = null;
	}
	
	public Door getDoor(){
		return opens;
	}
	
	public Container getContainedIn(){
		return containedIn;
	}

}
