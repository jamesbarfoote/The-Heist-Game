package game.items;

import game.Room;

import java.awt.Point;

/**
 * Class for representing all random/joke items that don't serve any particular purpose
 * @author Lachlan
 *
 */
public class Clutter extends InteractableItem{

	private String name;
	
	public Clutter(Room room, Point p, String name) {
		super(p);
		this.name = name;
	}

	public String getName() {
		return name;
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
