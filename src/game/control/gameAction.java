package game.control;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import graphics.GameCanvas;
import game.Money;
import game.Player;
import game.items.Container;
import game.items.InteractableItem;

/**
 * Actions by the player in the game world  not related to moving, eg: dropping money, unlocking doors
 * @author Cameron Porter 300279891 , Lachlan Lee ID# 300281826
 *
 */
public class gameAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	String action;
	Player player;
	GameCanvas canvas;

	public gameAction(String action, Player player, GameCanvas canvas) {
		this.action = action;
		this.player = player;
		this.canvas = canvas;
	}

	/**
	 * Calls whenever a key is pressed, updates the location of the player
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(canvas.getMenuSelect()) return;
		double zoom = this.canvas.getZoom();
		if (action.equals("=")){
			this.canvas.setZoom(zoom + 10, 1);
		}
		else if (action.equals("Minus")){
			this.canvas.setZoom(zoom - 10, 2);
		}
		else if(action.equals("E")){
			this.canvas.rotate("clockwise");
		}
		else if(action.equals("Q")){
			this.canvas.rotate("anti-clockwise");
		}
		else if(action.equals("P")){
			//The item (if any) that is in front of you
			InteractableItem check = player.checkforInteract(this.canvas);
			
			//If there is a container found
			if(check instanceof Container){
				Container c = (Container) check;
				player.lootContainer(c, this.canvas);
			}
			//If there is a cash stack found
			else if(check instanceof Money){
				Money m = (Money) check;
				player.pickUpMoney(m);
				this.canvas.getItems().remove(m);
			}
			//If there is a door found
			else if(player.checkForDoor(canvas) != null){
				player.unlockDoor(player.checkForDoor(canvas));
			}
		}
		else if(action.equals("B")){
			//If you have enough money to drop one full stack
			if(player.getMoneyHeld() >= 500){
				Money m = new Money(500, player.getLocation());
				this.canvas.getItems().add(m); //Adds the money object to the canvas
				player.dropMoney(500); //Decrements the players money count
			}
			//As long as you have money you can drop some
			else if(player.getMoneyHeld() > 0){
				Money m = new Money(player.getMoneyHeld(), player.getLocation());
				this.canvas.getItems().add(m); //Adds the money object to the canvas
				player.dropMoney(player.getMoneyHeld()); //Decrements the players money count
			}
		}
		this.canvas.repaint();
	}
}