package game.control;

import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import graphics.GameCanvas;
import game.Player;

public class moveAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	String direction;
	Player player;
	GameCanvas canvas;

	public moveAction(String direction, Player player, GameCanvas canvas) {
		this.direction = direction;
		this.player = player;
		this.canvas = canvas;
	}

	/**
	 * Calls whenever a key is pressed, updates the location of the player
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action");
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
		this.canvas.translateRoom();
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