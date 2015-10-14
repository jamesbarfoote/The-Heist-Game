package graphics;

import graphics.GameCanvas.State;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import networking.Client;

public class Notice extends Dialogue{
	private Menu listener;
	private String message;
	
	public Notice(Menu m, String message, GameCanvas cv){
		canvas = cv;
		menuBack = GameCanvas.loadImage("popup.png");
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = (canvas.getHeight()/2) - (menuBack.getHeight(null)/2);
		listener = m;
		this.message = message;
		gameButtons = new ArrayList<GameButton>();
		
		//add buttons to the menu
		gameButtons.add(new GameButton("close"));
	}
	
	//deal with mouse clicks
	public void mouseReleased(MouseEvent e){
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove(); //no button was selected
			return;
		}
		canvas.setState(State.MENU);
		canvas.removeConfirmation();
		canvas.simulateMouseMove();
	}
	
	public void keyPressed(KeyEvent e){
		
	}
	
	
	public void draw(Graphics g){
		g.drawImage(menuBack, menuX, menuY, null);
		g.setFont(GameCanvas.textFont);
		g.drawString(message, menuX + (menuBack.getWidth(null)/2) - (message.length()*4), menuY + 65);
		
		int yDown = menuY + (menuBack.getHeight(null)*55/100);
		int xAcross = menuX + (menuBack.getWidth(null)/3);
		for(GameButton gb: gameButtons){
			gb.setCoordinates(xAcross - (gb.getImage().getWidth(null)/2), yDown);
			g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
			xAcross += menuBack.getWidth(null)/3;
		}
	}

	@Override
	public Client getClient() {
		return null;
	}
	
}