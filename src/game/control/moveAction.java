package game.control;

import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import graphics.GameCanvas;
import game.Player;
import game.items.Desk;
import game.items.Item;

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
		System.err.println("Action x =" + player.getLocation().x);
		Point location = player.getLocation();
		if (direction.equals("Left")){
			Point newLocation = new Point(location.x, location.y-1);
			this.player.setDirection(2);
			if(isValidMove(newLocation) == false){
				return;
			}
			player.setOldLocation(location);
			player.setLocation(newLocation);
		}
		else if (direction.equals("Up")){
			Point newLocation = new Point(location.x+1, location.y);
			this.player.setDirection(1);
			if(isValidMove(newLocation) == false){
				return;
			}
			player.setOldLocation(location);
			player.setLocation(newLocation);
		}
		else if (direction.equals("Right")){
			Point newLocation = new Point(location.x, location.y+1);
			this.player.setDirection(0);
			if(isValidMove(newLocation) == false){
				return;
			}
			player.setOldLocation(location);
			player.setLocation(newLocation);
		}
		else if (direction.equals("Down")){
			Point newLocation = new Point(location.x-1, location.y);
			this.player.setDirection(3);
			if(isValidMove(newLocation) == false){
				return;
			}
			player.setOldLocation(location);
			player.setLocation(newLocation);
		}
		this.canvas.translateRoom();
		this.canvas.repaint();
	}

	/**
	 * Check if the current move is valid.
	 * @param newLocation - The location the player is trying to move
	 * @return boolean - whether the new location goes through a wall
	 */
	private boolean isValidMove(Point newLocation){
		if(canvas.getMenuSelect()) return false;
		System.out.println((int) newLocation.getX());
		if(this.canvas.getTiles()[(int) newLocation.getX()][(int) newLocation.getY()].equals("wall")){
			return false;
		}
		for(Player player : this.canvas.getPlayers()){
			if(player.getLocation().equals(newLocation)){
				return false;
			}
		}
		for(Item item : this.canvas.getItems()){
			if(!item.getFilename().equals("_obj_cashStack.png") && item.getPosition().equals(newLocation)){
				return false;
			}
			else if(item.getFilename().equals("_obj_desk.png")){
				Desk desk = (Desk) item;
				if(desk.getPositions().contains(newLocation)){
					return false;
				}
			}
		}
		return true;
	}
}