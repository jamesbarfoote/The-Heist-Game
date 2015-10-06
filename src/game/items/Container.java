package game.items;

import java.awt.Point;

import game.Money;
import game.Room;

public class Container extends InteractableItem{

	private Point position;
	private Money money; //Null if no money in container
	private InteractableItem item; //Null if no item in container
	
	public Container(Room room, Point position) {
		super(room);
		this.position = position;
	}
	
	public Point getPosition(){
		return position;
	}
	
	public int getMoney(){
		return money.getAmount();
	}
	
	public InteractableItem getItem(){
		return item;
	}
	
	public void itemRemovedFromContainer(){
		money = null;
		item = null;
	}

}
