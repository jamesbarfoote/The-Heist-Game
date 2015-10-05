package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InventoryTrade extends Inventory{
	private Map<String, Integer> items1;
	private Map<String, Integer> items2;
	private Rectangle box2; //area for second inventory to display
	private int startList1; //place for first list to start
	private int startList2; //place for second list to start drawing
	
	public InventoryTrade(GameCanvas cv){//, Map<String, Integer> i1, Map<String, Integer> i2){
		super(cv);
		//items1 = i1;
		//items2 = i2;
		box2 = new Rectangle((int)box1.getMaxX() + 15, box1.y, box1.width, box1.height);
		
		startList1 = 0;
		items1 = new LinkedHashMap<String, Integer>();
		items1.put("Gun", 1);
		items1.put("cheese", 1);
		items1.put("Gold", 100);
		items1.put("chips", 5);
		items1.put("tomato", 2);
		items1.put("donut", 1);
		items1.put("bullets", 10);
		items1.put("gum", 2);
		items1.put("bacon", 5);
		
		startList2 = 0;
		items2 = new LinkedHashMap<String, Integer>();
		items2.put("Gun", 1);
		items2.put("cheese", 1);
		items2.put("Gold", 100);
		items2.put("chips", 5);
		items2.put("tomato", 2);
		//items2.put("donut", 1);
		//items2.put("bullets", 10);
		//items2.put("gum", 2);
		//items2.put("bacon", 5);
	}
	
	public void mouseReleased(MouseEvent e){
		if(selectSwap(e)) return;
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove(); //no button was selected
			return;
		}
		if(button.equals("close")){
			canvas.showInventory();
		}
	}
	
	private boolean selectSwap(MouseEvent e){
		if(e.getX() > box1.x && e.getX() < box1.getMaxX()){
			int y = box1.y + 12;
			for(int i = 0; i < (MAXDISPLAY < items1.size() ? MAXDISPLAY : items1.size()); i++){
				if(e.getY() >= y && e.getY() < y + 25){
					makeSwap(i, 1);
					return true;
				}
				y += 25;
			}
		}
		if(e.getX() > box2.x && e.getX() < box2.getMaxX()){
			int y = box2.y + 12;
			for(int i = 0; i < (MAXDISPLAY < items2.size() ? MAXDISPLAY : items2.size()); i++){
				if(e.getY() >= y && e.getY() < y + 25){
					makeSwap(i, 1);
					return true;
				}
				y += 25;
			}
		}
		return false;
	}
	
	private void makeSwap(int i, int takenFrom){
		if(takenFrom == 1){
			List<String> itemNames = new ArrayList<String>(items1.keySet());
			String item = itemNames.get(i + startList1);
			//remove from first map
			if(items1.get(item) == 1){
				items1.remove(item);
			}
			else{
				items1.put(item, items1.get(item) - 1);
			}
			//add to second map
			if(items2.containsKey(item)){
				items2.put(item, items2.get(item) + 1);
			}
			else{
				items2.put(item, 1);
			}
		}
		else {
			List<String> itemNames = new ArrayList<String>(items2.keySet());
			String item = itemNames.get(i + startList2);
			//remove from second map
			if(items2.get(item) == 1){
				items2.remove(item);
			}
			else{
				items2.put(item, items2.get(item) - 1);
			}
			//add to first map
			if(items1.containsKey(item)){
				items1.put(item, items1.get(item) + 1);
			}
			else{
				items1.put(item, 1);
			}
		}
	}
	
	/**mouse wheel can be used to scroll through list of items**/
	public void mouseWheelMoved(MouseWheelEvent e){
		if(contains(e) == null) return;		
		if (contains(e).equals(box1)) {
			if (e.getWheelRotation() < 0) {
				if (startList1 > 0) {
					startList1--;
				}
			}
			if (e.getWheelRotation() > 0) {
				if (startList1 + MAXDISPLAY < items1.size()) {
					startList1++;
				}
			}
		}
		else{
			if (e.getWheelRotation() < 0) {
				if (startList2 > 0) {
					startList2--;
				}
			}
			if (e.getWheelRotation() > 0) {
				if (startList2 + MAXDISPLAY < items2.size()) {
					startList2++;
				}
			}
		}
	}
	
	private Rectangle contains(MouseWheelEvent e){
		if(box1.contains(e.getPoint())){
			return box1;
		}
		else if(box2.contains(e.getPoint())){
			return box2;
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
		List<String> itemNames = new ArrayList<String>(items1.keySet());
		for(int z = 0; z < (MAXDISPLAY < items1.size() ? MAXDISPLAY : items1.size()); z++){  
			g.drawString("" + items1.get(itemNames.get(startList1 + z)), x, y);
			g.drawString(itemNames.get(startList1 + z), x + 40, y);
			y += 25;
		}
		
		//now add scroll bar
		int spaceDist; 	//spacedist is distance scrollbar moves between positions
		if(items1.size() > MAXDISPLAY){ //no need to add scroll bar if too few items
			spaceDist = (menuBack.getHeight(null)/2 - scrollBar.getHeight(null) - 10)/(items1.size() - MAXDISPLAY);
			g.setColor(Color.BLACK);
			g.fillRect((int)box1.getMaxX(), box1.y, 2, box1.height);
			g.drawImage(scrollBar, (int)box1.getMaxX() - scrollBar.getWidth(null)/2, (menuY + listY - 10) + startList1*spaceDist, null);
		}
			
		//draw second list of items
		x = box2.x;
		y = box2.y + 12;
		itemNames = new ArrayList<String>(items2.keySet());
		for(int z = 0; z < (MAXDISPLAY < items2.size() ? MAXDISPLAY : items2.size()); z++){  
			g.drawString("" + items2.get(itemNames.get(startList2 + z)), x, y);
			g.drawString(itemNames.get(startList2 + z), x + 40, y);
			y += 25;
		}
		
		//now add second scroll bar
		if(items2.size() <= MAXDISPLAY) return; //no need to add scroll bar if too few items
		spaceDist = (menuBack.getHeight(null)/2 - scrollBar.getHeight(null) - 10)/(items2.size() - MAXDISPLAY);
		g.setColor(Color.BLACK);
		g.fillRect((int)box2.getMaxX(), box2.y, 2, box2.height);
		g.drawImage(scrollBar, (int)box2.getMaxX() - scrollBar.getWidth(null)/2, (menuY + listY - 10) + startList2*spaceDist, null);
	}
}
