package game.items;

import game.Money;
import game.Room;

import java.awt.Point;

public class Desk extends Container{

	public Desk(Room room, Point position, InteractableItem item, Money money) {
		super(room, position, item, money);
	}

}
