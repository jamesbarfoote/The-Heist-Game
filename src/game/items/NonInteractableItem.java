package game.items;

import java.awt.Point;
import java.io.Serializable;

/**
 * Abstraction of the Immovable objects in the bank. This encompasses all
 * objects that cannot be interacted with A.K.A. set dressing for the rooms.
 * 
 * @author Lachlan Lee ID# 300281826
 *
 */
public class NonInteractableItem implements Item, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Point position, oldPosition;
	private itemType itemType;

	public enum itemType {
		PLANT, COUCH, LAMP, VAULTDOOR
	}

	public NonInteractableItem(Point point, String type) {
		this.position = point;
		this.oldPosition = point;
		setType(type);
	}

	private void setType(String type) {
		switch (type) {
		case "plant":
			itemType = itemType.PLANT;
		case "couch":
			itemType = itemType.COUCH;
		case "lamp":
			itemType = itemType.LAMP;
		case "VaultDoor":
			itemType = itemType.VAULTDOOR;
		}
	}

	public Point getPosition() {
		return position;
	}
	
	public Point getOldPosition() {
		return oldPosition;
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

	@Override
	public void setOldPosition(Point oldLocation) {
		this.oldPosition = oldLocation;
	}

	@Override
	public void setPosition(Point newLocation) {
		this.position = newLocation;
	}
}
