package game.items;

import java.awt.Point;

/**
 * Interface for all the different items that can be placed in rooms of the bank
 * @author Lachlan Lee ID# 300281826
 *
 */
public interface Item {

	Point getPosition();

	String getFilename();

	String getDirection();

	double[] getSize();

	void setOldPosition(Point oldLocation);

	void setPosition(Point newLocation);

}