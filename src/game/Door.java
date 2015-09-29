package game;

import java.awt.Point;

public class Door {

	private Room room1;
	private Room room2;
	private Point room1Entry; //The Room 1 tile the door is on
	private Point room2Entry; //The Room 2 tile the door is on
	
	public Door(Room room1, Room room2, Point room1Entry, Point room2Entry){
		this.room1 = room1;
		this.room2 = room2;
		this.room1Entry = room1Entry;
		this.room2Entry = room2Entry;
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
}
