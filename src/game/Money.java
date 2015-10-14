package game;

import java.awt.Point;

import game.items.InteractableItem;

/**
 * Represents the money that is held in containers or placed on the floor/tables. Each Money object is given a value
 * as its currency value. This currency value is incremented to a players cash count when the pick up the object.
 * @author Lachlan Lee ID# 300281826
 *
 */
public class Money extends InteractableItem {

	private int amount;
	private Room location;
	private Point position; //Position in room, if it is not in a container, null if it is
	private boolean pickedUp; //Whether or not this object is picked up or not.
	String filename = "_obj_cashStack.png";
	String direction = "N";
	double[] size = {2, 3};
	
	public Money(int amount, Point position){
		super(position);
		this.amount = amount;
		this.position = position;
		pickedUp = false;
	}
	
	public int getAmount(){
		return amount;
	}
	
	public Room getLocation(){
		return location;
	}
	
	public boolean getPickedUp(){
		return pickedUp;
	}
	
	public Point getPosition(){
		return position;
	}
	
	public void setPosition(Point p){
		this.position = p;
	}
	
	/**
	 * Sets the money as picked up by the player
	 */
	public void pickUpCash(){
		location = null;
		pickedUp = true;
	}

	@Override
	public String getFilename() {
		return this.filename;
	}

	@Override
	public String getDirection() {
		return this.direction;
	}

	@Override
	public double[] getSize() {
		return this.size;
	}
}
