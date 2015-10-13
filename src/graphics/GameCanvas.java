package graphics;

import game.Door;
import game.Money;
import game.Player;
import game.Room;
import game.items.Desk;
import game.items.InteractableItem;
import game.items.Item;
import game.items.Safe;
//import networking.Client;
import game.items.Weapon;
import networking.Client;

import graphics.Menu.Action;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import data.fileReader;

/**
 * Main canvas onto which GUI components are drawn
 * @author Godfreya, CombuskenKid, jimmy2174
 */
public class GameCanvas extends Canvas{
	private static final long serialVersionUID = 1l;
	public enum State{MENU, PLAYING, MULTI}
	
	private static final String IMAGE_PATH = "images" + File.separator + "menus" + File.separator; //path for locating images
	private static final String ASSET_PATH = "res" + File.separator; //path for locating assets.
	private boolean menuUp = false;	//is the in game menu up?
	private Image secondScreen;    	//second image for use in double buffering
	private Dialogue gameMenu; 		//the current game menu
	private State gameState; 		//determines the status of the game client
	private Dialogue dialogue; 	//current dialogue open, if any
	private Inventory inventory;	//window for observing player inventory
	public static final Image logo = loadImage("title.png");
	public static final Font textFont = new Font("TimesRoman", Font.PLAIN, 18); //font to be used for text in game
	private static final int PI = 0;
	private TimerThread timer;	//timer for games
	private long timerSeconds;	//seconds left on timer
	public final int TIMELIMIT = 300; //time to complete mission
	
	//-----------------------------new-------------------------------//
	private AffineTransform at;
	String[][] tiles;
	Room room;
	List<Player> players = new CopyOnWriteArrayList<Player>();
	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Door> doors = new ArrayList<Door>();
	int width, height, rows, columns;
	Client cm;
	Player currentPlayer;
	
	double translateX, translateY;
	double zoom;
	int zooming = 0;	//0 = Not zooming, 1 = zooming in, 2 = zooming out
	private String[] directions = {"N", "E", "S", "W"};
	int direction = 0;
	private HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	
	/*
	 * Everything is held within the rooms. The canvas needs a current room to draw. This room will hold an
	 * arraylist of players, these are the players that the canvas will draw. When the canvas is initialized (only happens once)
	 * a room will have to be passed in. This will be based on whether or not they are a cop or robber. 
	 */
	
	//-------------------------------------------------------------------//
	
	public GameCanvas(Dimension d, Player player, List<Player> players){
		//this.cm = cm;
		
		this.players = players;
		//this.players.add(player);
		//this.rows = tiles.length;
		//this.columns = tiles.length;
		this.players.add(player);
		
		this.currentPlayer = player;
		addToImages();
		setSize(d);
		setState(State.MENU);
		//initialize();
	}
	
	public void initialize(){
		this.currentPlayer.setLocation(new Point(1, 1));
//		Player player2 = new Player(new Weapon("Badass", true), 1, new Point(6,2), game.Player.Type.robber);
//		ArrayList<Player> players = new ArrayList<Player>();
//		players.add(this.currentPlayer);
//		players.add(player2);
		
		fileReader data = new fileReader("6");
		
		Room currentRoom = new Room("testRoom", data.getWidth(), data.getHeight(), players);
		
		Money money = new Money(1000000, currentRoom, new Point(2, 4));
		Money money2 = new Money(1000000, currentRoom, new Point(20, 5));
		Money money3 = new Money(1000000, currentRoom, new Point(23, 6));
		Money money4 = new Money(1000000, currentRoom, new Point(19, 3));
		ArrayList<InteractableItem> deskItems = new ArrayList<InteractableItem>();
		//deskItems.add(money);
		currentRoom.addItem(money);
		currentRoom.addItem(money2);
		currentRoom.addItem(money3);
		currentRoom.addItem(money4);
		currentRoom.addItem(new Safe(currentRoom, new Point(4, 7), deskItems, money.getAmount()));
		currentRoom.addItem(new Desk(currentRoom, new Point(12, 10), deskItems, money.getAmount()));
		currentRoom.addItem(new Desk(currentRoom, new Point(22, 22), deskItems, money.getAmount()));
		currentRoom.addDoor(new Door(currentRoom, false, new Point(6,3), null));
		currentRoom.addDoor(new Door(currentRoom, false, new Point(6,11), null));
		currentRoom.addDoor(new Door(currentRoom, false, new Point(13,6), null));
		currentRoom.addDoor(new Door(currentRoom, false, new Point(11,14), null));
		currentRoom.addDoor(new Door(currentRoom, false, new Point(9,19), null));
		currentRoom.addDoor(new Door(currentRoom, false, new Point(18,12), null));
		
		
		this.tiles = data.getTiles();
		this.rows = data.getWidth();
		this.columns = data.getHeight();
		this.room = currentRoom;
		this.players = currentRoom.getPlayers();
		this.zoom = 80;
		this.items = currentRoom.getItems();
		this.doors = currentRoom.getDoors();
		initialTranslate();
		
		//start timer 
		timer = new TimerThread(this);
		timerSeconds = TIMELIMIT * 1000;
		timer.start();
		this.repaint();
	}
	
	private void addToImages(){
		ArrayList<String> filenames = addToFilenames();
		for(int i = 0; i < 11; i++){	//11 different kinds of assets.
			for(int j = 0; j < 4; j++){	//4 different directions.
				try {
					BufferedImage myPicture = ImageIO.read(new File(ASSET_PATH + this.directions[j] + filenames.get(i)));
					this.images.put(this.directions[j] + filenames.get(i), myPicture);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private ArrayList<String> addToFilenames(){
		ArrayList<String> filenames = new ArrayList<String>();
		
		filenames.add("_door_woodenClosed.png");
		filenames.add("_door_woodenOpen.png");
		filenames.add("_floor_carpet1.png");
		filenames.add("_floor_marble1.png");
		filenames.add("_floor_marble2.png");
		filenames.add("_obj_cashStack.png");
		filenames.add("_obj_desk.png");
		filenames.add("_obj_floorSafe.png");
		filenames.add("_player_1.png");
		filenames.add("_wall_block1.png");
		filenames.add("_wall_painted1.png");
		
		return filenames;
	}
	
	public void setDimension(int width, int height){
		this.width = width;
		this.height = height;
		initialTranslate();
	}
	
	/**for handling keyboard input**/
	public void keyPressed(KeyEvent e){
		if(dialogue != null){
			dialogue.keyPressed(e);
		}
		else{
			int code = e.getKeyCode();
			if(code == KeyEvent.VK_ESCAPE) {			
				if(dialogue == null){
					gameMenuSelect();
				}
			}
			if(code == KeyEvent.VK_I){
				showInventory();
			}
		}
		simulateMouseMove();
	}
	
	/** flips the menuUp selection**/
	public void gameMenuSelect(){
		menuUp = !menuUp;
	}
	
	/**open up a confirmation window**/
	public void showConfirmation(Menu listener, Menu.Action action, String message){
		if(action.equals(Action.QUIT)){
			dialogue = new Confirmation(listener, message, this);
		}
		else if(action.equals(Action.TEXT) || action.equals(Action.IP)){
			dialogue = new TextDialogue(listener, message, this);
		}
	}
	
	/**remove the confirmation window**/
	public void removeConfirmation(){
		dialogue = null;
	}
	
	public void showInventory(){
		if(gameState.equals(State.PLAYING) && !menuUp){
			if(inventory == null){
				inventory = new Inventory(this);
			}
			else{
				inventory = null;
			}
		}
	}
	
	/**for swapping items between containers**/
	public void openTrade(){
		
	}
	
	/**for dealing with mouse wheel movements**/
	public void mouseWheelMoved(MouseWheelEvent e){
		if(inventory != null){
			inventory.mouseWheelMoved(e);
		}
	}
	
	public boolean getMenuSelect(){
		return menuUp;
	}
	
	/**deal with mouse clicks**/
	public void mouseReleased(MouseEvent e) {
		if(dialogue != null){
			dialogue.mouseReleased(e);
		}
		else if(gameState.equals(State.PLAYING) && menuUp || gameState.equals(State.MENU) || gameState.equals(State.MULTI)){
			gameMenu.mouseReleased(e);				
		}
		else if(inventory != null){
			inventory.mouseReleased(e);
		}
	}
	
	/**deal with mouse movements**/
	public void mouseMoved(Point p){
		if(dialogue != null){
			dialogue.mouseMoved(p);
		}
		else if(gameState.equals(State.MENU) || gameState.equals(State.PLAYING) && menuUp || gameState.equals(State.MULTI)){
			gameMenu.mouseMoved(p);
		}
		else if(inventory != null){
			inventory.mouseMoved(p);
		}
	}
	
	/**
	 * simulates a mouse movement to refresh buttons
	 */
	public void simulateMouseMove(){
		mouseMoved(new Point(MouseInfo.getPointerInfo().getLocation()));
	}
	
	/**set the state of the game to playing or main menu**/
	public void setState(State s){
		//TODO
		gameState = s;
		if(s.equals(State.MENU)){
			System.out.println("Menu 1");
			gameMenu = new MainMenu(this, currentPlayer, players);
			if (timer != null) {
				timer.terminate();
			try{
				timer.join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
//			
			}			
			
		}
		else if(s.equals(State.PLAYING)){
			menuUp = false;
			inventory = null;
			gameMenu = new GameMenu(this, currentPlayer, players);
			cm = gameMenu.getClient();
			System.out.println(cm.getID());
			currentPlayer = cm.getPlayer();
			
		}
		else if(s.equals(State.MULTI)){
			//gameMenu = new Lobby(this);
		}
	}
	
	public void decrementTimer(){
		timerSeconds -= 5;
	}
	
	private void drawTimer(Graphics g){
		int minutes;
		int seconds;
		int millis;
		g.setFont(new Font("TimesRoman", Font.PLAIN, 96));
		g.setColor(Color.WHITE);
		minutes = (int)(timerSeconds/1000)/60;
		seconds = (int)(timerSeconds/1000) % 60;
		millis = (int)timerSeconds % 100;
		String sSeconds = seconds < 10 ? "0" + seconds : "" + seconds;
		String sMillis = millis < 10 ? "0" + millis : "" + millis;
		g.drawString(minutes + ":" + sSeconds + ":" + sMillis, 100, getHeight() - 100);
		g.setColor(Color.BLACK);
	}

	/**
	 * paint method for drawing the GUI
	 */
	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(gameState.equals(State.PLAYING)){
			Graphics2D g2 = (Graphics2D)g;
			try {
				drawRoom(g2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//draw the timer
			drawTimer(g);
			if(inventory != null){
				inventory.draw(g);
			}
			if(menuUp){
				gameMenu.draw(g);
			}
		}
		else if(gameState.equals(State.MENU) || gameState.equals(State.MULTI)){
			g.drawImage(logo, getWidth()/2 - logo.getWidth(null)/2, 35, null);
			gameMenu.draw(g);
		}
		
		if(dialogue != null){
			dialogue.draw(g);
		}
	}
	
	/**
	 * uses double buffering to update the GUI then draw image to the screen
	 */
	public void update(Graphics g) {	
		secondScreen = createImage(getWidth(), getHeight());
		Graphics g2 = secondScreen.getGraphics();		
		// do normal redraw on second screen
		paint(g2);
		// draw second screen on window
		g.drawImage(secondScreen, 0, 0, null);
	}
	
	/**
	 * Load an image from the file system using a given filename.
	 * @param filename
	 * @return the image loaded
	 */
	public static Image loadImage(String filename) {
		// using URL means the image loads when stored in a jar
		java.net.URL imageURL = GameCanvas.class.getResource(IMAGE_PATH + filename);
		try {
			Image img = ImageIO.read(imageURL);
			return img;
		} catch (IOException e) {
			// we've encountered an error loading the image. unsure what to do now
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}
	
	//----------------------In-game rendering methods-----------------------------------------------------//
	
	private void initialTranslate(){
		//Translate room to centre of screen
		this.translateX = this.width/2;
		this.translateY = this.height/2 + (((columns-1)/2.0)*this.zoom/2);
		//Translate room around initial player location
		Point location  = currentPlayer.getLocation();
		this.translateX = this.translateX - (location.getX() + location.getY()) * zoom/2;
		this.translateY = this.translateY - (((columns-location.getX()-1) + location.getY()) * zoom/4);
	}
	
	public void translateRoom(){
		//Game is being zoomed in.
		Point location  = currentPlayer.getLocation();
		if(this.zooming == 1){
			this.translateX = this.translateX - ((location.getX() + location.getY()) * 5);	//5 because it's currently set to zoom/2 and the change is 10
			if(location.getY() > location.getX()){
				this.translateY = this.translateY - ((location.getY() - location.getX()) * 2.5);
			}
			else if(location.getY() < location.getX()){
				this.translateY = this.translateY + ((location.getX() - location.getY()) * 2.5);
			}
			this.zooming = 0;
			return;
		}
		//Game is being zoomed out.
		else if(this.zooming == 2){
			this.translateX = this.translateX + ((location.getX() + location.getY()) * 5);	//5 because it's currently set to zoom/2 and the change is 10
			if(location.getY() > location.getX()){
				this.translateY = this.translateY + ((location.getY() - location.getX()) * 2.5);
			}
			else if(location.getY() < location.getX()){
				this.translateY = this.translateY - ((location.getX() - location.getY()) * 2.5);
			}
			this.zooming = 0;
			return;
		}
		
		//Player is moving
		Point oldLocation = currentPlayer.getOldLocation();
		//If moving north
		if(oldLocation.getX() < location.getX() && oldLocation.getY() == location.getY()){
			this.translateX = this.translateX - zoom/2;
			this.translateY = this.translateY + zoom/4;
		}
		//If moving south
		else if(oldLocation.getX() > location.getX() && oldLocation.getY() == location.getY()){
			this.translateX = this.translateX + zoom/2;
			this.translateY = this.translateY - zoom/4;
		}
		//If moving east
		else if(oldLocation.getY() < location.getY() && oldLocation.getX() == location.getX()){
			this.translateX = this.translateX - zoom/2;
			this.translateY = this.translateY - zoom/4;
		}
		//If moving west
		else if(oldLocation.getY() > location.getY() && oldLocation.getX() == location.getX()){
			this.translateX = this.translateX + zoom/2;
			this.translateY = this.translateY + zoom/4;
		}
	}
	
	private void drawRoom(Graphics2D g) throws InterruptedException{	
		for (int i = tiles.length-1; i >= 0; i--){
		    for (int j = 0; j < tiles[i].length; j++){
		    	Point point = new Point(i, j);
		    	Point p = twoDToIso(point);
		    	if(tiles[i][j] == "marble"){
		    		drawTile(g, p, this.directions[direction] + "_floor_marble1.png");
		    		drawIcons(g, point);
		    	}
		    	else if(tiles[i][j] == "wall"){
		            drawWall(g, p, this.directions[direction] + "_wall_block1.png");
		    		//drawTile(g, p, this.directions[direction] + "_floor_marble2.png");
		    	}
		    	else if(tiles[i][j] == "marble2"){
		    		drawTile(g, p, this.directions[direction] + "_floor_marble2.png");
		    		drawIcons(g, point);
		    	}
		    	else if(tiles[i][j] == "carpet"){
		    		drawTile(g, p, this.directions[direction] + "_floor_carpet1.png");
		    		drawIcons(g, point);
		    	}
		    	else if(tiles[i][j] == "door"){
		    		for(Player player : this.players){
		    			if(!player.getLocation().equals(point)){
				    		 drawWall(g, p, this.directions[direction] + "_door_woodenClosed.png");
		    			}
		    			else{
		    				drawWall(g, p, this.directions[direction] + "_door_woodenOpen.png");
		    				break;
		    			}
		    		}
		    	}
		    	else if(tiles[i][j] == "door2"){
		    		int direction = this.direction+1;
		    		if(direction == 4){
		    			direction = 0;
		    		}
		    		for(Player player : this.players){
		    			if(!player.getLocation().equals(point)){
				    		 drawWall(g, p, this.directions[direction] + "_door_woodenClosed.png");
		    			}
		    			else{
		    				drawWall(g, p, this.directions[direction] + "_door_woodenOpen.png");
		    				break;
		    			}
		    		}
		    	}
		    }
		}
	}
	
	private void drawWall(Graphics2D g, Point p, String filename){
		BufferedImage asset = this.images.get(filename);
		double width = this.zoom;
		double height = this.zoom*(3.0/2.0);
		BufferedImage scaled = getScaledImage(asset, (int) width, (int) height);
		this.at = new AffineTransform();
		this.at.translate(p.x + this.translateX, p.y + this.translateY);
		this.at.translate(0, this.zoom*-1);
		g.drawImage(scaled, this.at, getParent());
	}
	
	private void drawTile(Graphics2D g, Point p, String filename){
		BufferedImage asset = this.images.get(filename);
		double width = zoom;
		double height = zoom/2;
		BufferedImage scaled = getScaledImage(asset, (int) width, (int) height);
		this.at = new AffineTransform();
		this.at.translate(p.x + this.translateX, p.y + this.translateY);
		g.drawImage(scaled, this.at, getParent());
	}
	
	private void drawIcons(Graphics2D g, Point point){		
//		Draw the player(s)	
//		for(Player p: players)
//		{
//			if(p.getID() == cm.getID())//Get the current player
//			{
//				cm.setPlayer(p);//update the current plater in the client
//		//		System.out.println(p.getLocation().x);
//			}
//		}
//		cm.update(); //Tell the server the player has changed and to send it out
		
		
		for(Player p: players)
		{

			if(p.getID() == cm.getID())//Get the current player
			{
				cm.setPlayer(p);//update the current plater in the client
		//		System.out.println(p.getLocation().x);
			}
		}
		//cm.update(); //Tell the server the player has changed and to send it out
		try {
			cm.run();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<Player> temp4 = new CopyOnWriteArrayList<Player>();
		temp4 = cm.getPlayers();
		
		for(Player p: players)
		{
			if(p.getID() != cm.getID())
			{
				players.remove(p);
			}
		}

		for(Player p: temp4)
		{
			//System.out.println("Player has weapon ID = " + p.getID() + p.getWeapon().getWeaponType() + " and is at " + p.getLocation().x);
			if(p.getID() != cm.getID())
			{
				players.add(p);
			}
				
		}

		//System.out.println("Got updated players");
		//players = cm.getPlayers();
		
		for(Player p: players)//set the current player
		{
			if(p.getID() == cm.getID())
			{
				this.currentPlayer = p;
			//	System.out.println("Current player set");
			}
		}
		
		//players = temp4;
		
		
		//players = cm.getPlayers();
		for(Player player : this.players){
		//	System.out.println("Drawing player at: " + player.getLocation().x);
			Point location = player.getLocation();
			if(location.equals(point)){
				BufferedImage asset = this.images.get(player.getDirection() + "_player_1.png");
				double width = zoom;
				double height = zoom*1.5;
				BufferedImage scaled = getScaledImage(asset, (int) width, (int) height);
				AffineTransform at = new AffineTransform();
				double[] translation = calculatePlayerTranslate(currentPlayer.getLocation(), player.getLocation());
				at.translate(0, -this.zoom/1.2);
				at.translate(this.width/2 + translation[0], this.height/2 + translation[1]);
				g.drawImage(scaled, at, getParent());
			}
		}
		
		drawItems(g, point);
	}
	
	/*
	 * TODO optimize the game by storing the images in memory. So players store their own assets as do desks etc.
	 */
	private void drawItems(Graphics2D g, Point point){
		for(Item item : this.items){
			if(item.getFilename().equals("_obj_desk.png")){
				Desk desk = (Desk) item;
				for(Point p : desk.getPositions()){
					if(p.equals(point)){
						drawItems2(g, item);
					}
				}
			}
			Point location = item.getPosition();
			if(location.equals(point)){
				drawItems2(g, item);
			}
		}
	}
	
	private void drawItems2(Graphics2D g, Item item){	//Yes this is horrible convention but I can't be bothered anymore.
		BufferedImage asset = this.images.get(this.directions[direction] + item.getFilename());
		double width = zoom / item.getSize()[0];
		double height = zoom / item.getSize()[1];
		BufferedImage scaled = getScaledImage(asset, (int) width, (int) height);
		AffineTransform at = new AffineTransform();
		double[] translation = calculatePlayerTranslate(currentPlayer.getLocation(), item.getPosition());
		if(item.getFilename().equals("_obj_desk.png")){
			if(this.direction == 0){
				at.translate(-this.zoom/1.7, -this.zoom/1.35);
			}
			else if(this.direction == 1){
				at.translate(-this.zoom/1.8, -this.zoom/2.1);
			}
			else if(this.direction == 2){
				at.translate(-this.zoom/15, -this.zoom/2.1);
			}
			else if(this.direction == 3){
				at.translate(-this.zoom/19, -this.zoom/1.35);
			}
		}
		else if(item.getFilename().equals("_obj_floorSafe.png")){
			at.translate(this.zoom/8, -this.zoom/14);
		}
		else if(item.getFilename().equals("_obj_cashStack.png")){
			at.translate(this.zoom/3.8, this.zoom/4.3);
		}
		at.translate(this.width/2 + translation[0], this.height/2 + translation[1]);
		g.drawImage(scaled, at, getParent());
	}
	
	/*
	 * TODO should only be called when player location or other player location changes
	 * Also needs an initial call
	 */
	private double[] calculatePlayerTranslate(Point L1, Point L2){	//L1 = the player	L2 = other player
		double[] returnTranslate = new double[2];
		Point difference = new Point((int) (L2.getX() - L1.getX()), (int) (L2.getY() - L1.getY()));
		double translateX = ((difference.getX() * (zoom/2)) + (difference.getY() * (zoom/2)));
		double translateY = ((difference.getY() * (zoom/4)) + (difference.getX() * (-zoom/4)));
		returnTranslate[0] = translateX;
		returnTranslate[1] = translateY;
		return returnTranslate;
	}
	
	public void rotate(String direction){
		String[][] newArray = new String[this.tiles.length][this.tiles[0].length];
		Point oldLocation = currentPlayer.getLocation();
		Point newLocation;
		if(direction.equals("anti-clockwise")){
			for(int i=0; i<this.tiles[0].length; i++){
		        for(int j=this.tiles.length-1; j>=0; j--){
		            newArray[i][this.tiles.length-1-j] = this.tiles[j][i];
		        }
		    }
			for(Player player : this.players){
				player.rotatePlayer("anti-clockwise");
			}
			newLocation = new Point((int) oldLocation.getY(), this.tiles[(int) oldLocation.getX()].length - 1 - (int) oldLocation.getX());
			this.tiles = newArray;
			rotateAssets(direction);
		}
		else{
			for(int i=this.tiles.length-1; i>=0; i--){
		        for(int j=0; j<this.tiles[0].length; j++){
		            newArray[this.tiles.length-1-i][j] = this.tiles[j][i];
		        }
		    }
			for(Player player : this.players){
				player.rotatePlayer("clockwise");
			}
			newLocation = new Point((this.tiles[(int) oldLocation.getY()].length - 1 - (int) oldLocation.getY()), (int) oldLocation.getX());
			this.tiles = newArray;
			rotateAssets(direction);
		}
		this.currentPlayer.setOldLocation(oldLocation);
		this.currentPlayer.setLocation(newLocation);
		
		double[] translation = calculatePlayerTranslate(currentPlayer.getLocation(), currentPlayer.getOldLocation());
		this.translateX = this.translateX + translation[0];
		this.translateY = this.translateY + translation[1];
		
		//for(int i = 1; i < this.players.size(); i++){
		for(Player player : this.players){
			if(player.getID() != currentPlayer.getID()){
				oldLocation = player.getLocation();
				if(direction.equals("clockwise")){
					newLocation = new Point((this.tiles[(int) oldLocation.getY()].length - 1 - (int) oldLocation.getY()), (int) oldLocation.getX());
				}
				else{
					newLocation = new Point((int) oldLocation.getY(), this.tiles[(int) oldLocation.getX()].length - 1 - (int) oldLocation.getX());
				}
				player.setOldLocation(oldLocation);
				player.setLocation(newLocation);
			}
		}
		
		for(Item item : this.items){
			oldLocation = item.getPosition();
			if(direction.equals("clockwise")){
				newLocation = new Point((this.tiles[(int) oldLocation.getY()].length - 1 - (int) oldLocation.getY()), (int) oldLocation.getX());
			}
			else{
				newLocation = new Point((int) oldLocation.getY(), this.tiles[(int) oldLocation.getX()].length - 1 - (int) oldLocation.getX());
			}
			item.setOldPosition(oldLocation);
			item.setPosition(newLocation);
			if(item.getFilename().equals("_obj_desk.png")){
				Desk desk = (Desk) item;
				desk.setPositions(this.directions[this.direction]);
			}
		}
	}
	
	private BufferedImage getScaledImage(Image img, int w, int h){
	    BufferedImage resized = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
	    Graphics2D g2 = resized.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(img, 0, 0, w, h, null);
	    g2.dispose();
	    return resized;
	}
	
	private Point twoDToIso(Point point){
		Point tempPt = new Point(0,0);
		tempPt.x = (point.x * (int) zoom / 2) + (point.y * (int) zoom / 2);
		tempPt.y = (point.y * (int) zoom / 4) - (point.x * (int) zoom / 4);
		return tempPt;
	}
	
	public double getZoom(){
		return this.zoom;
	}
	
	public void setZoom(double zoom, int direction){
		if(direction == 1 && this.zoom < 500){
			this.zoom = zoom;
			this.zooming = direction;
			translateRoom();
		}
		else if(direction == 2 && this.zoom > 20){
			this.zoom = zoom;
			this.zooming = direction;
			translateRoom();
		}
	}
	
	private void rotateAssets(String direction) {
		if(direction.equals("clockwise")){
			this.direction--;
			if(this.direction == -1){
				this.direction = 3;
			}
		}
		else if(direction.equals("anti-clockwise")){
			this.direction++;
			if(this.direction == 4){
				this.direction = 0;
			}
		}
	}
	
	public String[][] getTiles(){
		return this.tiles;
	}
	
	public List<Player> getPlayers(){
		return this.players;
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	
	public ArrayList<Door> getDoors(){
		return doors;
	}
}
