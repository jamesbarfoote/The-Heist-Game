package control;

import graphics.GameCanvas;
import graphics.GameFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Represents a player in the game. Acts as the controller, taking input, consulting the model
 * and updating the view accordingly. Is used for both single player and multiplayer
 * @author godfreya
 */
public class Player implements KeyListener, MouseListener, MouseMotionListener{
	private GameFrame frame;
	private GameCanvas canvas;
	
	public Player(){
		frame = new GameFrame();
		canvas = frame.getCanvas();
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}
	
	/**
	 * handles keyboard events
	 */
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_ESCAPE) {			
			canvas.gameMenuSelect();
		}
	}
	
	//------below are various methods for handling mouse input-------
	
	public void keyReleased(KeyEvent e) {}
	
	public void keyTyped(KeyEvent e) {}
	
	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {
		canvas.mouseReleased(e);
	}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}
	
	public void mouseMoved(MouseEvent e){
		canvas.mouseMoved(e);
	}
	
	public void mouseDragged(MouseEvent e){}
	
	//main method for starting program
	public static void main(String[] args){
		new Player();
	}
}
