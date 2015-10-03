package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Inventory extends Dialogue{
	private final String message;
	private final int BUTTONSPACE = 35;
	private final int listX = 60;
	private final int listY = 95;
	private final int MAXDISPLAY = 6; //max number of items the inventory list can display at a time
	private int startList;
	private Image scrollBar = GameCanvas.loadImage("scroll_bar.png");
	private Map<String, Integer> items;
	
	public Inventory(GameCanvas cv){
		message = "Inventory";
		canvas = cv;
		menuBack = GameCanvas.loadImage("inventory.png");
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = (canvas.getHeight()/2) - (menuBack.getHeight(null)/2);
		
		//add close button
		gameButtons = new ArrayList<GameButton>();
		GameButton close = new GameButton("close");
		int xAcross = (canvas.getWidth()/2) - close.getImage().getWidth(null)/2;
		int yDown = (canvas.getHeight()/2) + (menuBack.getHeight(null)/2) - close.getImage().getHeight(null) - BUTTONSPACE;
		close.setCoordinates(xAcross, yDown);
		gameButtons.add(close);
		
		startList = 0;
		items = new LinkedHashMap<String, Integer>();
		items.put("Gun", 1);
		items.put("cheese", 1);
		items.put("Gold", 100);
		items.put("chips", 5);
		items.put("tomato", 2);
		items.put("donut", 1);
		items.put("bullets", 10);
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
	
	/**mouse wheel can be used to scroll through list of items**/
	public void mouseWheelMoved(MouseWheelEvent e){
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
	
	public void draw(Graphics g){
		g.drawImage(menuBack, menuX, menuY, null);	
		g.setFont(GameCanvas.textFont);
		g.drawString(message, menuX + (menuBack.getWidth(null)/2) - (message.length()*4), menuY + 65);
		GameButton gb = gameButtons.get(0);
		g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
		
		//draw list of items
		int x = menuX + listX - 10;
		int y = listY + menuY;
		List<String> itemNames = new ArrayList<String>(items.keySet());
		for(int z = 0; z < MAXDISPLAY; z++){  
			g.drawString("" + items.get(itemNames.get(startList + z)), x, y);
			g.drawString(itemNames.get(startList + z), x + 40, y);
			y += 25;
		}
		//now add scroll bar
		//spacedist is distance scrollbar moves between positions
		int spaceDist = menuBack.getHeight(null)/2 - 10 - scrollBar.getHeight(null)/(items.keySet().size() - MAXDISPLAY);
		g.setColor(Color.BLACK);
		g.fillRect(menuX + menuBack.getWidth(null)/2, menuY + listY - 10, 2, menuBack.getHeight(null)/2 - 10);
		g.drawImage(scrollBar, menuX + menuBack.getWidth(null)/2 - scrollBar.getWidth(null)/2, (menuY + listY - 10) + startList*spaceDist, null);
	}

}
