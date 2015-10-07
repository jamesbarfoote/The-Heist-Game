package game.items;

import game.Money;
import game.Room;

import java.awt.Point;

public class Desk extends Container {
	
	String filename = "_obj_desk.png";
	String direction = "N";

	public Desk(Room room, Point position, InteractableItem item, Money money) {
		super(room, position, item, money);
	}

	@Override
	public String getFilename() {
		return this.filename;
	}

	@Override
	public String getDirection() {
		return this.direction;
	}
}
