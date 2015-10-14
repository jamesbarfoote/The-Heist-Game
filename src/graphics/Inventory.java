package graphics;

import game.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import networking.Client;

public class Inventory extends Dialogue{
	public final String message;
	public final int BUTTONSPACE = 35;
	public final int listX = 60;
	public final int listY = 95;
	public final int MAXDISPLAY = 6; //max number of items the inventory list can display at a time
	private int startList;
	protected Image scrollBar = GameCanvas.loadImage("scroll_bar.png");
	protected Rectangle box1; //area in which inventory items are drawn
	
	private Map<String, Integer> items; //for testing
	
	public Inventory(GameCanvas cv, Player p){
		message = "Inventory";
		canvas = cv;
		menuBack = GameCanvas.loadImage("inventory.png");
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = (canvas.getHeight()/2) - (menuBack.getHeight(null)/2);
		box1 = new Rectangle(menuX + listX - 20, listY + menuY - 10, menuBack.getWidth(null)/2 - (listX - 10), menuBack.getHeight(null)/2 - 10);
		
		//add close button
		gameButtons = new ArrayList<GameButton>();
		GameButton close = new GameButton("close");
		int xAcross = (canvas.getWidth()/2) - close.getImage().getWidth(null)/2;
		int yDown = (canvas.getHeight()/2) + (menuBack.getHeight(null)/2) - close.getImage().getHeight(null) - BUTTONSPACE;
		close.setCoordinates(xAcross, yDown);
		gameButtons.add(close);
		
		startList = 0;
		items = p.getInventory();
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
	
	public void keyPressed(KeyEvent e){
		
	}
	
	/**mouse wheel can be used to scroll through list of items**/
	public void mouseWheelMoved(MouseWheelEvent e){
		if(contains(e) == null) return;
		if(e.getWheelRotation() < 0){
			if(startList > 0){
				startList--;
			}
		}
		if(e.getWheelRotation() > 0){
			if(startList + MAXDISPLAY < items.size()){
				startList++;
			}
		}
	}
	
	private Rectangle contains(MouseWheelEvent e){
		if(box1.contains(e.getPoint())){
			return box1;
		}
		return null;
	}
	
	public void draw(Graphics g){
		g.drawImage(menuBack, menuX, menuY, null);	
		g.setFont(GameCanvas.textFont);
		g.drawString(message, menuX + (menuBack.getWidth(null)/2) - (message.length()*4), menuY + 65);
		GameButton gb = gameButtons.get(0);
		g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
		
		//draw list of items
		int x = box1.x;
		int y = box1.y + 12;
		List<String> itemNames = new ArrayList<String>(items.keySet());
		for(int z = 0; z < (MAXDISPLAY < items.size() ? MAXDISPLAY : items.size()); z++){  
			g.drawString("" + items.get(itemNames.get(startList + z)), x, y);
			g.drawString(itemNames.get(startList + z), x + 40, y);
			y += 25;
		}
		
		//now add scroll bar
		if(items.size() <= MAXDISPLAY) return; //no need to add scroll bar if too few items
		//spacedist is distance scrollbar moves between positions
		int spaceDist = (menuBack.getHeight(null)/2 - scrollBar.getHeight(null) - 10)/(items.size() - MAXDISPLAY);
		g.setColor(Color.BLACK);
		g.fillRect((int)box1.getMaxX(), box1.y, 2, box1.height);
		g.drawImage(scrollBar, (int)box1.getMaxX() - scrollBar.getWidth(null)/2, (menuY + listY - 10) + startList*spaceDist, null);
	}

	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return null;
	}

}
