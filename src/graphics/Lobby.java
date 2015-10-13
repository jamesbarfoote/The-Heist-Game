package graphics;

import graphics.GameCanvas.State;
import networking.Client;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import game.Player;

public class Lobby extends Menu{
	private final int YSTART = 70; //how far up the buttons should appear on the menu
	Player currentPlayer;
	List<Player> players = new CopyOnWriteArrayList<Player>();
	Client cm;
	String host = "localhost";
	
	public Lobby(GameCanvas cv, Player player, List<Player> players, String host){
		canvas = cv;
		this.currentPlayer = player;
		this.players = players;
		this.host = host;
		menuBack = GameCanvas.loadImage("lobby.png");
		menuX = (canvas.getWidth()/2) - (menuBack.getWidth(null)/2);
		menuY = (canvas.getHeight()/2) - (menuBack.getHeight(null)/2); 
		gameButtons = new ArrayList<GameButton>();
		gameButtons.add(new GameButton("back"));
		gameButtons.add(new GameButton("start"));
		setButtonCoordinates();
		startClient();
	}
	
	public void keyPressed(KeyEvent e){
		
	}
	
	public void decline(){
		
	}
	
	public void accept(String data){
		canvas.setState(State.PLAYING);
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
			
			break;
		}
	}
	
	public void startClient()
	{
		try {
			cm = new Client(43200, host, currentPlayer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Connect to the server. Change localhost to the actual host computer

		players = cm.getPlayers();
		for(Player p: players)
		{
			if(p.getID() == cm.getID())
			{
				currentPlayer = p;
			}
		}

		System.out.println("Number of players = " + players.size());
	}
	
	
	public Client getClient()
	{
		return cm;
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
