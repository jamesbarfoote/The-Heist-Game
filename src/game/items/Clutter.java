package game.items;

import game.Room;

import java.awt.Point;
import java.io.Serializable;

/**
 * Class for representing all random/joke items that don't serve any particular purpose
 * @author Lachlan Lee ID# 300281826
 *
 */
public class Clutter extends InteractableItem implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
