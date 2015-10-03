package graphics;

import game.Player;
import game.Room;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Main canvas onto which GUI components are drawn
 * @author Godfreya, CombuskenKid
 */
public class GameCanvas extends Canvas{
	private static final long serialVersionUID = 1l;
	public enum State{MENU, PLAYING}
	
	private static final String IMAGE_PATH = "images" + File.separator + "menus" + File.separator; //path for locating images
	private boolean menuUp = false;	//is the in game menu up?
	private Image secondScreen;    	//second image for use in double buffering
	private Dialogue gameMenu; 		//the current game menu
	private State gameState; 		//determines the status of the game client
	private Confirmation dialogue; 	//current dialogue open, if any
	private Inventory inventory;	//window for observing player inventory
	public static final Image logo = loadImage("title.png");
	public static final Font textFont = new Font("TimesRoman", Font.PLAIN, 18); //font to be used for text in game
	
	//-----------------------------new-------------------------------//
	private AffineTransform at;
	String[][] tiles;
	Room room;
	ArrayList<Player> players = new ArrayList<Player>();
	int width, height, rows, columns;
	
	double translateX, translateY;
	double zoom;
	int zooming = 0;	//0 = Not zooming, 1 = zooming in, 2 = zooming out
	
	/*
	 * Everything is held within the rooms. The canvas needs a current room to draw. This room will hold an
	 * arraylist of players, these are the players that the canvas will draw. When the canvas is initialized (only happens once)
	 * a room will have to be passed in. This will be based on whether or not they are a cop or robber. 
	 */
	
	//-------------------------------------------------------------------//
	
	public GameCanvas(Dimension d, String[][] tiles, Room room){
		setSize(d);
		setState(State.MENU);
		this.tiles = tiles;
		this.room = room;
		this.players = room.getPlayers();
		this.rows = tiles.length;
		this.columns = tiles.length;
		this.zoom = 70;
		initialTranslate();
	}
	
	/** flips the menuUp selection**/
	public void gameMenuSelect(){
		menuUp = !menuUp;
	}
	
	/**open up a confirmation window**/
	public void showConfirmation(Menu listener, String message){
		dialogue = new Confirmation(listener, message, this);
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
	
	/**for dealing with mouse wheel movements**/
	public void mouseWheelMoved(MouseWheelEvent e){
		if(inventory != null){
			inventory.mouseWheelMoved(e);
		}
	}
	
	/**deal with mouse clicks**/
	public void mouseReleased(MouseEvent e) {
		if(dialogue != null){
			dialogue.mouseReleased(e);
		}
		else if(gameState.equals(State.PLAYING) && menuUp || gameState.equals(State.MENU)){
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
		else if(gameState.equals(State.MENU) || gameState.equals(State.PLAYING) && menuUp){
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
		gameState = s;
		if(s.equals(State.MENU)){
			gameMenu = new MainMenu(this);
		}
		else if(s.equals(State.PLAYING)){
			menuUp = false;
			inventory = null;
			gameMenu = new GameMenu(this);
		}
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
			if(inventory != null){
				inventory.draw(g);
			}
			if(menuUp){
				gameMenu.draw(g);
			}
		}
		else if(gameState.equals(State.MENU)){
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
		Point location  = players.get(0).getLocation();
		this.translateX = this.translateX - (location.getX() + location.getY()) * zoom/2;
		this.translateY = this.translateY - (((columns-location.getX()-1) + location.getY()) * zoom/4);
		//Translate other players around the current player
		
	}
	
	public void translateRoom(){
		//Game is being zoomed in.
		Point location  = players.get(0).getLocation();
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
		Point oldLocation = players.get(0).getOldLocation();
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
		    	if(tiles[i][j] == "floor"){
		    		//Thread.sleep(300);
		    		drawTile(g, p);
		    		//Thread.sleep(800);
		    		drawIcons(g, point);
		    	}
		    	else if(tiles[i][j] == "wall"){
		    		drawTile(g, p);
		    		drawIcons(g, point);
		    	}
		    }
		}
	}
	
	private void drawTile(Graphics2D g, Point p){	//TODO Should pass width and height and string of image
		try {
			BufferedImage myPicture = ImageIO.read(new File("floor.jpg"));
			double width = zoom/2;
			double height = zoom/2;
			BufferedImage scaled = getScaledImage(myPicture, (int) width, (int) height);
			this.at = new AffineTransform();
			this.at.translate(p.x + this.translateX, p.y + this.translateY);
			g.drawImage(scaled, this.at, getParent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void drawIcons(Graphics2D g, Point point){		
//		Draw the player(s)	
		for(Player player : this.players){
			Point location = player.getLocation();
			if(location.equals(point)){
				try {
					BufferedImage myPicture = ImageIO.read(new File("link.jpg"));
					double width = zoom/2;
					double height = zoom/2;
					BufferedImage scaled = getScaledImage(myPicture, (int) width, (int) height);
					AffineTransform at = new AffineTransform();
					double[] translation = calculatePlayerTranslate(players.get(0).getLocation(), player.getLocation());
					//System.out.println(translation[0] + " " + translation[1]);
					at.translate(this.width/2 + translation[0], this.height/2 + translation[1]);
					g.drawImage(scaled, at, getParent());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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
}
