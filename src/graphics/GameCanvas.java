package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Main canvas onto which gui components are drawn
 * @author godfreya
 */
public class GameCanvas extends Canvas{
	public enum State{MENU, PLAYING}
	
	private static final String IMAGE_PATH = "images\\"; //path for locating images
	private boolean menuUp = false; //is the in game menu up?
	private Image secondScreen;     //second image for use in double buffering
	private Dialogue gameMenu; //the current game menu
	private State gameState; //determines the status of the game client
	private Confirmation dialogue; //current dialogue open, if any
	
	public GameCanvas(){
		setSize(new Dimension(900, 900)); //default size if program minimised
		setState(State.MENU);
	}
	
	/** flips the menuUp selection**/
	public void gameMenuSelect(){
		menuUp = !menuUp;
	}
	
	public boolean menuUp(){
		return menuUp;
	}
	
	public Dialogue gameMenu(){
		return gameMenu;
	}
	
	public void showConfirmation(Menu listener){
		dialogue = new Confirmation(listener);
	}
	
	public void removeConfirmation(){
		dialogue = null;
	}
	
	public void mouseReleased(MouseEvent e) {
		if(dialogue != null){
			dialogue.mouseReleased(e);
		}
		else if(gameState.equals(State.PLAYING) && menuUp){
			//if(gameMenu.mouseReleased(e)){
			//	gameMenuSelect();
			//}
		}
		else if(gameState.equals(State.MENU)){
			gameMenu.mouseReleased(e);
		}
	}
	
	public void mouseMoved(MouseEvent e){
		if(dialogue != null){
			dialogue.mouseMoved(e);
		}
		else if(gameState.equals(State.MENU) || gameState.equals(State.PLAYING) && menuUp){
			gameMenu.mouseMoved(e);
		}
	}
	
	public void setState(State s){
		gameState = s;
		if(s.equals(State.MENU)){
			gameMenu = new MainMenu(this);
		}
		else if(s.equals(State.PLAYING)){
			menuUp = false;
			gameMenu = new GameMenu(this);
		}
	}

	/**
	 * paint method for drawing the gui
	 */
	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(gameState.equals(State.PLAYING)){
			if(menuUp){
				gameMenu.draw(g, getWidth(), getHeight());
			}
		}
		else if(gameState.equals(State.MENU)){
			Image logo = loadImage("title.png");
			g.drawImage(logo, getWidth()/2 - logo.getWidth(null)/2, 25, null);
			gameMenu.draw(g, getWidth(), 25 + logo.getHeight(null) + 50);
		}
		
		if(dialogue != null){
			dialogue.draw(g, getWidth(), getHeight());
		}
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
	
	/**
	 * uses double buffering to update the gui then draw image to the screen
	 */
	public void update(Graphics g) {	
		secondScreen = createImage(getWidth(), getHeight());
		Graphics g2 = secondScreen.getGraphics();		
		// do normal redraw on second screen
		paint(g2);
		// draw second screen on window
		g.drawImage(secondScreen, 0, 0, null);
	}
}
