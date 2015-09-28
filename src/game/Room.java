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
	private List<Door> roomDoors;
	private char[][] tiles; //The floorspace of the room
	private int width;
	private int height;
	
	public Room(String roomName, int width, int height){
		this.roomName = roomName;
		itemsInRoom = new ArrayList<Item>();
		moneyInRoom = new ArrayList<Money>();
		roomDoors = new ArrayList<Door>();
		this.width = width;
		this.height = height;
		tiles = new char[width][height];
		setUpFloorspace();
	}

	/**
	 * Sets the room to an empty space, with empty tiles denoted as 'T'.
	 */
	private void setUpFloorspace() {
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				tiles[i][j] = 'T';
			}
		}
		
	}
	
	/**
	 * Updates the character array with the characters new position, setting the old position to an empty tile
	 * @param newPos
	 * @param oldPos
	 */
	public void moveCharacter(RoomPosition newPos, RoomPosition oldPos){
		tiles[newPos.getXPos()][newPos.getYPos()] = 'C';
		tiles[oldPos.getXPos()][oldPos.getYPos()] = 'T';
	}

	public String getRoomName(){
		return roomName;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}

	/**
	 * Updates the char array to reflect a character moving into a different room by setting its current position to
	 * and empty tile
	 * @param room1Entry
	 */
	public void removeCharacter(RoomPosition roomEntry) {
		tiles[roomEntry.getXPos()][roomEntry.getYPos()] = 'T';
	}

	/**
	 * Updates the char array to reflect a character moving into this room from another by setting the doors entry
	 * point to a character.
	 * @param roomEntry
	 */
	public void addCharacter(RoomPosition roomEntry) {
		tiles[roomEntry.getXPos()][roomEntry.getYPos()] = 'C';
	}
}
