package graphics;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Inventory extends Dialogue{
	private final String message;
	private final int BUTTONSPACE = 35;
	
	public Inventory(GameCanvas cv){
		canvas = cv;
		menuBack = GameCanvas.loadImage("inventory.png");
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = (canvas.getHeight()/2) - (menuBack.getHeight(null)/2);
		message = "Inventory";
		
		//add close button
		gameButtons = new ArrayList<GameButton>();
		GameButton close = new GameButton("close");
		int xAcross = (canvas.getWidth()/2) - close.getImage().getWidth(null)/2;
		int yDown = (canvas.getHeight()/2) + (menuBack.getHeight(null)/2) - close.getImage().getHeight(null) - BUTTONSPACE;
		close.setCoordinates(xAcross, yDown);
		gameButtons.add(close);
	}
	
	public void mouseReleased(MouseEvent e){
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove(); //no button was selected
			return;
		}
		if(button.equals("close")){
			canvas.showInventory();
		}
	}
	
	public void draw(Graphics g){
		g.drawImage(menuBack, menuX, menuY, null);	
		g.setFont(GameCanvas.textFont);
		g.drawString(message, menuX + (menuBack.getWidth(null)/2) - (message.length()*4), menuY + 65);
		GameButton gb = gameButtons.get(0);
		g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
	}

}
