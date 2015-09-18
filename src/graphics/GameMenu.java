package graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GameMenu{
	private List<GameButton> gameButtons;
	private Image menuBack;
	private final int YSTART = 200; //how far down the buttons should appear on the menu
	private int menuX;
	private int menuY;
	
	public GameMenu(){
		menuBack = GameCanvas.loadImage("menu.png");
		gameButtons = new ArrayList<GameButton>();
		
		gameButtons.add(new GameButton("resume"));
		gameButtons.add(new GameButton("options"));
		gameButtons.add(new GameButton("quit"));
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
	
	public void mouseMoved(MouseEvent e){
		for(GameButton gb: gameButtons){
			gb.select(false);
		}
		for(GameButton gb: gameButtons){
			if(gb.contains(e)){
				gb.select(true);
				return;
			}
		}
	}
	
	private class GameButton{
		private boolean selected = false;
		private Image on;
		private Image off;
		private String name;
		private int startX; //start x coordinate for drawing
		private int startY; //start y coordinate for drawing
		
		public GameButton(String name){
			this.name = name;
			off = GameCanvas.loadImage(name + "_off.png");
			on = GameCanvas.loadImage(name + "_on.png");
		}
		
		public Image getImage(){
			return selected ? on: off;
		}
		
		public void setCoordinates(int x, int y){
			startX = x;
			startY = y;
		}
		
		public void select(boolean b){
			selected = b;
		}
		
		public boolean contains(MouseEvent e){
			return e.getX() >= startX && e.getX() <= startX + getImage().getWidth(null) &&
					e.getY() >= startY && e.getY() <= startY + getImage().getHeight(null);
		}
	}

}
