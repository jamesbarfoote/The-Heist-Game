package game.control;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import graphics.GameCanvas;
import game.Player;

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
		double zoom = this.canvas.getZoom();
		if (action.equals("=")){
			this.canvas.setZoom(zoom + 10, 1);
		}
		else if (action.equals("Minus")){
			this.canvas.setZoom(zoom - 10, 2);
		}
		this.canvas.repaint();
	}
}