package game.items;

import java.awt.Point;

import game.Room;

public class Container extends InteractableItem{

	private Point position;
	
	public Container(Room room, Point position) {
		super(room);
		this.position = position;
	}
	
	public Point getPosition(){
		return position;
	}

}
