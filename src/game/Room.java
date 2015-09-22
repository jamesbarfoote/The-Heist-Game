package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Each room has a different name and contents. A room does not necessarily have to contain any money or guards. 
 * Players can move through a room at whim, providing there is nothing in place to stop them eg: A puzzle/lock or guard
 * @author Lachlan
 *
 */
public class Room {
//Stores the locations of all the objects that need to be drawn
//Canvas calls this class and asks it to draw itself
	
	private String roomName;
	private List<Item> itemsInRoom;
	private List<Money> moneyInRoom;
	private List<Guard> guardsInRoom;
	
	public Room(String roomName){
		this.roomName = roomName;
		itemsInRoom = new ArrayList<Item>();
		moneyInRoom = new ArrayList<Money>();
		guardsInRoom = new ArrayList<Guard>();
	}
	
	public String getRoomName(){
		return roomName;
	}
}
