package game.control;

import game.Player;
import game.items.Desk;
import game.items.Item;
import graphics.GameCanvas;
import graphics.GameCanvas.State;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Player actions related to moving
 * @author Lachlan Lee ID# 300281826
 *
 */
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
		Point location = player.getLocation();
		if (direction.equals("Left") || direction.equals("A")){
			Point newLocation = new Point(location.x, location.y-1);
			this.player.setDirection(2);
			if(isValidMove(newLocation) == false){
				return;
			}
			player.setOldLocation(location);
			player.setLocation(newLocation);
		}
		else if (direction.equals("Up") || direction.equals("W")){
			Point newLocation = new Point(location.x+1, location.y);
			this.player.setDirection(1);
			if(isValidMove(newLocation) == false){
				return;
			}
			player.setOldLocation(location);
			player.setLocation(newLocation);
		}
		else if (direction.equals("Right") || direction.equals("D")){
			Point newLocation = new Point(location.x, location.y+1);
			this.player.setDirection(0);
			if(isValidMove(newLocation) == false){
				return;
			}
			player.setOldLocation(location);
			player.setLocation(newLocation);
		}
		else if (direction.equals("Down") || direction.equals("S")){
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
		if(!(canvas.getState().equals(State.PLAYING_SINGLE) || canvas.getState().equals(State.PLAYING_MULTI))){
			return false;
		}
		if(canvas.getMenuSelect()) return false;
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
		//Checks to make sure there isnt a locked door where you are trying to move
		if(player.checkForDoor(canvas) != null){
			if(player.checkForDoor(canvas).isLocked() == true){
				return false;
			}
		}
		return true;
	}
}