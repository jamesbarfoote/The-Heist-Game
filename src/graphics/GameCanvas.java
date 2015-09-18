package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * canvas onto which gui components are drawn
 * @author godfreya
 *
 */

public class GameCanvas extends Canvas implements KeyListener, MouseListener, MouseMotionListener{
	private static final String IMAGE_PATH = "images\\";
	private boolean menuUp = false;
	private Image secondScreen;
	private GameMenu gameMenu;
	
	public GameCanvas(){
		setSize(new Dimension(750, 750));
		addKeyListener(this);
		gameMenu = new GameMenu();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void paint(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(menuUp){
			gameMenu.draw(g, getWidth(), getHeight());
		}
	}
	
	public void keyPressed(KeyEvent e) {		//keylistening here is temporary, should prob be moved somewhere else
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_ESCAPE) {			
			menuUp = !menuUp;
		}
	}
	
	public void keyReleased(KeyEvent e) {		
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

    public void mouseEntered(MouseEvent e) {

	}

    public void mouseExited(MouseEvent e) {

    }

	public void mouseClicked(MouseEvent e) {

	}
	
	public void mouseMoved(MouseEvent e){
		if(menuUp){
			gameMenu.mouseMoved(e);
		}
	}
	
	public void mouseDragged(MouseEvent e){
		
	}
	
	/**
	 * Load an image from the file system using a given filename.
	 * 
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
	
	public void update(Graphics g) {	
		secondScreen = createImage(getWidth(), getHeight());
		Graphics g2 = secondScreen.getGraphics();		
		// do normal redraw
		paint(g2);
		// now draw second screen on window
		g.drawImage(secondScreen, 0, 0, null);
	}
}
