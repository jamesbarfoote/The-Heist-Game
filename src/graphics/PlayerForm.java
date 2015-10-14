package graphics;

import game.Player;
import game.Player.Type;
import graphics.Menu.Action;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import networking.Client;

public class PlayerForm extends Dialogue{
	private Menu listener;
	private String message;
	private String text;
	public final int LIMIT = 170; //max string length

	public PlayerForm(Menu m, String message, GameCanvas cv){
		canvas = cv;
		menuBack = GameCanvas.loadImage("popup.png");
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = (canvas.getHeight()/2) - (menuBack.getHeight(null)/2);
		text = "";
		listener = m;
		this.message = message;
		gameButtons = new ArrayList<GameButton>();
		
		//add buttons to the menu
		gameButtons.add(new GameButton("robber"));
		gameButtons.add(new GameButton("guard"));
	}
	
	public void mouseReleased(MouseEvent e){
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove(); //no button was selected
			return;
		}
		if(button.equals("robber")){
			Player p = new Player(text, new Point(1, 1), Type.robber);
			canvas.showConfirmation(listener, Action.IP, "Enter IP address", p);
			return;
		}
		if(button.equals("guard")){
			Player p = new Player(text, new Point(1, 1), Type.guard);
			canvas.showConfirmation(listener, Action.IP, "Enter IP address", p);
		}
	}
	
	public Client getClient(){
		return null;
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
			gb.setCoordinates(xAcross - (gb.getImage().getWidth(null)/2) - 10, yDown);
			g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
			xAcross += menuBack.getWidth(null)/3 + 20;
		}
		
		//draw the text
		g.setColor(Color.WHITE);
		g.fillRect(menuX + 55, menuY + 65, LIMIT, 20);
		g.setColor(Color.BLACK);
		g.drawString(text, menuX + 60, menuY + 80);
	}
	
	
}
