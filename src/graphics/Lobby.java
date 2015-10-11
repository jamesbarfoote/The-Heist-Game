package graphics;

import graphics.GameCanvas.State;
import networking.Client;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import game.Player;

public class Lobby extends Menu{
	private final int YSTART = 70; //how far up the buttons should appear on the menu
	private Player currentPlayer;
	private List<Player> players = new CopyOnWriteArrayList<Player>();
	private Client cm;
	
	
	public Lobby(GameCanvas cv, Player currentPlayer, List<Player> players, Client cm){
		this.cm = cm;
		canvas = cv;
		menuBack = GameCanvas.loadImage("lobby.png");
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = (canvas.getHeight()/2) - (menuBack.getHeight(null)/2); 
		gameButtons = new ArrayList<GameButton>();
		gameButtons.add(new GameButton("back"));
		gameButtons.add(new GameButton("start"));
		setButtonCoordinates();
		displayConnected();
	}
	
	private void displayConnected() {
		players = cm.getPlayers();
		//Loop until start is clicked
		//loop through the players list and print them onto the canvas
		
	}

	public void decline(){
		
	}
	
	public void accept(){
		
	}
	
	public void mouseReleased(MouseEvent e){
		String button = onClick(e);
		if(button == null) {
			canvas.simulateMouseMove();
			return;
		}
		switch(button){
		case "back":
			canvas.setState(State.MENU);
			break;
		case "start":
			canvas.setState(State.PLAYING);
			break;
		}
	}
	
	protected void setButtonCoordinates(){
		int yDown = menuY + menuBack.getHeight(null) - YSTART;
		int x = menuX + menuBack.getWidth(null)/4;
		GameButton current = gameButtons.get(0);
		current.setCoordinates(x, yDown);
		current = gameButtons.get(1);
		x = menuX + menuBack.getWidth(null)*3/5 + 10;
		current.setCoordinates(x, yDown);
	}
	
	public void draw(Graphics g){
		g.drawImage(menuBack, menuX, menuY, null);
		for(GameButton gb: gameButtons){
			g.drawImage(gb.getImage(), gb.getX(), gb.getY(), null);
		}
	}

}
