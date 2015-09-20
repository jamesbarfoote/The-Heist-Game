package game;

/**
 * Represents the money that is held in containers or placed on the floor/tables. Each Money object is given a value
 * as its currency value. This currency value is incremented to a players cash count when the pick up the object.
 * @author Lachlan
 *
 */
public class Money {

	private int amount;
	private Room location;
	private boolean pickedUp; //Whether or not this object is picked up or not.
	
	public Money(int amount, Room location){
		this.amount = amount;
		this.location = location;
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
	
	public void pickUpCash(Robber r){
		location = null;
		pickedUp = true;
	}
}
