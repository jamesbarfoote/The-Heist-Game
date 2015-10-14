package game.items;


import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The door to the vault
 * @author Cameron Porter 300279891
 */
public class VaultDoor extends NonInteractableItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String filename = "_obj_vaultdoor.png";
	String direction = "N";
	double[] size = {0.6, 0.8};
	private ArrayList<Point> positions = new ArrayList<Point>();
	

	public VaultDoor(Point position) {
		super(position, "VaultDoor");
		this.positions.add(new Point((int) position.getX(), (int) position.getY() - 1));
	}

	@Override
	public String getFilename() {
		return this.filename;
	}

	@Override
	public String getDirection() {
		return this.direction;
	}
	
	@Override
	public double[] getSize(){
		return this.size;
	}
}