import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

class MoveAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	String direction;
	player player;
	roomCanvas canvas;

	MoveAction(String direction, player player, roomCanvas canvas) {
		this.direction = direction;
		this.player = player;
		this.canvas = canvas;
	}

	/**
	 * Calls whenever a key is pressed, updates the location of the player
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Point location = player.getLocation();
		if (direction.equals("Left")){
			player.setOldLocation(location);
			player.setLocation(new Point(location.x, location.y-1));
		}
		else if (direction.equals("Up")){
			player.setOldLocation(location);
			player.setLocation(new Point(location.x+1, location.y));
		}
		else if (direction.equals("Right")){
			player.setOldLocation(location);
			player.setLocation(new Point(location.x, location.y+1));
		}
		else if (direction.equals("Down")){
			player.setOldLocation(location);
			player.setLocation(new Point(location.x-1, location.y));
		}
		this.canvas.repaint();
	}

	/**
	 * Check if the current move is valid.
	 * @param newLocation - The location the player is trying to move
	 * @return boolean - whether the new location goes through a wall
	 */
//	private boolean isValidMove(Point newLocation){
//		return true;
//	}
}