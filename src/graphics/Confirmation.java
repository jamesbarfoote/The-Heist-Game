package graphics;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Confirmation extends Dialogue{
	private final int YSTART = 50; //how far down the buttons should appear on the menu
	private Menu listener;
	
	public Confirmation(Menu m){
		listener = m;
		menuBack = GameCanvas.loadImage("popup.png");
		gameButtons = new ArrayList<GameButton>();
		
		//add buttons to the menu
		gameButtons.add(new GameButton("yes"));
		gameButtons.add(new GameButton("no"));
	}
	
	public Choice mouseReleased(MouseEvent e){
		String button = onClick(e);
		if(button == null) {
			mouseMoved(e);
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
		
		int yDown = menuY + YSTART;
		for(GameButton gb: gameButtons){
			int x = menuX + (menuBack.getWidth(null)/2) - gb.getImage().getWidth(null)/2;
			gb.setCoordinates(x, yDown);
			g.drawImage(gb.getImage(), x, yDown, null);
			yDown += 50;
		}
	}
	
}
