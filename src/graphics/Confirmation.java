package graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Confirmation extends Dialogue{
	private Menu listener;
	private String message;
	
	public Confirmation(Menu m, String message){
		listener = m;
		this.message = message;
		menuBack = GameCanvas.loadImage("popup.png");
		gameButtons = new ArrayList<GameButton>();
		
		//add buttons to the menu
		gameButtons.add(new GameButton("yes"));
		gameButtons.add(new GameButton("no"));
	}
	
	public Choice mouseReleased(MouseEvent e){
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove();
			return Choice.VOID; //no button was selected
		}
		if(button.equals("yes")){
			listener.accept();
			return Choice.YES; //action confirmed
		}
		listener.decline();
		return Choice.NO; //only other alternative is action declined
	}
	
	
	public void draw(Graphics g, int width, int height){
		menuX = (width/2) - (menuBack.getWidth(null)/2);
		menuY = (height/2) - (menuBack.getHeight(null)/2);
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
	
}
