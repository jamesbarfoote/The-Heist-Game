package game.items;

import java.awt.Point;

import game.Money;
import game.Room;

public class Container extends InteractableItem{

	private Point position;
	protected Money money; //Null if no money in container
	private InteractableItem item; //Null if no item in container
	
	public Container(Room room, Point position, InteractableItem item, Money money) {
		super(room, position);
		this.position = position;
		this.item = item;
		this.money = money;
	}
	
	public Point getPosition(){
		return this.position;
	}
	
	public void setPosition(Point p){
		this.position = p;
	}
	
	public int getMoney(){
		return money.getAmount();
	}
	
	public InteractableItem getItem(){
		return item;
	}
	
	/**
	 * Removes the money/item from the container when a player picks it up
	 * Should only be called by the methods that handle players picking up items
	 */
	public void containerLooted(){
		money = null;
		item = null;
	}

}
