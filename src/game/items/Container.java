package game.items;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import game.Money;

/**
 * An interactable item that can hold other interactable items, including money.
 * Cannot hold another container
 * @author Lachlan Lee ID# 300281826
 *
 */
public class Container extends InteractableItem implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Point position;
	protected int money; //Null if no money in container
	private Map<String, Integer> items; //Null if no item in container
	private Point oldPosition;

	public Container(Point position, Map<String, Integer> items, int money) {
		super(position);
		this.position = position;
		this.items = items;
		this.money = money;
	}

	public Point getPosition(){
		return this.position;
	}

	public int getMoney(){
		return this.money;
	}

	public Map<String, Integer> getItems(){
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

	@Override
	public double[] getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMoney(int money){
		this.money = money;
	}

	public void setPosition(Point p){
		this.position = p;
	}

	@Override
	public void setOldPosition(Point oldLocation) {
		this.oldPosition = oldLocation;
	}

}
