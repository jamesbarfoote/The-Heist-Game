package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import networking.Client;

public class TextDialogue extends Dialogue{
	private Menu listener;
	private String message;
	private String text;
	public final int LIMIT = 170; //max string length
	
	public TextDialogue(Menu m, String message, GameCanvas cv){
		canvas = cv;
		menuBack = GameCanvas.loadImage("popup.png");
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = (canvas.getHeight()/2) - (menuBack.getHeight(null)/2);
		listener = m;
		this.message = message;
		text = "";
		gameButtons = new ArrayList<GameButton>();
		
		//add buttons to the menu
		gameButtons.add(new GameButton("yes"));
		gameButtons.add(new GameButton("back"));
	}
	
	//deal with mouse clicks
	public void mouseReleased(MouseEvent e){
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove(); //no button was selected
			return;
		}
		if(button.equals("yes")){
			listener.accept(text); //action confirmed
			return;
		}
		if(button.equals("back")){
			listener.decline(); //action retracted
		}
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
			if(text.length() <= 1){
				text = "";
			}
			else{
				text = text.substring(0, text.length()-1);
			}
		}
		else{
			char c = e.getKeyChar();
			if(!(Character.isLetter(c) || Character.isDigit(c)  || c == '.')) return;
			if(canvas.getGraphics().getFontMetrics().stringWidth(text + c) > LIMIT - 50) return;
			text += c;
		}
	}
	
	public void draw(Graphics g){
		g.drawImage(menuBack, menuX, menuY, null);
		g.setFont(GameCanvas.textFont);
		g.drawString(message, menuX + (menuBack.getWidth(null)/2) - (message.length()*4), menuY + 50);
		
		int yDown = menuY + (menuBack.getHeight(null)*55/100);
		int xAcross = menuX + (menuBack.getWidth(null)/3);
		for(GameButton gb: gameButtons){
			gb.setCoordinates(xAcross - (gb.getImage().getWidth(null)/2), yDown);
			g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
			xAcross += menuBack.getWidth(null)/3;
		}
		
		//draw the text
		g.setColor(Color.WHITE);
		g.fillRect(menuX + 55, menuY + 65, LIMIT, 20);
		g.setColor(Color.BLACK);
		g.drawString(text, menuX + 60, menuY + 80);
	}

	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return null;
	}

}
