package graphics;

import game.Door;
import game.Money;
import game.Player;
import game.Room;
import game.items.Desk;
import game.items.InteractableItem;
import game.items.Item;
import game.items.Key;
import game.items.Safe;
import game.items.VaultDoor;
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
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import data.fileReader;

/**
 * Main canvas onto which GUI components are drawn
 * @author Godfreya, Cameron Porter	300279891, james.barfoote, Lachlan Lee ID# 300281826
 */
public class GameCanvas extends Canvas{
	private static final long serialVersionUID = 1l;
	public enum State{MENU, PLAYING_SINGLE, PLAYING_MULTI, MULTI}
	
	private static final String IMAGE_PATH = "images" + File.separator + "menus" + File.separator; //path for locating images
	private static final String ASSET_PATH = "res" + File.separator; //path for locating assets.
	private boolean menuUp = false;	//is the in game menu up?
	private Image secondScreen;    	//second image for use in double buffering
	private Dialogue gameMenu; 		//the current game menu
	private State gameState; 		//determines the status of the game client
	private Dialogue dialogue; 	//current dialogue open, if any
	private Inventory inventory;	//window for observing player inventory
	private InventoryTrade inventoryTrade; //window for inventory trading between player and container
	public static final Image logo = loadImage("title.png");
	public static final Font textFont = new Font("TimesRoman", Font.PLAIN, 18); //font to be used for text in game
	private TimerThread timer;	//timer for games
	private int timerSeconds;	//seconds left on timer
	private Room room;
	public final int TIMELIMIT = 180; //time to complete mission
	
	//-----------------------------new-------------------------------//
	private AffineTransform at;
	private String[][] tiles;
	private List<Player> players = new CopyOnWriteArrayList<Player>();
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<Door> doors = new ArrayList<Door>();
	private int width, height, columns;
	private Client cm;
	private Player currentPlayer;
	private String host = "localhost";
	
	private double translateX, translateY;
	private double zoom = 200;
	private int zooming = 0;	//0 = Not zooming, 1 = zooming in, 2 = zooming out
	private String[] directions = {"N", "E", "S", "W"};
	private int direction = 0;
	private HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	private ArrayList<String> filenames;
	
	/*
	 * Everything is held within the rooms. The canvas needs a current room to draw. This room will hold an
	 * arraylist of players, these are the players that the canvas will draw. When the canvas is initialized (only happens once)
	 * a room will have to be passed in. This will be based on whether or not they are a cop or robber. 
	 */
	
	//-------------------------------------------------------------------//
	
	/**
	 * 
	 * @param Dimension of the canvas
	 * @param player
	 * @param players
	 */
	public GameCanvas(Dimension d, Player player, List<Player> players){
		this.players = players;	
		this.currentPlayer = player;
		addToImages();
		scaleImages();
		setSize(d);
		setState(State.MENU);
	}
	
	/**
	 * Sets up everything in the room
	 */
	public void initialize(){
		this.currentPlayer.setLocation(new Point(5, 20));		
		fileReader data = new fileReader("10");		
		Room currentRoom = new Room("testRoom", data.getWidth(), data.getHeight(), players);
		
		VaultDoor vaultDoor = new VaultDoor(new Point(8, 19));
		Money money = new Money(1000000, new Point(2, 4));
		Money money2 = new Money(1000, new Point(20, 5));
		Money money3 = new Money(1000, new Point(23, 6));
		Money money4 = new Money(1000, new Point(19, 3));
		Map<String, Integer> deskItems = new HashMap<String, Integer>();
		Map<String, Integer> deskItems2 = new HashMap<String, Integer>();
		deskItems.put("Money", 50);
		deskItems2.put("Old Coin", 16);
		deskItems.put("Paper Weight", 10);
		deskItems.put("Key", 1);
		deskItems2.put("Safe Combination", 1);
		deskItems2.put("Money", 200);
		currentRoom.addItem(vaultDoor);
		currentRoom.addItem(money);
		currentRoom.addItem(money2);
		currentRoom.addItem(money3);
		currentRoom.addItem(money4);
		currentRoom.addItem(new Safe(new Point(4, 7), money.getAmount(), true));
		currentRoom.addItem(new Desk(new Point(12, 10), deskItems));
		currentRoom.addItem(new Desk(new Point(22, 22), deskItems2));
		currentRoom.addDoor(new Door(false, new Point(6,3)));
		currentRoom.addDoor(new Door(true, new Point(6,11)));
		currentRoom.addDoor(new Door(false, new Point(13,6)));
		currentRoom.addDoor(new Door(false, new Point(11,14)));
		currentRoom.addDoor(new Door(false, new Point(9,19)));
		currentRoom.addDoor(new Door(false, new Point(18,12)));		
		this.room = currentRoom;
		//this.setRoom(currentRoom);
		this.tiles = data.getTiles();
		this.columns = data.getHeight();
		this.players = currentRoom.getPlayers();
		this.items = currentRoom.getItems();
		this.doors = currentRoom.getDoors();
		initialTranslate();
		
		//start timer 
		timer = new TimerThread(this);
		timerSeconds = TIMELIMIT;
		repaint();
		timer.start();
	}
	
	private void addToImages(){
		this.filenames = addToFilenames();
		for(int i = 0; i < 17; i++){	//17 different kinds of assets.
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
	
	public State getState(){
		return gameState;
	}
	
	private void scaleImages(){
		double width = 0;
		double height = 0;
		for(String filename : this.filenames){
			if(filename.equals("_door_woodenClosed.png")){
				width = this.zoom;
				height = this.zoom*(3.0/2.0);
			}
			else if(filename.equals("_floor_checkered.png")){
				width = zoom;
				height = zoom/2;
			}
			else if(filename.equals("_obj_cashStack.png")){
				width = this.zoom / 2.0;
				height = this.zoom / 3.0;
			}
			else if(filename.equals("_obj_desk.png")){
				width = this.zoom / 0.6;
				height = this.zoom / 0.8;
			}
			else if(filename.equals("_obj_floorSafe.png")){
				width = this.zoom / 1.3;
				height = this.zoom / 2.0;
			}
			else if(filename.equals("_player_1.png")){
				width = zoom;
				height = zoom*1.5;
			}
			else if(filename.equals("_obj_vaultdoor.png")){
				width = zoom;
				height = zoom;
			}
			for(int j = 0; j < 4; j++){	
				BufferedImage asset = this.images.get(this.directions[j] + filename);
				BufferedImage scaled =  getScaledImage(asset, (int) width, (int) height);
				this.images.put(this.directions[j] + filename, scaled);
			}
		}
	}
	
	/**
	 * Get a list of all the file names of all the assets
	 * @return ArrayList<String> 
	 */
	private ArrayList<String> addToFilenames(){
		ArrayList<String> filenames = new ArrayList<String>();
		
		filenames.add("_door_woodenClosed.png");
		filenames.add("_door_woodenOpen.png");
		filenames.add("_wall_block1.png");
		filenames.add("_wall_painted1.png");
		filenames.add("_wall_vault_1.png");
		filenames.add("_wall_vault_2.png");
		filenames.add("_wall_vault_3.png");
		filenames.add("_floor_checkered.png");
		filenames.add("_floor_marble1.png");
		filenames.add("_floor_marble2.png");
		filenames.add("_floor_vault.png");
		filenames.add("_player_1.png");
		filenames.add("_obj_cashStack.png");
		filenames.add("_obj_desk.png");
		filenames.add("_obj_floorSafe.png");
		filenames.add("_player_1.png");
		filenames.add("_obj_vaultdoor.png");
		
		return filenames;
	}
	
	/**
	 * Get the dimensions of the canvas
	 * @param width
	 * @param height
	 */
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
	public void showConfirmation(Menu listener, Menu.Action action, String message, Player player){
		this.currentPlayer = player;
		if(action.equals(Action.QUIT)){
			dialogue = new Confirmation(listener, message, this, null);
		}
		else if(action.equals(Action.IP)){
			dialogue = new TextDialogue(listener, message, this, player);
		}
		else if(action.equals(Action.CHOOSE)){
			dialogue = new PlayerForm(listener, message, this);
		}
		else if(action.equals(Action.SAVE) || action.equals(Action.LOAD)){
			dialogue = new TextDialogue(listener, message, this, null);
		}
	}
	
	/**remove the confirmation window**/
	public void removeConfirmation(){
		dialogue = null;
	}
	
	/**
	 * Display the players inventory
	 */
	public void showInventory(){
		if((gameState.equals(State.PLAYING_SINGLE) || gameState.equals(State.PLAYING_MULTI)) && !menuUp){
			if(inventory == null){
				inventory = new Inventory(this, currentPlayer);
			}
			else{
				inventory = null;
			}
		}
	}
	
	/**for swapping items between containers**/
	public void openTrade(Desk d){
		if((gameState.equals(State.PLAYING_SINGLE) || gameState.equals(State.PLAYING_MULTI)) && !menuUp){
			if(inventoryTrade == null){
				inventoryTrade = new InventoryTrade(this, currentPlayer, d);
			}
			else{
				inventoryTrade = null;
			}
		}
	}
	
	/**for dealing with mouse wheel movements**/
	public void mouseWheelMoved(MouseWheelEvent e){
		if(inventory != null){
			inventory.mouseWheelMoved(e);
		}
		else if(inventoryTrade != null){
			inventoryTrade.mouseWheelMoved(e);
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
		else if((gameState.equals(State.PLAYING_SINGLE)|| gameState.equals(State.PLAYING_MULTI)) && menuUp || gameState.equals(State.MENU) || gameState.equals(State.MULTI)){
			gameMenu.mouseReleased(e);				
		}
		else if(inventory != null){
			inventory.mouseReleased(e);
		}
		else if(inventoryTrade != null){
			inventoryTrade.mouseReleased(e);
		}
	}
	
	/**deal with mouse movements**/
	public void mouseMoved(Point p){
		if(dialogue != null){
			dialogue.mouseMoved(p);
		}
		else if(gameState.equals(State.MENU) || (gameState.equals(State.PLAYING_SINGLE)|| gameState.equals(State.PLAYING_MULTI)) && menuUp || gameState.equals(State.MULTI)){
			gameMenu.mouseMoved(p);
		}
		else if(inventory != null){
			inventory.mouseMoved(p);
		}
		else if(inventoryTrade != null){
			inventoryTrade.mouseMoved(p);
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
		gameState = s;
		if(s.equals(State.MENU)){
			gameMenu = new MainMenu(this);
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
		else if(s.equals(State.PLAYING_SINGLE)){ //Playing in single player mode
			menuUp = false;
			inventory = null;
			inventoryTrade = null;
			
			inventoryTrade = null;			
			gameMenu = new GameMenu(this, currentPlayer, players, this.room);
			
			this.cm = gameMenu.getClient(); //Get the client that was created
			currentPlayer = this.cm.getPlayer(); //Update the player
			
			
		}
		else if(s.equals(State.PLAYING_MULTI)) //Playing in multiplayer mode
		{
			
			menuUp = false;
			inventory = null;
			inventoryTrade = null;
			gameMenu = new GameMenu(this, currentPlayer, players, this.room);
		}
		else if(s.equals(State.MULTI)){
			this.initialize();
			gameMenu = new Lobby(this, currentPlayer, players, this.host, this.room);//Create a new game lobby
		}
	}
	
	public void decrementTimer(){
		timerSeconds --;
	}
	
	/**
	 * Draw the on-screen overlays such as timer
	 * @param graphics
	 */
	private void drawHUD(Graphics g){
		//draw timer
		int minutes;
		int seconds;
		g.setFont(new Font("TimesRoman", Font.PLAIN, 96));
		g.setColor(Color.WHITE);
		minutes = (int)timerSeconds/60;
		seconds = (int)timerSeconds % 60;
		String sSeconds = seconds < 10 ? "0" + seconds : "" + seconds;
		g.drawString(minutes + ":" + sSeconds, 100, getHeight() - 100);
		
		//draw cash amount
		g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		g.drawString("$" + currentPlayer.getMoneyHeld(), 10, 40);
		
		
		g.setColor(Color.BLACK);
	}

	/**
	 * paint method for drawing the GUI
	 */
	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(gameState.equals(State.PLAYING_SINGLE) || gameState.equals(State.PLAYING_MULTI)){
			Graphics2D g2 = (Graphics2D)g;
			try {
				drawRoom(g2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//draw the timer
			drawHUD(g);
			if(inventory != null){
				inventory.draw(g);
			}
			if(inventoryTrade != null){
				inventoryTrade.draw(g);
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
		    	if(p.getX() > this.width - this.translateX - this.zoom/8  || p.getY() > this.height - this.translateY + this.zoom){
		    		continue;
		    	}
		    	if(p.getX() < 0 - this.translateX - this.zoom  || p.getY() < 0 - this.translateY - this.zoom/2){
		    		continue;
		    	}
		    	if(tiles[i][j] == "marble"){
		    		drawTile(g, p, this.directions[direction] + "_floor_marble1.png");
		    		drawIcons(g, point);
		    	}
		    	else if(tiles[i][j] == "wall"){
		            drawWall(g, p, this.directions[direction] + "_wall_block1.png");
		    	}
		    	else if(tiles[i][j] == "marble2"){
		    		drawTile(g, p, this.directions[direction] + "_floor_marble2.png");
		    		drawIcons(g, point);
		    	}
		    	else if(tiles[i][j] == "checkered"){
		    		drawTile(g, p, this.directions[direction] + "_floor_checkered.png");
		    		drawIcons(g, point);
		    	}
		    	else if(tiles[i][j] == "vault"){
		    		drawTile(g, p, this.directions[direction] + "_floor_vault.png");
		    		drawIcons(g, point);
		    	}
		    	else if(tiles[i][j] == "vaultWall1"){
		    		drawWall(g, p, this.directions[direction] + "_wall_vault_1.png");
		    	}
		    	else if(tiles[i][j] == "vaultWall2"){
		    		drawWall(g, p, this.directions[direction] + "_wall_vault_2.png");
		    	}
		    	else if(tiles[i][j] == "vaultWall3"){
		    		drawWall(g, p, this.directions[direction] + "_wall_vault_3.png");
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
		this.at = new AffineTransform();
		this.at.translate(p.x + this.translateX, p.y + this.translateY);
		this.at.translate(0, this.zoom*-1);
		g.drawImage(asset, this.at, getParent());
	}
	
	private void drawTile(Graphics2D g, Point p, String filename){
		BufferedImage asset = this.images.get(filename);
		this.at = new AffineTransform();
		this.at.translate(p.x + this.translateX, p.y + this.translateY);
		g.drawImage(asset, this.at, getParent());
	}
	
	private void drawIcons(Graphics2D g, Point point){	

		//System.out.println("ID  = " + cm.getID());
		for(Player p: this.players)//Find the current player in the list and update the local player with it

		{
			if(p.getID() == cm.getID())//Get the current player
			{
				cm.setPlayer(p);//update the current plater in the client
			}
		}

		//Call the main client loop. This fetches and sends the latest player information
		try {
			cm.run();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<Player> temp = new CopyOnWriteArrayList<Player>();
		temp = cm.getPlayers(); //Put the updated players into a tempory aray
		
		for(Player p: players)//Remove all players execpt current player from the list of players
		{
			if(p.getID() != cm.getID())
			{
				players.remove(p);
			}
		}

		for(Player p: temp)//Add back in the updated players (except for the current player)
		{
			if(p.getID() != cm.getID())
			{
				players.add(p);
			}
				
		}
		
		for(Player p: players)//set the current player
		{
			if(p.getID() == cm.getID())
			{
				this.currentPlayer = p;
			}
		}
		
		//Draw all the player onto the screen
		for(Player player : this.players){
			Point location = player.getLocation();
			if(location.equals(point)){
				BufferedImage asset = this.images.get(player.getDirection() + "_player_1.png");
				AffineTransform at = new AffineTransform();
				double[] translation = calculatePlayerTranslate(currentPlayer.getLocation(), player.getLocation());
				at.translate(0, -this.zoom/1.2);
				at.translate(this.width/2 + translation[0], this.height/2 + translation[1]);
				g.drawImage(asset, at, getParent());
			}
		}
		
		drawItems(g, point);
	}
	
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
//			if(item.getFilename().equals("_obj_vaultdoor.png")){
//				VaultDoor vaultDoor = (VaultDoor) item;
//				for(Point p : vaultDoor.getPositions()){
//					if(p.equals(point)){
//						drawItems2(g, item);
//					}
//				}
//			}
			Point location = item.getPosition();
			if(location.equals(point)){
				drawItems2(g, item);
			}
		}
	}
	
	private void drawItems2(Graphics2D g, Item item){	//Yes this is horrible convention but I can't be bothered anymore.
		BufferedImage asset = this.images.get(this.directions[direction] + item.getFilename());
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
		else if(item.getFilename().equals("_obj_vaultdoor.png")){
			if(this.direction == 2){
				at.translate(this.zoom/4, -this.zoom/1.5);
			}
			else if(this.direction == 3){
				at.translate(0, -this.zoom/1.2);
			}
		}
		else if(item.getFilename().equals("_obj_floorSafe.png")){
			at.translate(this.zoom/8, -this.zoom/14);
		}
		else if(item.getFilename().equals("_obj_cashStack.png")){
			at.translate(this.zoom/3.8, this.zoom/4.3);
		}
		at.translate(this.width/2 + translation[0], this.height/2 + translation[1]);
		g.drawImage(asset, at, getParent());
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
			addToImages();
			scaleImages();
			translateRoom();
		}
		else if(direction == 2 && this.zoom > 20){
			this.zoom = zoom;
			this.zooming = direction;
			addToImages();
			scaleImages();
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
	
	//-------------------------------------------Getters and setters-----------------------------\\
	
	/**
	 * 
	 * @return 2D array is tiles
	 */
	public String[][] getTiles(){
		return this.tiles;
	}
	
	/**
	 * @return List of players
	 */
	public List<Player> getPlayers(){
		return this.players;
	}
	
	/**
	 * Returns all the items in the room
	 * @return all the items
	 */
	public ArrayList<Item> getItems(){
		return items;
	}
	
	/**
	 * Returns a list of all the doors
	 * @return List of Doors
	 */
	public ArrayList<Door> getDoors(){
		return doors;
	}

	/**
	 * Sets the address of the server we want to connect to
	 * @param String serverAddress
	 */
	public void setHost(String data) {
		this.host = data;		
	}
	
	/**
	 * Sets the current player
	 * @param player
	 */
	public void setCurrentPlayer(Player p)
	{
		currentPlayer = p;
	}
	
	/**
	 * Sets the client
	 * @param Client
	 */
	public void setClient(Client c)
	{
		cm = c;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
}
