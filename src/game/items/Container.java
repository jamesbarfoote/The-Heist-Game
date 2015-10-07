package game.items;

import java.awt.Point;
import java.util.ArrayList;

import game.Money;
import game.Room;

public class Container extends InteractableItem{

	private Point position;
	protected int money; //Null if no money in container
	private ArrayList<InteractableItem> items; //Null if no item in container
	
	public Container(Room room, Point position, ArrayList<InteractableItem> items) {
		super(room, position);
		this.position = position;
		this.items = items;
	}
	
	public Point getPosition(){
		return this.position;
	}
	
	public void setPosition(Point p){
		this.position = p;
	}
	
	public int getMoney(){
		return this.money;
	}
	
	public ArrayList<InteractableItem> getItem(){
		return this.items;
	}
	
	/**
	 * Removes the money/item from the container when a player picks it up
	 * Should only be called by the methods that handle players picking up items
	 */
	public void containerLooted(){
		money = 0;
		this.items = null;
	}

	@Override
	public String getFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDirection() {
		// TODO Auto-generated method stub
		return null;
	}

}
