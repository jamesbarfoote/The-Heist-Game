package game.items;

import game.Room;

import java.awt.Point;
import java.util.ArrayList;

public class Desk extends Container {
	
	String filename = "_obj_desk.png";
	String direction = "N";
	double[] size = {1.3, 2};

	public Desk(Room room, Point position, ArrayList<InteractableItem> items) {
		super(room, position, items);
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
	public double[] getSize(){
		return this.size;
	}
}