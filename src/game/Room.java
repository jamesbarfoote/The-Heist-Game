package game;

import game.items.Item;

import java.awt.Point;
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
	private ArrayList<Item> itemsInRoom;
	private List<Door> roomDoors;
	private ArrayList<Player> players = new ArrayList<Player>();
	private char[][] tiles; //The floorspace of the room
	private int width;
	private int height;
	
	public Room(String roomName, int width, int height, ArrayList<Player> players){
		this.roomName = roomName;
		this.players = players;
		itemsInRoom = new ArrayList<Item>();
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
	public void moveCharacter(Point newPos, Point oldPos){
		tiles[(int) newPos.getX()][(int) newPos.getY()] = 'C';
		tiles[(int) oldPos.getX()][(int) oldPos.getY()] = 'T';
	}

	/**
	 * Updates the char array to reflect a character moving into a different room by setting its current position to
	 * and empty tile
	 * @param room1Entry
	 */
	public void removeCharacter(Point roomEntry) {
		tiles[(int) roomEntry.getX()][(int) roomEntry.getY()] = 'T';
	}

	/**
	 * Updates the char array to reflect a character moving into this room from another by setting the doors entry
	 * point to a character.
	 * @param roomEntry
	 */
	public void addCharacter(Point roomEntry) {
		tiles[(int) roomEntry.getX()][(int) roomEntry.getY()] = 'C';
	}
	
	public void addItem(Item item){
		this.itemsInRoom.add(item);
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
	
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
	
	public ArrayList<Item> getItems(){
		return itemsInRoom;
	}
	
	public List<Door> getDoors(){
		return roomDoors;
	}
}
