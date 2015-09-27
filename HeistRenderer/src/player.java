import java.awt.Point;

public class player {
	
	Point location;
	Point oldLocation;

	public player(Point location) {
		this.location = location;
		this.oldLocation = location;
	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
	}
	
	/**
	 * @return the oldLocation
	 */
	public Point getOldLocation() {
		return oldLocation;
	}

	/**
	 * @param oldLocation the oldLocation to set
	 */
	public void setOldLocation(Point location) {
		this.oldLocation = location;
	}
}
