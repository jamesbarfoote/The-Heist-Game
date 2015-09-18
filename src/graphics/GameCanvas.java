package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Main canvas onto which gui components are drawn
 * @author godfreya
 */
public class GameCanvas extends Canvas{
	private static final String IMAGE_PATH = "images\\"; //path for locating images
	private boolean menuUp = false; //is the in game menu up?
	private Image secondScreen;     //second image for use in double buffering
	private GameMenu gameMenu;		//in game menu, shows when escape pressed
	
	public GameCanvas(){
		setSize(new Dimension(750, 750)); //default size if program minimized
		gameMenu = new GameMenu();
	}
	
	/** flips the menuUp selection**/
	public void gameMenuSelect(){
		menuUp = !menuUp;
	}
	
	public boolean menuUp(){
		return menuUp;
	}
	
	public GameMenu gameMenu(){
		return gameMenu;
	}

	/**
	 * paint method for drawing the gui
	 */
	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(menuUp){
			gameMenu.draw(g, getWidth(), getHeight());
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
		// do normal redraw
		paint(g2);
		// now draw second screen on window
		g.drawImage(secondScreen, 0, 0, null);
	}
}
