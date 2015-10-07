package game.items;

import game.Room;

import java.awt.Point;


/**
 * Abstraction of the Immovable objects in the bank. This encompasses all objects that cannot be interacted with
 * A.K.A. set dressing for the rooms.
 * @author Lachlan
 *
 */
public class NonInteractableItem implements Item{

	private Room room;
	private Point position;
	private itemType itemType;
	
	public enum itemType{
		PLANT, COUCH, LAMP
	}
	
	public NonInteractableItem(Room room, Point point, String type){
		this.room = room;
		this.position = point;
		setType(type);
	}
	
	private void setType(String type) {
		switch(type){
			case "plant":
				itemType = itemType.PLANT;
			case "couch":
				itemType = itemType.COUCH;
			case "lamp":
				itemType = itemType.LAMP;
		}
	}

	public Room getRoom(){
		return room;
	}
	
	public Point getPosition(){
		return position;
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
}
