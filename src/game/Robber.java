package game;

/**
 * A representation of a players Character and its associated abilities. Keeps track of a given players money count, as
 * well as their current weapon.
 * @author Lachlan
 *
 */
public class Robber extends Character{
	
	private int player; //The Player controlling this Robber
	private int numDollars; //Current amount of Money the player has picked up
	
	public Robber(int player, Weapon currentWeapon, Room currentRoom, RoomPosition currentPosition){
		super(currentWeapon, currentRoom, currentPosition);
		this.player = player;
		numDollars = 0;
	}
	
	public int getPlayer(){
		return player;
	}
	
	public int getNumDollars(){
		return numDollars;
	}
	
	/**
	 * Increments the players current money count when they pick up a given Money object
	 * @param m
	 */
	public void pickUpMoney(Money m){
		numDollars += m.getAmount();
	}

}
